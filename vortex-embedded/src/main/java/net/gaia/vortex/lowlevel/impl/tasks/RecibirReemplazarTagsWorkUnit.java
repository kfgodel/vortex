/**
 * 16/01/2012 20:02:37 Copyright (C) 2011 Darío L. García
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

import java.util.HashSet;
import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.tags.TagSummarizer;
import net.gaia.vortex.protocol.messages.meta.ReemplazarTags;

/**
 * Esta clase representa la operación que realiza el nodo al recibir el mensaje de reemplazo de tags
 * 
 * @author D. García
 */
public class RecibirReemplazarTagsWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	private ReemplazarTags mensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Reemplazamos los tags de los declarados por el receptor
		final List<String> nuevosTags = mensaje.getTags();
		final ReceptorVortex receptorAModificar = contexto.getEmisor();
		final TagSummarizer summarizer = contexto.getTagSummarizer();
		summarizer.reemplazarTagsPara(receptorAModificar, new HashSet<String>(nuevosTags));

		// Si hay cambios de tags globales, se disparará notificaciones a los receptores vecinos
	}

	public static RecibirReemplazarTagsWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final ReemplazarTags mensaje) {
		final RecibirReemplazarTagsWorkUnit agregar = new RecibirReemplazarTagsWorkUnit();
		agregar.contexto = contexto;
		agregar.mensaje = mensaje;
		return agregar;
	}

}
