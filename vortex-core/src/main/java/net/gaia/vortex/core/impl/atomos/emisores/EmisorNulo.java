/**
 * 13/01/2013 19:40:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.emisores;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.impl.atomos.support.EmisorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el emisor nulo que es utilizable para evitar null donde se requiere un
 * emisor inicialmente
 * 
 * @author D. García
 */
public class EmisorNulo extends EmisorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(EmisorNulo.class);

	private static final WeakSingleton<EmisorNulo> ultimaReferencia = new WeakSingleton<EmisorNulo>(
			DefaultInstantiator.create(EmisorNulo.class));

	public static EmisorNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		LOG.trace("Se intento conectar el destino[{}] en el emisor nulo", destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		LOG.trace("Se intento desconectar el destino[{}] en el emisor nulo", destino);
	}

}
