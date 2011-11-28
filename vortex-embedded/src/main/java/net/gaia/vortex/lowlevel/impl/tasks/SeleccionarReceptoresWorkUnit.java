/**
 * 27/11/2011 22:13:13 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.RegistroDeReceptores;
import net.gaia.vortex.lowlevel.impl.SeleccionDeReceptores;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa la tarea de seleccionar los mejores receptores para un mensaje ruteado
 * 
 * @author D. García
 */
public class SeleccionarReceptoresWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	public static SeleccionarReceptoresWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final SeleccionarReceptoresWorkUnit seleccion = new SeleccionarReceptoresWorkUnit();
		seleccion.contexto = contexto;
		return seleccion;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		final MensajeVortexEmbebido mensaje = this.contexto.getMensaje();
		final List<String> tagsDelMensaje = mensaje.getTagsDestino();
		final RegistroDeReceptores registro = contexto.getRegistroDeReceptoresDelNodo();
		final SeleccionDeReceptores seleccion = registro.getReceptoresInteresadosEn(tagsDelMensaje);
		if (seleccion.esVacia()) {
			// El mensaje no le interesa a nadie
			final DevolverConfirmacionSinConsumoWorkUnit devolucion = DevolverConfirmacionSinConsumoWorkUnit.create(
					contexto, seleccion.getExcluidos());
			contexto.getProcesador().process(devolucion);
			return;
		}
		// Si le interesa a alguien tenemos que entregarle el mensaje
		final EnviarMensajeAInteresadosWorkUnit envioAInteresados = EnviarMensajeAInteresadosWorkUnit.create(contexto,
				seleccion);
		contexto.getProcesador().process(envioAInteresados);
	}
}
