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
import net.gaia.vortex.protocol.IdVortex;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa el trabajo quimport net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
 * e hace el nodo al comenzar el ruteo de un mensaje
 * 
 * @author D. García
 */
public class RutearMensajeWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	public static RutearMensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final RutearMensajeWorkUnit ruteo = new RutearMensajeWorkUnit();
		ruteo.contexto = contexto;
		return ruteo;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Creamos la estructura de control para realizar el ruteo
		final MensajeVortexEmbebido mensaje = this.contexto.getMensaje();
		final IdVortex idMensaje = mensaje.getIdentificacion();
		final ControlDeRuteo controlDeRuteo = ControlDeRuteo.create(idMensaje);
		this.contexto.setControl(controlDeRuteo);

		// Tenemos que ver si es un metamensaje
		if (mensaje.isMetaMensaje()) {
			// Si es meta, es para procesarlo internamente, no para enviarlo a otros nodos
			final ProcesarMetamensajeWorkUnit procesoDeMetaMensaje = ProcesarMetamensajeWorkUnit.create(contexto);
			this.contexto.getProcesador().process(procesoDeMetaMensaje);
			return;
		}
		// Si es mensaje normal simplemente elegimos quienes lo tienen que recibir
		final SeleccionarReceptoresWorkUnit seleccionNodos = SeleccionarReceptoresWorkUnit.create(contexto);
		this.contexto.getProcesador().process(seleccionNodos);
	}
}
