/**
 * 18/01/2012 21:52:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.ArrayList;
import java.util.List;

import ar.com.dgarcia.coding.caching.Instantiator;
import ar.com.dgarcia.coding.caching.SimpleCacheConcurrentMap;

/**
 * Esta clase representa un reporte de notificaciones de cambios de tag en el nodo.<br>
 * A partir de este reporte se puede notificar a los clientes del nodo, de los cambios de tag que le
 * incumben a cada uno.
 * 
 * @author D. García
 */
public class ReporteCambioDeTags {

	private SimpleCacheConcurrentMap<ReceptorVortex, NotificacionDeCambioDeTags> notificacionesPorReceptor;

	public static ReporteCambioDeTags create() {
		final ReporteCambioDeTags reporte = new ReporteCambioDeTags();
		reporte.notificacionesPorReceptor = new SimpleCacheConcurrentMap<ReceptorVortex, NotificacionDeCambioDeTags>(
				new Instantiator<ReceptorVortex, NotificacionDeCambioDeTags>() {
					@Override
					public NotificacionDeCambioDeTags instantiate(final ReceptorVortex receptor) {
						return NotificacionDeCambioDeTags.create(receptor);
					}
				});
		return reporte;
	}

	/**
	 * Devuelve la lista de todas las notificaciones por receptor
	 * 
	 * @return las notificaciones recopiladas de cambios
	 */
	public List<NotificacionDeCambioDeTags> getNotificaciones() {
		final List<NotificacionDeCambioDeTags> notificaciones = new ArrayList<NotificacionDeCambioDeTags>(
				notificacionesPorReceptor.values());
		return notificaciones;
	}

	/**
	 * Indica si este reporte registra notificaciones para clientes del nodo
	 * 
	 * @return true si hay al menos un receptor a avisar
	 */
	public boolean tieneNotificaciones() {
		return !this.notificacionesPorReceptor.isEmpty();
	}

	/**
	 * Registra que se debe generar una notificación de tag agregado para el receptor indicado
	 * 
	 * @param receptorANotificar
	 *            El receptor a notificar
	 * @param tagAgregado
	 *            El tag agregado a notificar
	 */
	public void notificarTagIncorporadoAlNodoA(final ReceptorVortex receptorANotificar, final String tagAgregado) {
		final NotificacionDeCambioDeTags notificacion = notificacionesPorReceptor
				.getOrCreateIfNullFor(receptorANotificar);
		notificacion.agregarTag(tagAgregado);
	}

	/**
	 * Registra que se debe generar una notificación de tag quitado para el receptor indicado
	 * 
	 * @param receptorANotificar
	 *            El receptor a notificar del tag quitado
	 * @param tagQuitado
	 *            El tag que se quitó
	 */
	public void notificarTagEliminadoDelNodoA(final ReceptorVortex receptorANotificar, final String tagQuitado) {
		final NotificacionDeCambioDeTags notificacion = notificacionesPorReceptor
				.getOrCreateIfNullFor(receptorANotificar);
		notificacion.quitarTag(tagQuitado);

	}

	/**
	 * Devuelve la notificación registrada en este reporte para el receptor indicado
	 * 
	 * @param receptorNotificado
	 * @return La notificación registrada o null si no hay ninguna
	 */
	public NotificacionDeCambioDeTags getNotificacionPara(final ReceptorVortex receptorNotificado) {
		NotificacionDeCambioDeTags notificacionDelReceptor = notificacionesPorReceptor.get(receptorNotificado);
		return notificacionDelReceptor;
	}

}
