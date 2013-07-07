/**
 * 07/07/2013 17:02:36 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.api.time;

/**
 * Esta interfaz representa un reloj para medir los milis transcurridos
 * 
 * @author D. García
 */
public interface CronometroMilis {

	/**
	 * Devuelve la cantidad de milis que paso en la última vuelta, y comienza el registro de una
	 * nueva
	 * 
	 * @return La cantidad de milis transcurridos desde la ultima llamada a este método. La primera
	 *         vez será 0
	 */
	long getLastLapAndStartNew();

	/**
	 * Setea el tiempo actual como momento 0 de la ejecución global
	 */
	void reset();

	/**
	 * Detiene el conteo del tiempo para medir el tiempo total
	 */
	void stop();

	/**
	 * Devuelve la cantidad total de milis transcurridos entre el momento de reset() y el de stop()
	 * 
	 * @return El tiempo en milis transcurridos
	 */
	long getTotalMilis();

}
