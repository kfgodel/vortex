/**
 * 17/06/2012 16:16:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.moleculas.ruteo;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core3.api.annon.Molecula;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.atomos.condicional.Bifurcador;
import net.gaia.vortex.core3.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core3.api.atomos.forward.Nexo;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.moleculas.ruteo.NodoHubConNexoCore;
import net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core3.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core3.impl.atomos.ReceptorVariable;
import net.gaia.vortex.core3.impl.atomos.condicional.NexoBifurcador;
import net.gaia.vortex.core3.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core3.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core3.impl.atomos.forward.NexoNulo;
import net.gaia.vortex.core3.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core3.impl.condiciones.RemitenteDistinto;
import net.gaia.vortex.core3.impl.condiciones.TieneRemitente;
import net.gaia.vortex.core3.impl.transformaciones.AsignarComoRemitente;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa un hub que utiliza un nexo como lógica interna para permitir agregar
 * comportamiento adicional al recibir los mensajes
 * 
 * @author D. García
 */
@Molecula
public class HubConNexo extends ComponenteConProcesadorSupport implements NodoHubConNexoCore {
	private static final Logger LOG = LoggerFactory.getLogger(HubConNexo.class);

	private Bifurcador procesoDeEntrada;
	private Multiplexor multiplexorDeSalidas;
	private ConcurrentMap<Receptor, NexoFiltro> filtrosPorSalida;
	private ReceptorVariable<Nexo> nexoCore;

	/**
	 * @see net.gaia.vortex.core3.api.moleculas.ruteo.NodoHubConNexoCore#getNexoCore()
	 */
	@Override
	public Nexo getNexoCore() {
		return this.nexoCore.getReceptorActual();
	}

	/**
	 * @see net.gaia.vortex.core3.api.moleculas.ruteo.NodoHubConNexoCore#setNexoCore(net.gaia.vortex.core3.api.atomos.forward.Nexo)
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
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#conectarCon(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		final RemitenteDistinto entregaSoloSiVieneDeOtroDestino = RemitenteDistinto.de(destino);
		final NexoFiltro nuevaSalida = NexoFiltro.create(getProcessor(), entregaSoloSiVieneDeOtroDestino, destino);
		filtrosPorSalida.put(destino, nuevaSalida);
		multiplexorDeSalidas.conectarCon(nuevaSalida);
	}

	/**
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core3.api.atomos.Receptor)
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
	 * @see net.gaia.vortex.core3.api.atomos.Receptor#recibir(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		procesoDeEntrada.recibir(mensaje);
	}

	/**
	 * Crea un hub cuyo nexo sólo envía los mensajes que recibe (un hub simple)
	 * 
	 * @param processor
	 *            El procesador de las tareas internas de este componente
	 * @return El hub creado
	 */
	public static HubConNexo create(final TaskProcessor processor) {
		final HubConNexo hubConNexo = new HubConNexo();
		hubConNexo.initializeWith(processor);
		return hubConNexo;
	}

	/**
	 * Crea un hub con el nexo pasado como core para operar sobre los mensajes recibidos
	 * 
	 * @param processor
	 *            El procesador de las tareas de este componente
	 * @param nexoCore
	 *            El nexo que define el core del comportamiento de este hub
	 * @return El hub creado
	 */
	public static HubConNexo create(final TaskProcessor processor, final Nexo nexoCore) {
		final HubConNexo hub = new HubConNexo();
		hub.initializeWith(processor);
		hub.setNexoCore(nexoCore);
		return hub;
	}

	/**
	 * @param nexoCore2
	 * @see net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		// Empezamos desde las salidas (por las referencias entre instancias)
		filtrosPorSalida = new ConcurrentHashMap<Receptor, NexoFiltro>();
		multiplexorDeSalidas = MultiplexorParalelo.create(processor);
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
	 * Devuelve el conjunto de los receptores que están conectados a este hub
	 * 
	 * @return El conjunto de receptores para los mensajes que recibe este hub
	 */
	public Collection<Receptor> getDestinos() {
		return filtrosPorSalida.keySet();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("destinos", getDestinos().size()).toString();
	}

}
