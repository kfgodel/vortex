/**
 * 16/01/2012 19:59:35 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.tags.TagSummarizer;
import net.gaia.vortex.protocol.messages.meta.QuitarTags;

/**
 * Esta clase representa la operación realizada por el nodo al recibir un mensaje de quitar tags
 * 
 * @author D. García
 */
public class RecibirQuitarTagsWorkunit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	private QuitarTags mensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Quitarmos los tags de los declarados por el receptor
		final List<String> nuevosTags = mensaje.getTags();
		final ReceptorVortex receptorAModificar = contexto.getEmisor();
		final TagSummarizer summarizer = contexto.getTagSummarizer();
		summarizer.quitarTagsPara(receptorAModificar, nuevosTags);

		// Si hay cambios de tags globales, se disparará notificaciones a los receptores vecinos
	}

	public static RecibirQuitarTagsWorkunit create(final ContextoDeRuteoDeMensaje contexto, final QuitarTags mensaje) {
		final RecibirQuitarTagsWorkunit agregar = new RecibirQuitarTagsWorkunit();
		agregar.contexto = contexto;
		agregar.mensaje = mensaje;
		return agregar;
	}

}
