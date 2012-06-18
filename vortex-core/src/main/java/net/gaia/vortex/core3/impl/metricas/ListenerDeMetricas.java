/**
 * 27/05/2012 12:19:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.metricas;

/**
 * Esta interfaz representa el contrato de métodos que debe tener un listener para calcular las
 * métricas del nodo
 * 
 * @author D. García
 */
public interface ListenerDeMetricas {

	/**
	 * Registra en esta métrica una recepción de mensaje
	 */
	void registrarRecepcion();

	/**
	 * Registra en esta métrica un ruteo realizado por el nodo
	 */
	void registrarRuteo();

}
