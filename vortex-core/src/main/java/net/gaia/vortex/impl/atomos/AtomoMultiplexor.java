/**
 * 20/08/2013 01:26:15 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import java.util.List;

import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.support.MultiConectableSupport;

/**
 * Esta clase representa el atomo básico que permite multiplexar los mensajes recibidos enviándolos
 * a un conjunto de receptores
 * 
 * @author D. García
 */
public class AtomoMultiplexor extends MultiConectableSupport implements Multiplexor {

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		final List<Conector> allConectores = getConectores();
		for (final Conector conector : allConectores) {
			conector.recibir(mensaje);
		}
	}

	public static AtomoMultiplexor create() {
		final AtomoMultiplexor atomo = new AtomoMultiplexor();
		atomo.inicializar();
		return atomo;
	}
}
