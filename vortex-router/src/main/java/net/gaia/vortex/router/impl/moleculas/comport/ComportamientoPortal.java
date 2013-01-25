/**
 * 22/01/2013 16:26:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.comport;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.moleculas.FlujoVortex;
import net.gaia.vortex.core.impl.atomos.condicional.NexoBifurcador;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutable;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.router.impl.atomos.MultiplexorDePatas;
import net.gaia.vortex.router.impl.condiciones.EsMetaMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comportamiento bidi que tiene el portal
 * 
 * @author D. García
 */
public class ComportamientoPortal implements ComportamientoBidi {

	private Portal portalInterno;
	public static final String portalInterno_FIELD = "portalInterno";

	public static ComportamientoPortal create() {
		final ComportamientoPortal comportamiento = new ComportamientoPortal();
		return comportamiento;
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.comport.ComportamientoBidi#crearFlujoParaMensajesRecibidos(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	public FlujoVortex crearFlujoParaMensajesRecibidos(final TaskProcessor processor) {
		// Los metamensajes se los mandamos directo a las patas
		final Multiplexor multiplexorDePatas = MultiplexorDePatas.create(processor);

		// El portal interno recibe los mensaje normales, y envía a las patas
		portalInterno = PortalMapeador.createForOutputWith(processor, multiplexorDePatas);

		// Si es metamensaje va directo a las patas, si es mensaje normal al portal interno
		final Receptor bifurcador = NexoBifurcador.create(processor, EsMetaMensaje.create(), multiplexorDePatas,
				portalInterno);

		// Y antes que nada filtrar los mensajes duplicados que ya recibimos
		final NexoSinDuplicados filtroDeDuplicados = NexoSinDuplicados.create(processor, bifurcador);

		// En la entrada van los mensajes, en la salida se conectan las patas
		final FlujoVortex flujoDeMensajes = FlujoInmutable.create(filtroDeDuplicados, multiplexorDePatas);
		return flujoDeMensajes;
	}

	public Portal getPortalInterno() {
		return portalInterno;
	}

	public void setPortalInterno(final Portal portalInterno) {
		this.portalInterno = portalInterno;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(portalInterno_FIELD, portalInterno).toString();
	}

}
