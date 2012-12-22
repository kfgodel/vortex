/**
 * 22/12/2012 18:59:31 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.listeners.IgnorarCambioDeFiltro;
import net.gaia.vortex.router.impl.listeners.IgnorarRuteos;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

/**
 * Esta clase base definir comportamiento comun de los nodos bidireccionales
 * 
 * @author D. García
 */
public class NodoBidi extends ComponenteConProcesadorSupport implements NodoBidireccional {

	private List<PataBidireccional> patas;

	private AtomicReference<ListenerDeCambiosDeFiltro> listenerDeFiltros;
	private AtomicReference<ListenerDeRuteo> listenerDeRuteo;

	public List<PataBidireccional> getPatas() {
		return patas;
	}

	public void setPatas(final List<PataBidireccional> patas) {
		this.patas = patas;
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		patas = new CopyOnWriteArrayList<PataBidireccional>();
		listenerDeFiltros = new AtomicReference<ListenerDeCambiosDeFiltro>(IgnorarCambioDeFiltro.getInstancia());
		listenerDeRuteo = new AtomicReference<ListenerDeRuteo>(IgnorarRuteos.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.router.api.moleculas.NodoBidireccional#setListenerDeFiltrosRemotos(net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro)
	 */
	@Override
	public void setListenerDeFiltrosRemotos(final ListenerDeCambiosDeFiltro listenerDeFiltros) {
		if (listenerDeFiltros == null) {
			throw new IllegalArgumentException("El listener de filtros no puede ser null. A lo sumo una instancia de "
					+ IgnorarCambioDeFiltro.class);
		}
		this.listenerDeFiltros.set(listenerDeFiltros);
	}

	/**
	 * @see net.gaia.vortex.router.api.moleculas.NodoBidireccional#setListenerDeRuteos(net.gaia.vortex.router.api.listeners.ListenerDeRuteo)
	 */
	@Override
	public void setListenerDeRuteos(final ListenerDeRuteo listenerDeRuteos) {
		if (listenerDeRuteos == null) {
			throw new IllegalArgumentException("El listener de ruteo no puede ser null. A lo sumo una instancia de "
					+ IgnorarCambioDeFiltro.class);
		}
		this.listenerDeRuteo.set(listenerDeRuteos);
	}
}
