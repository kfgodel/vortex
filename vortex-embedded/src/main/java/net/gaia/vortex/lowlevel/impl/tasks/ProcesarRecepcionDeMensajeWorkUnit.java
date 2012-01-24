/**
 * 01/12/2011 22:24:05 Copyright (C) 2011 Darío L. García
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el trabajo que hace el nodo al recibir un mensaje, después de validarlo
 * 
 * @author D. García
 */
public class ProcesarRecepcionDeMensajeWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarRecepcionDeMensajeWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	public static ProcesarRecepcionDeMensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final ProcesarRecepcionDeMensajeWorkUnit recibir = new ProcesarRecepcionDeMensajeWorkUnit();
		recibir.contexto = contexto;
		return recibir;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Comenzamos el ruteo del mensaje
		final RutearMensajeWorkUnit ruteo = RutearMensajeWorkUnit.create(contexto);
		this.contexto.getProcesador().process(ruteo);
	}
}
