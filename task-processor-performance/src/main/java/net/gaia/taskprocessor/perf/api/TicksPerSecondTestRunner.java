/**
 * 07/07/2013 14:50:34 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.api;


/**
 * Esta interfaz representa un runner de tests que mide cuantas veces se incrementó una variable por
 * segundo para determinar la performance del elemento testeado.<br>
 * 
 * @author D. García
 */
public interface TicksPerSecondTestRunner {

	/**
	 * Ejecuta el test pasado indefinidamente hasta que el usuario ingrese algo por consola.<br>
	 * La invocación de este método bloquea el thread actual hasta que el usuario ingrese algo que
	 * corta la ejecución y el test termina. El resultado de las invocaciones se muestra por
	 * pantalla
	 * 
	 * @param processorTest
	 *            El test a ejecutar indefinidamente
	 */
	void ejecutarIndefinidamente(TicksPerSecondTestUnit processorTest);

}
