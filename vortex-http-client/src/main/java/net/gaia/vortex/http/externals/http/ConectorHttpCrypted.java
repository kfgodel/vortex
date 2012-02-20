/**
 * 14/02/2012 22:17:18 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.http.externals.http;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;
import net.gaia.vortex.protocol.http.VortexWrapper;
import net.gaia.vortex.protocol.http.crypted.ConcesionDeSesionYClave;
import net.gaia.vortex.protocol.http.crypted.PedidoDeSesionYClave;
import net.gaia.vortex.protocol.http.crypted.WrapperEncriptado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.encryptor.api.CryptoKey;
import ar.dgarcia.encryptor.api.GeneratedKeys;
import ar.dgarcia.encryptor.api.TextEncryptor;
import ar.dgarcia.encryptor.impl.RSATextEncryptor;
import ar.dgarcia.http.simple.api.HttpResponseProvider;
import ar.dgarcia.http.simple.api.StringRequest;
import ar.dgarcia.http.simple.api.StringResponse;

import com.google.common.base.Objects;

/**
 * Esta clase representa un conector http usado por el cliente para enviar mensajes al nodo en fora
 * encriptada
 * 
 * @author D. García
 */
public class ConectorHttpCrypted extends ConectorHttpNaked {
	private static final Logger LOG = LoggerFactory.getLogger(ConectorHttpCrypted.class);

	private TextEncryptor encryptor;
	private GeneratedKeys clavesDelCliente;
	private CryptoKey clavePublicaDelNodoServidor;
	private String idDeSesionEncriptadoParaServidor;
	private String idDeSesion;
	public static final String idDeSesion_FIELD = "idDeSesion";
	private String urlParaPedidoDeKeys;
	public static final String urlParaPedidoDeKeys_FIELD = "urlParaPedidoDeKeys";

	public static ConectorHttpCrypted createCrypted(final String urlParaPedidoDeClaves,
			final String urlParaEnvioDeMensajes, final InterpreteJson interpreteJson,
			final HttpResponseProvider httpProvider) {
		final ConectorHttpCrypted conector = new ConectorHttpCrypted();
		conector.setHttpProvider(httpProvider);
		conector.initialize(urlParaEnvioDeMensajes, interpreteJson);
		conector.encryptor = RSATextEncryptor.create();
		conector.clavesDelCliente = conector.encryptor.generateKeys();
		conector.urlParaPedidoDeKeys = urlParaPedidoDeClaves;
		return conector;
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttpNaked#translateFromHttp(ar.dgarcia.http.simple.api.StringResponse)
	 */
	@Override
	protected VortexWrapper translateFromHttp(final StringResponse httpResponse) throws VortexConnectorException {
		final String wrapperEncriptado = getContentFrom(httpResponse);
		final String wrapperComoJson = encryptor.decrypt(wrapperEncriptado, clavesDelCliente.getDecriptionKey());
		LOG.debug("Wrapper desencriptado: {}", wrapperComoJson);
		final VortexWrapper wrapperDeMensajes = translateFromJson(wrapperComoJson);
		return wrapperDeMensajes;
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttpNaked#translateToHttp(net.gaia.vortex.protocol.http.VortexWrapper)
	 */
	@Override
	protected StringRequest translateToHttp(final VortexWrapper enviado) {
		final String wrapperSinEncriptar = translateToJson(enviado);
		final CryptoKey clavePublicaDelNodo = getClavePublicaDelNodoServidor();
		final String contenidoEncriptado = encryptor.encrypt(wrapperSinEncriptar, clavePublicaDelNodo);
		LOG.debug("Wrapper encriptado: {}", wrapperSinEncriptar);
		final WrapperEncriptado wrapperEncriptado = WrapperEncriptado.create(idDeSesionEncriptadoParaServidor,
				contenidoEncriptado);
		final String wrapperEncriptadoComoTexto = translateToJson(wrapperEncriptado);
		return createHttpRequestFor(wrapperEncriptadoComoTexto);
	}

	/**
	 * Devuelve la clave publica del servidor para encriptar, solicitándola en el momento si no la
	 * tiene aún
	 * 
	 * @return La clave publica del servidor
	 */
	private CryptoKey getClavePublicaDelNodoServidor() {
		if (clavePublicaDelNodoServidor == null) {
			clavePublicaDelNodoServidor = negociarClavesConServidor();
		}
		return clavePublicaDelNodoServidor;
	}

	/**
	 * Se conecta al servidor para conseguir la clave pública
	 * 
	 * @return La clave publica del servidor
	 */
	private CryptoKey negociarClavesConServidor() {
		LOG.debug("Solicitando sesión encriptada al servidor: {}", urlParaPedidoDeKeys);
		final StringRequest requestConPedidoDeClaveYSesion = crearRequestDePedidoDeClave();
		final StringResponse respuestConConcesion = getHttpProvider().sendRequest(requestConPedidoDeClaveYSesion);
		final CryptoKey clavePublicaDelServidor = recuperarClaveYsesionDe(respuestConConcesion);
		return clavePublicaDelServidor;
	}

	/**
	 * Intenta recuperar de la respuesta recibida la clave del servidor y dejar registrada la sesión
	 * recibida
	 * 
	 * @param respuestConConcesion
	 *            La respuesta al pedido de sesión
	 * @return La clave publica recibida
	 */
	private CryptoKey recuperarClaveYsesionDe(final StringResponse respuestConConcesion) {
		// Reconstruimos el objeto
		final String consesionComoJson = getContentFrom(respuestConConcesion);
		final ConcesionDeSesionYClave concesion = translateConcesionFromJson(consesionComoJson);

		// Recuperamos la clave
		final String claveDelServidorSerializada = concesion.getClavePublicaServer();
		final CryptoKey clavePublicaDelServidor = encryptor.deserialize(claveDelServidorSerializada);

		// Recuperamos el ID de sesión para indicar al servidor
		final String sessionIdEncriptadoParaCliente = concesion.getSessionIdEncriptado();
		final CryptoKey clavePrivadaDelCliente = clavesDelCliente.getDecriptionKey();
		idDeSesion = encryptor.decrypt(sessionIdEncriptadoParaCliente, clavePrivadaDelCliente);
		idDeSesionEncriptadoParaServidor = encryptor.encrypt(idDeSesion, clavePublicaDelServidor);
		LOG.debug("Clave publica del servidor recibida y sesión obtenida: {}", idDeSesion);

		return clavePublicaDelServidor;
	}

	/**
	 * Devuelve la concesión interpretada del String pasado como JSON
	 * 
	 * @param consesionComoJson
	 *            El texto que representa una concesión de clave y sesión
	 * @return la concesión interpretada
	 */
	private ConcesionDeSesionYClave translateConcesionFromJson(final String consesionComoJson) {
		final ConcesionDeSesionYClave concesion;
		try {
			concesion = getInterprete().fromJson(consesionComoJson, ConcesionDeSesionYClave.class);
		} catch (final JsonConversionException e) {
			throw new VortexConnectorException("El server[" + getServerUrl()
					+ "] envio una respuesta que no podemos transformar en ConcesionDeSesionYClave: "
					+ consesionComoJson, e);
		}
		return concesion;
	}

	/**
	 * Crea el request para solicitar la clave publica y una sesión para este conector
	 * 
	 * @return El request a enviar como pedido
	 */
	private StringRequest crearRequestDePedidoDeClave() {
		final CryptoKey clavePublicaDelCliente = clavesDelCliente.getEncriptionKey();
		final String claveDelClienteSerializada = encryptor.serialize(clavePublicaDelCliente);
		final PedidoDeSesionYClave pedidoDeClaveYSesion = PedidoDeSesionYClave.create(claveDelClienteSerializada);
		final String pedidoComoJson = translateToJson(pedidoDeClaveYSesion);
		final StringRequest request = createHttpRequestFor(pedidoComoJson);
		// Para las claves se usa otra URL
		request.setUrl(urlParaPedidoDeKeys);
		return request;
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttpNaked#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idDeSesion_FIELD, idDeSesion).add(serverUrl_FIELD, getServerUrl())
				.add(urlParaPedidoDeKeys_FIELD, urlParaPedidoDeKeys).toString();
	}

}
