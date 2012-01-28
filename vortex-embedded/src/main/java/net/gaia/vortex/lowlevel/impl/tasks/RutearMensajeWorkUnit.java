/**
 * 01/12/2011 22:54:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeRuteos;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el trabajo quimport net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
 * e hace el nodo al comenzar el ruteo de un mensaje
 * 
 * @author D. García
 */
public class RutearMensajeWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RutearMensajeWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	public static RutearMensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final RutearMensajeWorkUnit ruteo = new RutearMensajeWorkUnit();
		ruteo.contexto = contexto;
		return ruteo;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Creamos la estructura de control para realizar el ruteo
		final MensajeVortex mensaje = this.contexto.getMensaje();
		LOG.debug("Comenzando ruteo interno para mensaje[{}]", mensaje);
		final IdVortex idMensaje = mensaje.getIdentificacion();
		final ControlDeRuteo controlDeRuteo = ControlDeRuteo.create(idMensaje);
		this.contexto.setControl(controlDeRuteo);

		// Registramos el ruteo como activo (es necesaria la creación anterior)
		final ReceptorVortex emisor = contexto.getEmisor();
		final IdentificadorDeEnvio idEnvioRecibido = IdentificadorDeEnvio.create(idMensaje, emisor);
		final MemoriaDeRuteos memoriaDeRuteos = contexto.getMemoriaDeRuteos();
		memoriaDeRuteos.registrarRuteoActivo(idEnvioRecibido);

		// Tenemos que ver si es un metamensaje
		if (mensaje.esMetaMensaje()) {
			LOG.debug("MetaMensaje detectado en Mensaje[{}]", mensaje);
			// Si es meta, es para procesarlo internamente, no para enviarlo a otros nodos
			final ProcesarRecepcionDeMetamensajeWorkUnit procesoDeMetaMensaje = ProcesarRecepcionDeMetamensajeWorkUnit
					.create(contexto);
			this.contexto.getProcesador().process(procesoDeMetaMensaje);
			return;
		}
		// Si es mensaje normal simplemente elegimos quienes lo tienen que recibir
		final SeleccionarReceptoresWorkUnit seleccionNodos = SeleccionarReceptoresWorkUnit.create(contexto);
		this.contexto.getProcesador().process(seleccionNodos);
	}
}
