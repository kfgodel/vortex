/**
 * 04/03/2012 21:57:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.lowlevel.impl.ruteo.jajo;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Esta clase representa el feedback generado por el {@link OptimizadorJaJo} para las decisiones de
 * ruteo
 * 
 * @author D. García
 */
public class FeedbackJajoDeRuteo {

	private ConcurrentHashMap<String, DecisionDeRuteo> decisiones;

	public static FeedbackJajoDeRuteo create() {
		final FeedbackJajoDeRuteo feedback = new FeedbackJajoDeRuteo();
		feedback.decisiones = new ConcurrentHashMap<String, DecisionDeRuteo>();
		return feedback;
	}

	/**
	 * Devuelve la decisión previamente tomada en este feedback para el tag indicado
	 * 
	 * @param tagDelMensaje
	 *            El tag del que se pide la decisión
	 * @return La decisión tomada en base al feedback o el valor de "no decisión" si no hay datos
	 *         previos
	 */
	public DecisionDeRuteo getDecisionPara(final String tagDelMensaje) {
		final DecisionDeRuteo decision = decisiones.get(tagDelMensaje);
		if (decision == null) {
			return DecisionDeRuteo.NO_DECIDIDO;
		}
		return decision;
	}

	/**
	 * Registra en este feedback la decisión tomada para los tags pasados
	 * 
	 * @param decision
	 *            La decisión tomada
	 * @param tagsDelMensaje
	 *            Los tag en los que aplica la decisión
	 */
	public void registrarDecision(final DecisionDeRuteo decision, final List<String> tagsDelMensaje) {
		for (final String tag : tagsDelMensaje) {
			final DecisionDeRuteo decisionPrevia = this.decisiones.putIfAbsent(tag, decision);
			// Si ya existía decision previa, solo la podemos reemplazar si no era de envio
			if (decisionPrevia != null && decisionPrevia.esReemplazable()) {
				this.decisiones.put(tag, decision);
			}
		}
	}

	/**
	 * Devuelve los tags en los que este nodo tenía la decisión de envío.<br>
	 * Esto tags puede perder la optimización al quitar el nodo
	 * 
	 * @return El conjunto de tags para el que el nodo tenía envio habilitado
	 */
	public Set<String> getTagsConDecisionDeEnvio() {
		final Set<String> tagsConEnvio = new HashSet<String>();
		final Set<Entry<String, DecisionDeRuteo>> entrySet = this.decisiones.entrySet();
		for (final Entry<String, DecisionDeRuteo> entry : entrySet) {
			final DecisionDeRuteo decision = entry.getValue();
			if (!DecisionDeRuteo.ENVIAR.equals(decision)) {
				// No es la de envio, seguimos
				continue;
			}
			final String tagConEnvio = entry.getKey();
			tagsConEnvio.add(tagConEnvio);
		}
		return tagsConEnvio;
	}

	/**
	 * Invalida las decisiones de los tags pasados para este nodo, permitiendo que se reoptimicen
	 * 
	 * @param tagsInvalidadosAlQuitar
	 */
	public void invalidarTags(final Set<String> tagsInvalidadosAlQuitar) {
		for (final String tagInvalidado : tagsInvalidadosAlQuitar) {
			final DecisionDeRuteo decisionPrevia = this.decisiones.get(tagInvalidado);
			if (decisionPrevia == null) {
				// No tenemos decision previa, no nos afecta
				continue;
			}
			if (!DecisionDeRuteo.NO_ENVIAR.equals(decisionPrevia)) {
				// La decision previa no era restrictiva, no hace falta cambiar nada
				continue;
			}
			// Quitamos el tag restrictivo para permitir mensajes salientes
			this.decisiones.remove(tagInvalidado);
		}
	}

}
