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
package ar.com.dgarcia.lang.metrics;

/**
 * Esta interfaz representa el contrato de métodos que debe tener un listener para calcular las
 * métricas de un sistema como una contabilización de entradas y salidas correspondientes
 * 
 * @author D. García
 */
public interface ListenerDeMetricas {

	/**
	 * Registra en esta métrica la recepción de un input en el sistema<br>
	 * Normalmente debería haber un output por cada input, para que los datos de delivery sean
	 * consistentes
	 */
	void registrarInput();

	/**
	 * Registra en esta métrica la cantidad de unidades ingresadas en el sistema como conjunto.
	 * 
	 * @param cantidad
	 *            La cantidad a contabilizad como entrantes del sistema medido
	 */
	void registrarInput(long cantidadIngresada);

	/**
	 * Registra en esta métrica generación de un output en el sistema.<br>
	 * Normalmente debería haber un output por cada input, para que los datos de delivery sean
	 * consistentes
	 */
	void registrarOutput();

	/**
	 * Registra en esta métrica la cantidad de unidades egresadas como conjunto.<br>
	 * 
	 * @param cantidadEgresada
	 *            La cantidad a contabilizar como saliente del sistema medido
	 */
	void registrarOutput(long cantidadEgresada);

}
