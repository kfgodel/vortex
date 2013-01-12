/**
 * 06/07/2012 01:34:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorSupport;
import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la acción realizada por el nodo multiplexor al registrar nuevos mensajes
 * como input
 * 
 * @author D. García
 */
final class RegistrarInputEnMetricas extends ReceptorSupport {

	private ListenerDeMetricas listener;

	@Override
	public void recibir(final MensajeVortex mensaje) {
		listener.registrarInput();
	}

	public static RegistrarInputEnMetricas con(final ListenerDeMetricas listener) {
		final RegistrarInputEnMetricas registrar = new RegistrarInputEnMetricas();
		registrar.listener = listener;
		return registrar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(numeroDeComponente_FIELD, getNumeroDeInstancia()).toString();
	}

}