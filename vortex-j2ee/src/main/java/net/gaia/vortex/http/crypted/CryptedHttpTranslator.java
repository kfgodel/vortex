/**
 * 14/02/2012 00:02:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.crypted;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;
import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.http.controller.HttpTranslator;
import net.gaia.vortex.http.controller.NakedHttpTranslator;
import net.gaia.vortex.protocol.http.crypted.ConcesionDeSesionYClave;
import net.gaia.vortex.protocol.http.crypted.PedidoDeSesionYClave;
import net.gaia.vortex.protocol.http.crypted.WrapperEncriptado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ar.dgarcia.encryptor.api.CryptoKey;
import ar.dgarcia.encryptor.api.FailedCryptException;
import ar.dgarcia.encryptor.api.GeneratedKeys;
import ar.dgarcia.encryptor.api.TextEncryptor;
import ar.dgarcia.encryptor.impl.RSATextEncryptor;

import com.google.common.base.Strings;
import com.tenpines.commons.exceptions.UnhandledConditionException;

/**
 * Esta clase representa el traductor de mensajes encriptados sobre HTTP
 * 
 * @author D. García
 */
@Component
@Scope("singleton")
public class CryptedHttpTranslator implements HttpTranslator {
	private static final Logger LOG = LoggerFactory.getLogger(CryptedHttpTranslator.class);

	@Autowired
	private NakedHttpTranslator translator;

	@Autowired
	private InterpreteJson interprete;

	private final TextEncryptor encryptor;

	private final Map<String, SesionEncriptada> sesionesPorId;
	private final AtomicLong secuencia;

	/**
	 * Claves de este servidor
	 */
	private final GeneratedKeys clavesDelServidor;

	public CryptedHttpTranslator() {
		encryptor = RSATextEncryptor.create();
		this.clavesDelServidor = encryptor.generateKeys();
		this.sesionesPorId = new ConcurrentHashMap<String, SesionEncriptada>();
		this.secuencia = new AtomicLong(0);
	}

	/**
	 * @see net.gaia.vortex.http.controller.HttpTranslator#translate(net.gaia.vortex.externals.http.OperacionHttp)
	 */
	@Override
	public void translate(final OperacionHttp pedido) {
		final WrapperEncriptado wrapperEncriptado = getWrapperEncriptadoFrom(pedido);
		if (wrapperEncriptado == null) {
			LOG.warn("Se recibió un request encriptado sin wrapper. Devolviendo vacío");
			return;
		}
		// Verificamos que exista la sesión indicada
		final SesionEncriptada sesionDelCliente = getSesionDelCliente(wrapperEncriptado.getSessionIdEncriptada());
		if (sesionDelCliente == null) {
			LOG.warn("Se recibió un request encriptado sin sesion asociada. Devolviendo vacío");
			return;
		}
		// Desencriptamos el mensaje original
		final String wrapperDesencriptado = desencriptar(wrapperEncriptado.getContenido());

		// Derivamos el mensaje como si fuera uno normal
		final CryptoKey claveEncriptacionDelCliente = sesionDelCliente.getClavePublica();
		final OperacionHttpEncriptada operacionEncriptada = OperacionHttpEncriptada.create(wrapperDesencriptado,
				pedido, encryptor, claveEncriptacionDelCliente);
		translator.translate(operacionEncriptada);
	}

	/**
	 * Devuelve el mensaje vortex desencriptado del request utilizando la clave privada de este
	 * server
	 * 
	 * @param contenido
	 *            El contenido recibido del wrapper encriptado
	 * @return
	 */
	private String desencriptar(final String contenido) {
		final CryptoKey clavePrivadaDelServer = clavesDelServidor.getDecriptionKey();
		String desencriptado;
		try {
			desencriptado = encryptor.decrypt(contenido, clavePrivadaDelServer);
		} catch (final FailedCryptException e) {
			LOG.error("Se produjo un error al intentar desencriptar un mensaje de uno de los clientes: " + contenido, e);
			throw new UnhandledConditionException("No se pudo desencriptar un mensje", e);
		}
		return desencriptado;
	}

	/**
	 * Devuelve la sesión con los datos de encriptado del cliente identificado por ID
	 * 
	 * @param idDeSesionEncriptado
	 *            El identificador de la sesión encriptada
	 * @return La sesión correspondiente al cliente o null si no existe una
	 */
	private SesionEncriptada getSesionDelCliente(final String idDeSesionEncriptado) {
		// Desencriptamos el ID recibido
		final String idDeSesionDesencriptado = encryptor.decrypt(idDeSesionEncriptado,
				clavesDelServidor.getDecriptionKey());
		final SesionEncriptada sesion = sesionesPorId.get(idDeSesionDesencriptado);
		return sesion;
	}

	/**
	 * Genera el objeto del mensaje encriptado desde el request recibido
	 * 
	 * @param pedido
	 *            El pedido que debería contener un objeto json con el valor encriptado
	 * @return La interpretación como wrapper encriptado
	 */
	private WrapperEncriptado getWrapperEncriptadoFrom(final OperacionHttp pedido) {
		final String textoDelWrapper = pedido.getMensajeVortex();
		if (Strings.isNullOrEmpty(textoDelWrapper)) {
			return null;
		}
		final WrapperEncriptado wrapper;
		try {
			wrapper = interprete.fromJson(textoDelWrapper, WrapperEncriptado.class);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("Se produjo un error al interpretar el wrapper recibido: ["
					+ textoDelWrapper + "]", e);
		}
		return wrapper;
	}

	/**
	 * Genera una sesión encriptada y registra la clave para comunicacion con esa sesión a partir de
	 * los datos brindados en el request
	 * 
	 * @param pedidoHttp
	 *            El pedido recibido
	 */
	public void grantKeys(final OperacionHttp pedidoHttp) {
		final PedidoDeSesionYClave pedidoDeSesion = getPedidoDeSesionFrom(pedidoHttp);
		if (pedidoDeSesion == null) {
			LOG.warn("Se recibió un request de sesion encriptada sin wrapper. Devolviendo vacío");
			return;
		}
		final String clavePublicaClienteSerializada = pedidoDeSesion.getClavePublicaCliente();
		final CryptoKey clavePublicaCliente = encryptor.deserialize(clavePublicaClienteSerializada);

		// Generamos la sesión del cliente
		final SesionEncriptada sesionEncriptada = createNewClientSession(clavePublicaCliente);

		// Encriptamos su ID para que sólo él sepa cual es
		final String idDeSesion = sesionEncriptada.getSessionId().toString();
		final String idDeSesionEncriptado = encryptor.encrypt(idDeSesion, clavePublicaCliente);

		// Obtenemos la versión serializada de la clave publica del servidor
		final CryptoKey clavePublicaDelServidor = this.clavesDelServidor.getEncriptionKey();
		final String clavePublicaDeServidorSerializada = encryptor.serialize(clavePublicaDelServidor);

		// Generamos la concesion y la enviamos
		final ConcesionDeSesionYClave concesionDeSesion = ConcesionDeSesionYClave.create(
				clavePublicaDeServidorSerializada, idDeSesionEncriptado);
		final String concesionComoJson = interprete.toJson(concesionDeSesion);
		pedidoHttp.responder(concesionComoJson);

	}

	/**
	 * Crea una nueva sesión encriptada para utilizar en las comunicaciones con los clientes
	 * 
	 * @param clavePublicaCliente
	 *            La clave publica del cliente
	 * @return La sesión creada
	 */
	private SesionEncriptada createNewClientSession(final CryptoKey clavePublicaCliente) {
		final String nuevoId = getProximoIdDeSesion();
		final SesionEncriptada sesionEncriptada = SesionEncriptada.create(nuevoId, clavePublicaCliente);
		sesionesPorId.put(nuevoId, sesionEncriptada);
		return sesionEncriptada;
	}

	/**
	 * Calcula el próximo valor para el ID de sesión e incrementa el actual
	 * 
	 * @return Un nuevo ID generado
	 */
	private String getProximoIdDeSesion() {
		return String.valueOf(secuencia.getAndIncrement());
	}

	/**
	 * Devuelve el pedido de sesión interpretado del request pasado
	 * 
	 * @param pedidoHttp
	 *            El pedido http
	 * @return El pedido de sesión reconstruido desde JSON
	 */
	private PedidoDeSesionYClave getPedidoDeSesionFrom(final OperacionHttp pedidoHttp) {
		final String textoDelWrapper = pedidoHttp.getMensajeVortex();
		if (Strings.isNullOrEmpty(textoDelWrapper)) {
			return null;
		}
		final PedidoDeSesionYClave pedidoDeSesion;
		try {
			pedidoDeSesion = interprete.fromJson(textoDelWrapper, PedidoDeSesionYClave.class);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("Se produjo un error al interpretar el pedido de sesión recibido: ["
					+ textoDelWrapper + "]", e);
		}
		return pedidoDeSesion;
	}

}
