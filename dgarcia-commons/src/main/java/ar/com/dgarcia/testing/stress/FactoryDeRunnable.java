/**
 * 01/07/2012 12:08:54 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.testing.stress;

/**
 * Esta interfaz define el contrato para abstraer el lugar desde el cual se generan los runnables
 * 
 * @author D. García
 */
public interface FactoryDeRunnable {

	/**
	 * Devuelve el próximo runnable a ser utilizado
	 * 
	 * @return El runnable creado o reutilizado
	 */
	public Runnable getOrCreateRunnable();

}
