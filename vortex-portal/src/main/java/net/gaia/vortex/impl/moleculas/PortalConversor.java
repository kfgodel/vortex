/**
 * 04/09/2013 22:37:14 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.moleculas;

import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.impl.support.MonoConectableSupport;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;

/**
 * Esta clase implementa la forma más básica de portal que sólo convierte los objetos en mensaje, y
 * los mensajes en objetos.<br>
 * A diferencia de otros portales, este no discrimina mensajes propios o repetidos por lo que la
 * topología de la red no puede tener bucles
 * 
 * @author D. García
 */
public class PortalConversor extends MonoConectableSupport implements Portal {

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#recibirCon(net.gaia.vortex.portal.api.mensaje.HandlerDePortal)
	 */
	public void recibirCon(final HandlerDePortal<?> handlerDeMensajes) {
		// TODO Auto-generated method stub

	}

	public static PortalConversor create() {
		final PortalConversor portal = new PortalConversor();
		return portal;
	}
}
