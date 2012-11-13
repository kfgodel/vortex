/**
 * 13/10/2012 11:03:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta interfaz representa el simulador como un objeto que contiene pasos que puede ejecutar paso a
 * paso o todos juntos
 * 
 * @author D. García
 */
public interface Simulador {

	/**
	 * Agrega un nuevo paso pendiente a este simulador
	 * 
	 * @param nuevoPaso
	 *            El paso a ejecutar
	 */
	void agregar(PasoSimulacion nuevoPaso);

	/**
	 * Devuelve la cantidad de pasos que faltan para terminar la simulacion en el estado actual
	 * 
	 * @return La cantidad de pasos que puede variar al ejecutar pasos nuevos
	 */
	int getCantidadDePasosPendientes();

	/**
	 * Ejecuta el proximo paso de ejecucion disponible y lo devuelve.<br>
	 * Si no hay mas pasos devuelve null
	 */
	<T extends PasoSimulacion> T ejecutarSiguiente();

	/**
	 * Ejecuta todos los pasos siguientes hasta que no queden más como pendientes.<br>
	 * Puede bloquearse si existen pasos recursivos. Para lo que se estipula un limite de tiempo en
	 * la ejecución.<br>
	 * Si al terminar el tiempo permitido todavía quedan pasos se lanza una excepción
	 */
	void ejecutarTodos(TimeMagnitude esperaMaxima) throws TimeoutExceededException;

	/**
	 * Ejecuta una cierta cantidad de pasos, sin importar que queden pendientes.<br>
	 * Se ejecutaran a lo suma la cantidad indicada, menos si no quedan pasos
	 * 
	 * @param cantidadDePasosMaxima
	 *            La cantidad máxima de pasos a ejecutar
	 */
	void ejecutarPasos(int cantidadDePasosMaxima);

}
