/**
 * 13/06/2012 01:46:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos.ejecutor;

import net.gaia.vortex.core2.api.annon.Atomo;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.impl.atomos.ProxySupport;

/**
 * Esta clase representa un {@link ComponenteProxy} que ejecuta el código de otro
 * {@link ComponenteVortex} al recibir un mensaje
 * 
 * @author D. García
 */
@Atomo
public class ProxyEjecutor extends ProxySupport {

	public static ProxyEjecutor create() {
		final ProxyEjecutor ejecutor = new ProxyEjecutor();
		return ejecutor;
	}
}
