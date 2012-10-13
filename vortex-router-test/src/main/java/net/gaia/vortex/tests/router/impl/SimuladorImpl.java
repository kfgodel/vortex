/**
 * 13/10/2012 11:04:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.tests.router.PasoSimulacion;
import net.gaia.vortex.tests.router.Simulador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.SystemChronometer;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase implementa el simulador
 * 
 * @author D. García
 */
public class SimuladorImpl implements Simulador {
	private static final Logger LOG = LoggerFactory.getLogger(SimuladorImpl.class);

	private List<PasoSimulacion> proximos;
	private List<PasoSimulacion> previos;
	private int contadorDePasos;

	public List<PasoSimulacion> getProximos() {
		if (proximos == null) {
			proximos = new ArrayList<PasoSimulacion>();
		}
		return proximos;
	}

	public void setProximos(final List<PasoSimulacion> proximos) {
		this.proximos = proximos;
	}

	public List<PasoSimulacion> getPrevios() {
		if (previos == null) {
			previos = new ArrayList<PasoSimulacion>();
		}
		return previos;
	}

	public void setPrevios(final List<PasoSimulacion> previos) {
		this.previos = previos;
	}

	public static SimuladorImpl create() {
		final SimuladorImpl simulador = new SimuladorImpl();
		return simulador;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Simulador#agregar(net.gaia.vortex.tests.router.impl.pasos.ConectarUni)
	 */
	@Override
	public void agregar(final PasoSimulacion nuevoPaso) {
		getProximos().add(nuevoPaso);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Simulador#getCantidadDePasosPendientes()
	 */
	@Override
	public int getCantidadDePasosPendientes() {
		return getProximos().size();
	}

	/**
	 * @see net.gaia.vortex.tests.router.Simulador#ejecutarSiguiente()
	 */
	@Override
	public <T extends PasoSimulacion> T ejecutarSiguiente() {
		if (getProximos().isEmpty()) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final T proximo = (T) getProximos().remove(0);

		if (contadorDePasos == 0) {
			// El primer paso
			LOG.info(" -- Comenzando nueva simulación -- ");
		}
		LOG.info("{}: {}", contadorDePasos, proximo);
		contadorDePasos++;
		proximo.ejecutar();
		return proximo;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Simulador#ejecutarTodos(ar.com.dgarcia.lang.time.TimeMagnitude)
	 */
	@Override
	public void ejecutarTodos(final TimeMagnitude esperaMaxima) throws TimeoutExceededException {
		final SystemChronometer cronometro = SystemChronometer.create();
		// Mientras no superemos la espera máxima
		while (cronometro.getElapsedMillis() <= esperaMaxima.getMillis()) {
			if (getProximos().isEmpty()) {
				// Terminamos!
				return;
			}
			ejecutarSiguiente();
		}
		final int pasosPendientes = getProximos().size();
		if (pasosPendientes > 0) {
			throw new TimeoutExceededException("Se acabó el tiempo[" + esperaMaxima
					+ "] y aún quedan pasos para ejecutar[" + pasosPendientes + "]");
		}
	}

}
