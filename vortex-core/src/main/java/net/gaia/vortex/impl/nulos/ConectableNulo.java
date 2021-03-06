/**
 * 19/08/2013 20:42:35 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.nulos;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.impl.support.ComponenteSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Este conector envía todos los mensajes al {@link ReceptorNulo} para ser descartados
 * 
 * @author D. García
 */
public class ConectableNulo extends ComponenteSupport implements Conectable {
	private static final Logger LOG = LoggerFactory.getLogger(ConectableNulo.class);

	private static final WeakSingleton<ConectableNulo> ultimaReferencia = new WeakSingleton<ConectableNulo>(
			DefaultInstantiator.create(ConectableNulo.class));

	public static ConectableNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Conector#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		LOG.debug("Se intentó conectar el conector nulo al receptor: {}", destino);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Conector#desconectar()
	 */
	public void desconectar() {
		LOG.debug("Se intentó desconectar el conector nulo");
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		LOG.debug("Se intentó desconectar el conector nulo de: {}", destino);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Conector#getDestino()
	 */
	public Receptor getDestino() {
		return ReceptorNulo.getInstancia();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		// No tengo herencia multiple, tengo que usar metodos esstaticos compartidos
		return WeakSingletonSupport.singletonEquals(obj, this);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// No tengo herencia multiple, tengo que usar metodos esstaticos compartidos
		return WeakSingletonSupport.singletonHashFor(this);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados() {
		// No estamos conectados a nada
		return Collections.emptyList();
	}
}
