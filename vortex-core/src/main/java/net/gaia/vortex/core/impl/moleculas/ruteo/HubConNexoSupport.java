/**
 * 20/06/2012 19:11:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas.ruteo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.condicional.Bifurcador;
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.metricas.MetricasDelNodo;
import net.gaia.vortex.core.api.moleculas.ruteo.NodoHubConNexoCore;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.ReceptorVariable;
import net.gaia.vortex.core.impl.atomos.condicional.NexoBifurcador;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.forward.NexoNulo;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.RemitenteDistinto;
import net.gaia.vortex.core.impl.condiciones.TieneRemitente;
import net.gaia.vortex.core.impl.metricas.MetricasDelNodoImpl;
import net.gaia.vortex.core.impl.metricas.NodoConMetricas;
import net.gaia.vortex.core.impl.transformaciones.AsignarComoRemitente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación base de un hub con nexo interno a partir del cual otros pueden
 * definir comportamiento adicional
 * 
 * @author D. García
 */
public abstract class HubConNexoSupport extends ComponenteConProcesadorSupport implements NodoHubConNexoCore,
		NodoConMetricas {

	private static final Logger LOG = LoggerFactory.getLogger(HubConNexo.class);
	private Bifurcador procesoDeEntrada;
	private MultiplexorParalelo multiplexorDeSalidas;
	private ConcurrentMap<Receptor, NexoFiltro> filtrosPorSalida;
	private ReceptorVariable<Nexo> nexoCore;
	private MetricasDelNodoImpl metricas;

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ruteo.NodoHubConNexoCore#getNexoCore()
	 */
	@Override
	public Nexo getNexoCore() {
		return this.nexoCore.getReceptorActual();
	}

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ruteo.NodoHubConNexoCore#setNexoCore(net.gaia.vortex.core.api.atomos.forward.Nexo)
	 */
	@Override
	public void setNexoCore(final Nexo nuevoCore) {
		if (nuevoCore == null) {
			throw new IllegalArgumentException("El nexo del hub no puede ser null. A lo sumo puede ser un "
					+ NexoNulo.class);
		}
		final Nexo nexoPrevio = getNexoCore();

		// Conectamos primero las salidas al nuevo nexo, para no perder mensajes
		nuevoCore.setDestino(multiplexorDeSalidas);
		// Reemplazamos el core que recibirá las entradas
		nexoCore.setReceptorActual(nuevoCore);

		// Desconectamos el nexo previo de las salidas (si es otra instancia)
		if (nexoPrevio != nuevoCore) {
			nexoPrevio.setDestino(ReceptorNulo.getInstancia());
		}
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		final RemitenteDistinto entregaSoloSiVieneDeOtroDestino = RemitenteDistinto.de(destino);
		final NexoFiltro nuevaSalida = NexoFiltro.create(getProcessor(), entregaSoloSiVieneDeOtroDestino, destino);
		filtrosPorSalida.put(destino, nuevaSalida);
		multiplexorDeSalidas.conectarCon(nuevaSalida);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		final NexoFiltro salidaUsada = filtrosPorSalida.remove(destino);
		if (salidaUsada == null) {
			// Probablemente nos mandaron fruta!
			LOG.debug("Se intentó desconectar un destino[" + destino + "] para el que no teníamos conexión");
		}
		multiplexorDeSalidas.desconectarDe(salidaUsada);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		metricas.registrarRecepcion();
		procesoDeEntrada.recibir(mensaje);
	}

	/**
	 * @param nexoCore2
	 * @see net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		// Creamos el registro de métricas
		metricas = MetricasDelNodoImpl.create();

		// Empezamos desde las salidas (por las referencias entre instancias)
		filtrosPorSalida = new ConcurrentHashMap<Receptor, NexoFiltro>();
		multiplexorDeSalidas = MultiplexorParalelo.create(processor);
		// Hacemos que el multiplexor registre cuando termina de procesar
		multiplexorDeSalidas.setListenerMetricas(metricas);
		// Por defecto usamos un nexo nulo como core para pasar los mensajes al multiplexor
		nexoCore = ReceptorVariable.<Nexo> create(NexoNulo.create(processor, multiplexorDeSalidas));
		//
		final TieneRemitente mensajeConRemitente = TieneRemitente.getInstancia();
		final Receptor descartarMensajesPropios = NexoFiltro.create(processor, RemitenteDistinto.de(this), nexoCore);
		final Receptor asignarRemitente = NexoTransformador.create(processor, AsignarComoRemitente.a(this), nexoCore);
		procesoDeEntrada = NexoBifurcador.create(processor, mensajeConRemitente, descartarMensajesPropios,
				asignarRemitente);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		int cantidadDeDestinos = 0;
		if (filtrosPorSalida != null) {
			cantidadDeDestinos = filtrosPorSalida.size();
		}
		return ToString.de(this).add("destinos", cantidadDeDestinos).toString();
	}

	@Override
	public MetricasDelNodo getMetricas() {
		return metricas;
	}

}