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
package net.gaia.vortex.tests.router2.simulador;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
	 * @see net.gaia.vortex.tests.router2.simulador.Simulador#agregar(net.gaia.vortex.tests.router.impl.pasos.ConectarUni)
	 */
	@Override
	public void agregar(final PasoSimulacion nuevoPaso) {
		getProximos().add(nuevoPaso);
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.Simulador#getCantidadDePasosPendientes()
	 */
	@Override
	public int getCantidadDePasosPendientes() {
		return getProximos().size();
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.Simulador#ejecutarSiguiente()
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
	 * @see net.gaia.vortex.tests.router2.simulador.Simulador#ejecutarTodos(ar.com.dgarcia.lang.time.TimeMagnitude)
	 */
	@Override
	public void ejecutarTodos(final TimeMagnitude esperaMaxima) throws TimeoutExceededException {
		ejecutarConLimites(esperaMaxima, Integer.MAX_VALUE);
	}

	/**
	 * Ejecuta los pasos pendientes en este simulador con los limites indicados.<br>
	 * Si se acaba el tiempo se produce una excepción, si se acaban los pasos se termina normalmente
	 * y quedarán pasos pendientes
	 * 
	 * @param esperaMaxima
	 *            El tiempo máximo que puede usarse para la ejecucion
	 * @param pasosRestantes
	 *            La cantidad de pasos máxima a ejecutar
	 */
	private void ejecutarConLimites(final TimeMagnitude esperaMaxima, int pasosRestantes) {
		final SystemChronometer cronometro = SystemChronometer.create();

		// Mientras no superemos la espera máxima, o los pasos maximos
		while (cronometro.getElapsedMillis() <= esperaMaxima.getMillis() && pasosRestantes > 0) {
			if (getProximos().isEmpty()) {
				// Terminamos!
				return;
			}
			ejecutarSiguiente();
			pasosRestantes--;
		}
		if (pasosRestantes == 0) {
			// Terminamos de ejecutar los pasos que nos pidieron
			return;
		}
		final int pasosPendientes = getProximos().size();
		if (pasosPendientes > 0) {
			throw new TimeoutExceededException("Se acabó el tiempo[" + esperaMaxima
					+ "] y aún quedan pasos para ejecutar[" + pasosPendientes + "]");
		}
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.Simulador#ejecutarPasos(int)
	 */
	@Override
	public void ejecutarPasos(final int cantidadDePasosMaxima) {
		ejecutarConLimites(TimeMagnitude.of(10, TimeUnit.MINUTES), cantidadDePasosMaxima);
	}
}
