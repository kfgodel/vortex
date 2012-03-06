/**
 * 28/11/2011 14:08:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.nodo;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo;
import net.gaia.vortex.lowlevel.impl.ruteo.flooding.OptimizadorFlooding;

/**
 * Esta clase representa la info de configuración de un nodo vortex
 * 
 * @author D. García
 */
public class ConfiguracionDeNodo {

	private TimeMagnitude timeoutDeAcuseDeConsumo;
	private TimeMagnitude esperaPorAcuseDeConsumo;
	private OptimizadorDeRuteo optimizador;

	public static ConfiguracionDeNodo create(final int segundosEsperandoAcuseDeConsumo,
			final int segundosAgregadosPorEsperaDeAcuse) {
		final ConfiguracionDeNodo configuracion = new ConfiguracionDeNodo();
		configuracion.timeoutDeAcuseDeConsumo = TimeMagnitude.of(segundosEsperandoAcuseDeConsumo, TimeUnit.SECONDS);
		configuracion.esperaPorAcuseDeConsumo = TimeMagnitude.of(segundosAgregadosPorEsperaDeAcuse, TimeUnit.SECONDS);
		configuracion.optimizador = OptimizadorFlooding.create();
		return configuracion;
	}

	public OptimizadorDeRuteo getOptimizador() {
		return optimizador;
	}

	public void setOptimizador(final OptimizadorDeRuteo optimiziador) {
		this.optimizador = optimiziador;
	}

	public TimeMagnitude getTimeoutDeAcuseDeConsumo() {
		return timeoutDeAcuseDeConsumo;
	}

	public void setTimeoutDeConfirmacionConsumo(final TimeMagnitude timeoutDeConfirmacionConsumo) {
		this.timeoutDeAcuseDeConsumo = timeoutDeConfirmacionConsumo;
	}

	public TimeMagnitude getEsperaPorAcuseDeConsumo() {
		return esperaPorAcuseDeConsumo;
	}

	public void setEsperaporAcuseDeConsumo(final TimeMagnitude esperaporAcuseDeConsumo) {
		this.esperaPorAcuseDeConsumo = esperaporAcuseDeConsumo;
	}

	public void setTimeoutDeAcuseDeConsumo(final TimeMagnitude timeoutDeAcuseDeConsumo) {
		this.timeoutDeAcuseDeConsumo = timeoutDeAcuseDeConsumo;
	}

	/**
	 * Crea una configuración default para un nodo en memoria
	 * 
	 * @return
	 */
	public static ConfiguracionDeNodo createEnMemoria() {
		return create(2, 2);
	}

	/**
	 * Crea una configuración default para un nodo en memoria
	 * 
	 * @return
	 */
	public static ConfiguracionDeNodo createEnHttp() {
		return create(10, 60);
	}

	/**
	 * Devuelve la cantidad de tiempo que se debe esperar como mínimo para considerar como perdido
	 * un consumo que no llegó.<br>
	 * Esta espera es el resultado de la espera por consumo + el timeout por solicitud de consumo no
	 * contestada.<br>
	 * Normalmente este valor se usa con un margen extra
	 * 
	 * @return El tiempo de espera mínimo en el que se puede pensar que el consumo no llegará desde
	 *         un nodo
	 */
	public TimeMagnitude getEsperaMinimaDeConsumoNoIndicado() {
		final TimeMagnitude esperaMinima = this.getEsperaPorAcuseDeConsumo().plus(this.getTimeoutDeAcuseDeConsumo());
		return esperaMinima;
	}

}
