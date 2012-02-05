/**
 * 27/11/2011 21:17:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.nodo;

import java.util.concurrent.atomic.AtomicBoolean;

import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesVortex;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortexConSesion;
import net.gaia.vortex.lowlevel.impl.tasks.ComenzarProcesoDeMensajeWorkUnit;
import net.gaia.vortex.lowlevel.impl.tasks.ProcesarCierreDeConexionWorkUnit;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la sesión vortex creada desde el nodo y dada al cliente para poder enviar y
 * recibir mensajes
 * 
 * @author D. García
 */
public class SesionVortexImpl implements SesionVortex, MensajeVortexHandler {
	private static final Logger LOG = LoggerFactory.getLogger(SesionVortexImpl.class);

	/**
	 * Receptor de los mensajes de esta sesión
	 */
	private ReceptorVortexConSesion receptorEmisor;
	public static final String receptorEmisor_FIELD = "receptorEmisor";
	private NodoVortexConTasks nodo;
	public static final String nodo_FIELD = "nodo";
	private AtomicBoolean cerrada;
	public static final String cerrada_FIELD = "cerrada";
	private MensajeVortexHandler handlerDelReceptor;

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#enviar(net.gaia.vortex.protocol.MensajeVortex)
	 */
	@Override
	public void enviar(final MensajeVortex mensajeEnviado) {
		if (cerrada.get()) {
			LOG.error("Se intentó enviar un mensaje[{}] por una sesión cerrada[{}]", mensajeEnviado, this);
			return;
		}
		LOG.debug("Mensaje[{}] recibido desde receptor[{}] en nodo[{}]", new Object[] { mensajeEnviado, receptorEmisor,
				nodo });
		Loggers.RUTEO.info("RECIBIDO mensaje[{}] desde receptor[{}] en nodo[{}]. Contenido: [{}]", new Object[] {
				mensajeEnviado, receptorEmisor, nodo, mensajeEnviado.toPrettyPrint() });
		final ColaDeMensajesVortex colaDeMensajes = receptorEmisor.getColaDeMensajes();
		final boolean esElProximo = colaDeMensajes.agregarPendiente(mensajeEnviado);
		if (!esElProximo) {
			LOG.debug("Mensaje[{}] encolado hasta procesar previos", mensajeEnviado, receptorEmisor);
			return;
		}

		// Comenzamos con el procesamiento
		final ComenzarProcesoDeMensajeWorkUnit comienzoDeProceso = ComenzarProcesoDeMensajeWorkUnit.create(
				mensajeEnviado, receptorEmisor, nodo);
		nodo.getProcesador().process(comienzoDeProceso);
	}

	public static SesionVortexImpl create(final MensajeVortexHandler handlerDeMensajes,
			final NodoVortexConTasks nodoHost) {
		final SesionVortexImpl sesion = new SesionVortexImpl();
		sesion.handlerDelReceptor = handlerDeMensajes;
		sesion.receptorEmisor = ReceptorVortexConSesion.create(sesion);
		sesion.nodo = nodoHost;
		sesion.cerrada = new AtomicBoolean(false);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#cerrar()
	 */
	@Override
	public void cerrar() {
		if (cerrada.get()) {
			LOG.info("Se solicito cierre de sesión ya cerrada[{}]", this);
			return;
		}
		// Marcamos que ya estamos cerrados, por lo que se ignorará toda circulación de mensajes
		cerrada.set(true);
		final ProcesarCierreDeConexionWorkUnit cierreDeConexion = ProcesarCierreDeConexionWorkUnit.create(nodo,
				receptorEmisor);
		nodo.getProcesador().process(cierreDeConexion);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#estaCerrada()
	 */
	@Override
	public boolean estaCerrada() {
		return cerrada.get();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeRecibido(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
		if (cerrada.get()) {
			LOG.warn("Se recibió un mensaje[{}] en una sesión cerrada[{}]", nuevoMensaje, this);
			return;
		}
		handlerDelReceptor.onMensajeRecibido(nuevoMensaje);
	}

	public ReceptorVortexConSesion getReceptorEmisor() {
		return receptorEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(cerrada_FIELD, cerrada.get()).add(receptorEmisor_FIELD, receptorEmisor)
				.add(nodo_FIELD, nodo).toString();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeConErrores(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.lowlevel.api.ErroresDelMensaje)
	 */
	@Override
	public void onMensajeConErrores(final MensajeVortex mensajeFallido, final ErroresDelMensaje errores) {
		handlerDelReceptor.onMensajeConErrores(mensajeFallido, errores);
	}
}
