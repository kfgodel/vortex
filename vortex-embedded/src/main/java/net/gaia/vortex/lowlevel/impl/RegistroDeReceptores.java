/**
 * 27/11/2011 22:17:24 Copyright (C) 2011 Darío L. García
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

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Esta clase representa el registro interno que un nodo tiene de sus receptores.<br>
 * A través de este registro el nodo puede saber qué nodos tiene interesados en un tag o no. Y como
 * se desempeña cada receptor con respecto a un tag en particular de manera de ajustar pesos para
 * elegir correctamente los receptores para el próximo envío
 * 
 * @author D. García
 */
public class RegistroDeReceptores {

	/**
	 * La lista de los receptores existentes en este nodo
	 */
	private ConcurrentLinkedQueue<ReceptorVortexConSesion> receptores;

	public static RegistroDeReceptores create() {
		final RegistroDeReceptores registro = new RegistroDeReceptores();
		registro.receptores = new ConcurrentLinkedQueue<ReceptorVortexConSesion>();
		return registro;
	}

	/**
	 * Agrega el receptor pasado, permitiendo que sea visible al nodo como tal
	 * 
	 * @param nuevoReceptor
	 *            El receptor que comenzará a recibir mensajes
	 */
	public void agregar(final ReceptorVortexConSesion nuevoReceptor) {
		this.receptores.add(nuevoReceptor);
	}

	/**
	 * Devuelve la lista de receptores que declararon interés en recibir mensajes con alguno de los
	 * tags pasados
	 * 
	 * @param tagsDelMensaje
	 *            Tags del mensaje a rutear
	 * @return Los receptores que tienen al menos uno de los tags declarados como recibibles
	 */
	public SeleccionDeReceptores getReceptoresInteresadosEn(final List<String> tagsDelMensaje) {
		final SeleccionDeReceptores nuevaSeleccion = SeleccionDeReceptores.create();

		final Iterator<ReceptorVortexConSesion> iterator = this.receptores.iterator();
		while (iterator.hasNext()) {
			final ReceptorVortexConSesion receptor = iterator.next();
			if (receptor.estaInteresadoEnCualquieraDe(tagsDelMensaje)) {
				nuevaSeleccion.incluir(receptor);
			} else {
				nuevaSeleccion.excluir(receptor);
			}
		}
		return nuevaSeleccion;
	}

	/**
	 * Ajusta los pesos de los tags de cada receptor de acuerdo a los resultados de performance
	 * indicados
	 * 
	 * @param reportePerformance
	 *            El resultado de un ruteo
	 */
	public void ajustarPesosDeAcuerdoA(final ReportePerformanceRuteo reportePerformance) {
		// TODO Auto-generated method stub

	}

}
