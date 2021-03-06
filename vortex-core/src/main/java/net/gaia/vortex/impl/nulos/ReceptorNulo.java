/**
 * 13/06/2012 00:48:48 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente nulo que no realiza acción al recibir un mensaje.<br>
 * Este objeto permite no utilizar null
 * 
 * @author D. García
 */
@Atomo
public class ReceptorNulo extends ReceptorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ReceptorNulo.class);

	private static final WeakSingleton<ReceptorNulo> ultimaReferencia = new WeakSingleton<ReceptorNulo>(
			DefaultInstantiator.create(ReceptorNulo.class));

	public static ReceptorNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public void recibir(final MensajeVortex mensaje) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Se recibió un mensaje[{}] en el receptor nulo", mensaje);
		}
	}

	public static ReceptorNulo create() {
		final ReceptorNulo componente = new ReceptorNulo();
		return componente;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).toString();
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

}
