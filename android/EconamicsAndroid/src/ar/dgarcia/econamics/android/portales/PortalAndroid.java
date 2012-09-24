/**
 * 24/09/2012 13:08:40 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.econamics.android.portales;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.VortexRoot;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta clase permite obtener un portal por composición ya conectado al nodo central para reducir el
 * código necesario para crear uno
 * 
 * @author D. García
 */
public class PortalAndroid implements Disposable {

	private Portal portalVortex;

	public static PortalAndroid create() {
		PortalAndroid portalAndroid = new PortalAndroid();
		portalAndroid.crearPortalVortex();
		return portalAndroid;
	}

	/**
	 * Crea el portal para utilizar en los activities
	 */
	private void crearPortalVortex() {
		TaskProcessor procesadorCentral = VortexRoot.getProcessor();
		Nodo nodoCentral = VortexRoot.getNode();
		portalVortex = PortalMapeador.createForIOWith(procesadorCentral, nodoCentral);
	}

	public Portal getPortalVortex() {
		return portalVortex;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose() {
		Nodo nodoCentral = VortexRoot.getNode();
		portalVortex.desconectarDe(nodoCentral);
		nodoCentral.desconectarDe(portalVortex);
	}
}
