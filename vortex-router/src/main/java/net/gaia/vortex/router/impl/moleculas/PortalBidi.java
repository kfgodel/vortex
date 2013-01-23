/**
 * 22/12/2012 18:56:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.router.api.moleculas.PortalBidireccional;
import net.gaia.vortex.router.impl.moleculas.comport.ComportamientoPortal;

/**
 * Esta clase representa el componente vortex que implementa el portal con comunicaciones
 * bidireccionales, lo que le permite comunicar sus filtros y optimizar el ruteo de mensajes
 * 
 * @author D. García
 */
public class PortalBidi extends NodoBidi implements PortalBidireccional {

	private static ComportamientoPortal comportamiento;

	public static PortalBidi create(final TaskProcessor processor) {
		final PortalBidi portal = new PortalBidi();
		comportamiento = ComportamientoPortal.create();
		portal.initializeWith(processor, comportamiento);
		return portal;
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	@Override
	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#recibirCon(net.gaia.vortex.portal.api.mensaje.HandlerDePortal)
	 */
	@Override
	public void recibirCon(final HandlerDePortal<?> handlerDeMensajes) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		// TODO Auto-generated method stub
		return null;
	}

}
