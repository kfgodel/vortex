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

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.impl.support.ComponenteSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Este conector envía todos los mensajes al {@link ReceptorNulo} para ser descartados
 * 
 * @author D. García
 */
public class ConectorNulo extends ComponenteSupport implements Conector {
	private static final Logger LOG = LoggerFactory.getLogger(ConectorNulo.class);

	private static final WeakSingleton<ConectorNulo> ultimaReferencia = new WeakSingleton<ConectorNulo>(
			DefaultInstantiator.create(ConectorNulo.class));

	public static ConectorNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		LOG.debug("Se intentó conectar el conector nulo al receptor: {}", destino);
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#desconectar()
	 */
	public void desconectar() {
		LOG.debug("Se intentó desconectar el conector nulo");
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		ReceptorNulo.getInstancia().recibir(mensaje);
	}

}
