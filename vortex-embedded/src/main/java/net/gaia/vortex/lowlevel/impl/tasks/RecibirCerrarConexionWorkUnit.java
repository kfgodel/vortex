/**
 * 21/01/2012 19:02:44 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.meta.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo al recibir un pedido de cierre de la
 * conexión
 * 
 * @author D. García
 */
public class RecibirCerrarConexionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirCerrarConexionWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	public static RecibirCerrarConexionWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final RecibirCerrarConexionWorkUnit cierre = new RecibirCerrarConexionWorkUnit();
		cierre.contexto = contexto;
		return cierre;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Recibiendo cierre de conexión en el mensaje[{}]", contexto.getMensaje());

		final ReceptorVortex emisor = contexto.getEmisor();
		final NodoVortexConTasks nodo = contexto.getNodo();

		Loggers.RUTEO.info("CERRAR CONEXION confirmada del receptor[{}]. FIN", emisor);

		// Disparamos el cierre efectivo de la conexión
		final ProcesarCierreDeConexionWorkUnit cierreConexion = ProcesarCierreDeConexionWorkUnit.create(nodo, emisor);
		nodo.getProcesador().process(cierreConexion);
	}
}
