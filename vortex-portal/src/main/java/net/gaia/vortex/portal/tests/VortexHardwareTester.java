/**
 * Created on: Sep 13, 2013 9:09:28 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.portal.tests;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.support.HandlerTipado;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.FactoryDeRunnable;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase representa el test de hardware que evalua la capacidad de procesamiento de la máquina
 * donde es ejecutada, tomando una red muy simple y tipica de un programa en memoria para las
 * mediciones
 * 
 * @author dgarcia
 */
public class VortexHardwareTester {

	private VortexPortal builder;

	public static VortexHardwareTester create(final VortexPortal builder) {
		final VortexHardwareTester name = new VortexHardwareTester();
		name.builder = builder;
		return name;
	}

	/**
	 * Realiza 2 tests midiendo primero cuantos mensajes por segundo se pueden procesar utilizando
	 * un solo thread, y luego usando tantos threads como cores alla disponibles
	 * 
	 * @return
	 */
	public List<Long> medirPerformanceEnMensajesPorMilisegundo() {
		final List<Long> mediciones = new ArrayList<Long>(2);
		mediciones.add(medirConUnThread());
		mediciones.add(medirConTantosThreadsComoCores());
		return mediciones;
	}

	private Long medirConTantosThreadsComoCores() {
		final int procesadoresDisponibles = Runtime.getRuntime().availableProcessors();
		return testearParaThreads(procesadoresDisponibles);
	}

	private Long testearParaThreads(final int cantidadDeThreads) {
		final Compuesto<Portal, Portal> flujo = crearRedATestear();
		return testearRed(cantidadDeThreads, flujo);
	}

	private Long testearRed(final int cantidadDeThreads, final Compuesto<Portal, Portal> flujo) {
		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		// Generamos tantos portales como receptores tengamos
		flujo.getSalida().recibirCon(new HandlerTipado<ObjetoDePruebas>(SiempreTrue.getInstancia()) {
			public void onObjetoRecibido(final ObjetoDePruebas mensaje) {
				metricas.registrarOutput();
			}
		});

		return correrThreadsEmisores(cantidadDeThreads, metricas, flujo);
	}

	private Long correrThreadsEmisores(final int cantidadDeThreads, final MetricasPorTiempoImpl metricas,
			final Compuesto<Portal, Portal> flujo) {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(cantidadDeThreads);

		// Por cada ejecucion genera el mensaje y lo manda por algunos de los sockets de salida
		stress.setFactoryDeRunnable(new FactoryDeRunnable() {
			public Runnable getOrCreateRunnable() {
				return new Runnable() {
					public void run() {
						final ObjetoDePruebas objeto = ObjetoDePruebas.create(0);
						metricas.registrarInput();
						flujo.getEntrada().enviar(objeto);
					}
				};
			}
		});

		return correrYMostrarResultados(stress, metricas);
	}

	private Long correrYMostrarResultados(final StressGenerator stress, final MetricasPorTiempoImpl metricas) {
		// Comenzamos el test
		metricas.resetear();
		stress.start();

		// Medimos durante un tiempo
		try {
			Thread.sleep(10000);
		}
		catch (final InterruptedException e) {
			throw new UnhandledConditionException("Se interrumpió la medición del hardware");
		}
		// Freezamos la medición
		final SnapshotDeMetricaPorTiempo medicion = SnapshotDeMetricaPorTiempo.createFrom(metricas);
		// Detenemos el stress
		stress.detenerThreads();

		// Mostramos los resultados
		final long cantidadDeOutputs = medicion.getCantidadDeOutputs();
		final long millisTranscurridos = medicion.getDuracionDeMedicionEnMilis();
		final long mensajesPorSegundo = cantidadDeOutputs / millisTranscurridos;
		return mensajesPorSegundo;
	}

	private Long medirConUnThread() {
		return testearParaThreads(1);
	}

	/**
	 * Crea un nodo compuesto con la entrada y salida que debemos testear
	 */
	protected Compuesto<Portal, Portal> crearRedATestear() {
		final Portal portalEntrada = builder.portalConversor();
		final Portal portalSalida = builder.portalConversor();

		final Distribuidor distribuidorEnElMedio = builder.getCore().distribuidor();
		final Terminal terminalParaEntrada = distribuidorEnElMedio.crearTerminal();
		portalEntrada.conectarCon(terminalParaEntrada);
		terminalParaEntrada.conectarCon(portalEntrada);

		final Terminal terminalDeSalida = distribuidorEnElMedio.crearTerminal();
		terminalDeSalida.conectarCon(portalSalida);
		portalSalida.conectarCon(terminalDeSalida);

		final Compuesto<Portal, Portal> compuesto = builder.getCore().componer(portalEntrada, portalSalida);
		return compuesto;
	}

}
