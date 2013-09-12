/**
 * 20/08/2013 01:00:09 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos.productores;

import net.gaia.vortex.api.basic.productores.Productor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.MonoEmisorSupport;

/**
 * Esta clase representa el átomo que permite introducir nuevos mensajes bien formados a la red
 * vortex
 * 
 * @author D. García
 */
public class AtomoProductor extends MonoEmisorSupport implements Productor {

	/**
	 * Toma el mensaje pasado y lo introduce en la red a través del conector de salida como si se
	 * estuviese produciendo en esta instancia
	 * 
	 * @param mensaje
	 *            El mensaje a producir
	 */
	public void producir(final MensajeVortex mensaje) {
		getConectado().recibir(mensaje);
	}

	public static AtomoProductor create() {
		final AtomoProductor productor = new AtomoProductor();
		productor.desconectar();
		return productor;
	}
}
