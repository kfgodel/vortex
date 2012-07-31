/**
 * 30/07/2012 20:43:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.cliente.server.comandos.CerrarSesionCliente;
import net.gaia.vortex.http.impl.cliente.server.comandos.CrearSesionCliente;
import net.gaia.vortex.http.impl.cliente.server.comandos.IntercambiarMensajesCliente;
import net.gaia.vortex.http.messages.PaqueteHttpVortex;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta clase representa una sesión http con un server remoto.<br>
 * Esta clase abstrae el formato texto de la sesión para enviar los mensajes vortex
 * 
 * @author D. García
 */
public class ConexionHttpCliente {

	private String idDeSesion;
	public static final String idDeSesion_FIELD = "idDeSesion";

	private ServerVortexHttpRemoto serverRemoto;
	public static final String serverRemoto_FIELD = "serverRemoto";

	private VortexHttpTextualizer textualizer;

	private long esperaMinima;
	private long esperaMaxima;

	public static ConexionHttpCliente create(final ServerVortexHttpRemoto serverRemoto,
			final VortexHttpTextualizer textualizer) throws VortexHttpException {
		final ConexionHttpCliente sesion = new ConexionHttpCliente();
		sesion.serverRemoto = serverRemoto;
		sesion.textualizer = textualizer;
		sesion.esperaMinima = 0;
		sesion.esperaMaxima = TimeMagnitude.of(7, TimeUnit.DAYS).getMillis();
		sesion.conectarAlServer();
		return sesion;
	}

	/**
	 * Inicializa el estado de esta sesión solicitando una en el servidor
	 */
	private void conectarAlServer() throws VortexHttpException {
		final CrearSesionCliente comando = CrearSesionCliente.create();
		serverRemoto.enviarComando(comando);
		this.idDeSesion = comando.getIdDeSesionCreada();
	}

	/**
	 * Desconecta esta sesión del servidor dejándola inutilizable
	 */
	public void desconectarDelServer() {
		final CerrarSesionCliente comando = CerrarSesionCliente.create(idDeSesion);
		serverRemoto.enviarComando(comando);
	}

	/**
	 * Envía los mensajes indicados y recibe los mensajes acumulados en el server para esta sesión
	 * 
	 * @param mensajesAEnviar
	 *            La lista de mensajes a enviar
	 * @return La lista de mensajes recibidos desde el server
	 */
	public List<MensajeVortex> intercambiar(final List<MensajeVortex> mensajesAEnviar) throws VortexHttpException {
		final IntercambiarMensajesCliente comando = enviarMensajes(mensajesAEnviar);
		return procesarRespuesta(comando);
	}

	/**
	 * Procesa la respuesta del servidor en el comando pasado devolviendo los mensajes recibidos
	 * 
	 * @param comando
	 *            El comando con la respuesta recibida
	 * @return La lista de mensajes recibidos
	 */
	private List<MensajeVortex> procesarRespuesta(final IntercambiarMensajesCliente comando) {
		final String textoRecibido = comando.getMensajesEnJsonRecibidos();
		if (textoRecibido == null || textoRecibido.trim().length() == 0) {
			return Collections.emptyList();
		}
		List<MensajeVortex> mensajesRecibidos;
		try {
			mensajesRecibidos = procesarTextoRecibido(textoRecibido);
		} catch (final CannotTextUnserializeException e) {
			throw new VortexHttpException("Se produjo un error al interpretar el texto recibido del servidor: "
					+ textoRecibido, e);
		}
		return mensajesRecibidos;
	}

	/**
	 * Envia los mensajes indicados al servidor devolviendo el comando con la respuesta
	 * 
	 * @param mensajesAEnviar
	 *            Los mensajes a enviar al server
	 * @return El comando con la respuesta recibida
	 */
	private IntercambiarMensajesCliente enviarMensajes(final List<MensajeVortex> mensajesAEnviar) {
		String textoEnviado;
		try {
			textoEnviado = generarTextoDeEnvio(mensajesAEnviar);
		} catch (final CannotTextSerializeException e) {
			throw new VortexHttpException(
					"Se produjo un error al generar el texto para enviar al server desde los mensajes: "
							+ mensajesAEnviar, e);
		}
		final IntercambiarMensajesCliente comando = IntercambiarMensajesCliente.create(idDeSesion, textoEnviado);
		serverRemoto.enviarComando(comando);
		return comando;
	}

	/**
	 * Procesa el texto recibido como un paquete de mensajes.<br>
	 * Negociando también la espera indicada por el servidor
	 * 
	 * @param textoRecibido
	 *            El texto recibido como respuesta
	 * @return La lista de mensajes recibidos
	 */
	private List<MensajeVortex> procesarTextoRecibido(final String textoRecibido) throws CannotTextUnserializeException {
		final PaqueteHttpVortex paqueteRecibido = textualizer.convertFromString(textoRecibido);
		negociarEsperaConServer(paqueteRecibido);
		final List<Map<String, Object>> contenidos = paqueteRecibido.getContenidos();
		final List<MensajeVortex> mensajesRecibidos = new ArrayList<MensajeVortex>(contenidos.size());
		for (final Map<String, Object> contenido : contenidos) {
			final MensajeConContenido mensajeRecibido = MensajeConContenido.regenerarDesde(contenido);
			mensajesRecibidos.add(mensajeRecibido);
		}
		return mensajesRecibidos;
	}

	/**
	 * @param mensajesAEnviar
	 * @return
	 */
	private String generarTextoDeEnvio(final List<MensajeVortex> mensajesAEnviar) throws CannotTextSerializeException {
		final PaqueteHttpVortex paqueteEnviado = PaqueteHttpVortex.create(esperaMinima, esperaMaxima);
		for (final MensajeVortex mensajeAEnviar : mensajesAEnviar) {
			final ContenidoVortex contenido = mensajeAEnviar.getContenido();
			paqueteEnviado.agregarContenido(contenido);
		}
		final String textoEnviado = textualizer.convertToString(paqueteEnviado);
		return textoEnviado;
	}

	/**
	 * Modifica los valores de espera de acuerdo a lo obtenido del server
	 * 
	 * @param paqueteRecibido
	 *            El paquete de mensajes recibido
	 */
	private void negociarEsperaConServer(final PaqueteHttpVortex paqueteRecibido) {
		this.esperaMaxima = paqueteRecibido.getProximaEsperaMaxima();
		this.esperaMinima = paqueteRecibido.getProximaEsperaMinima();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDeSesion_FIELD, idDeSesion).con(serverRemoto_FIELD, serverRemoto).toString();
	}

}
