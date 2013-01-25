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

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.router.api.moleculas.PortalBidireccional;
import net.gaia.vortex.router.impl.filtros.ConjuntoSincronizado;
import net.gaia.vortex.router.impl.moleculas.comport.ComportamientoPortal;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el componente vortex que implementa el portal con comunicaciones
 * bidireccionales, lo que le permite comunicar sus filtros y optimizar el ruteo de mensajes
 * 
 * @author D. García
 */
public class PortalBidi extends NodoBidi implements PortalBidireccional {

	private Portal portalInterno;
	private List<Condicion> condicionesDelPortal;

	public static PortalBidi create(final TaskProcessor processor) {
		final PortalBidi portal = new PortalBidi();
		final ComportamientoPortal comportamiento = ComportamientoPortal.create();
		portal.condicionesDelPortal = new ArrayList<Condicion>();
		portal.initializeWith(processor, comportamiento);
		portal.portalInterno = comportamiento.getPortalInterno();
		return portal;
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	@Override
	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		portalInterno.enviar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#recibirCon(net.gaia.vortex.portal.api.mensaje.HandlerDePortal)
	 */
	@Override
	public void recibirCon(final HandlerDePortal<?> handlerDeMensajes) {
		// Registramos la condición para poder unificarlas
		final Condicion nuevaCondicion = handlerDeMensajes.getCondicionSuficiente();
		condicionesDelPortal.add(nuevaCondicion);

		// Le pasamos el handler al portal real
		portalInterno.recibirCon(handlerDeMensajes);

		// Notificamos del cambio interno
		evento_cambioEstadoFiltrosLocales();
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor nuevoDestino) {
		final Receptor destinoAnterior = getDestino();
		if (destinoAnterior.equals(nuevoDestino)) {
			// Ya estamos conectados
			return;
		}
		desconectarDe(destinoAnterior);
		conectarCon(nuevoDestino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		final List<Receptor> allDestinos = getDestinos();
		if (allDestinos.isEmpty()) {
			// No estamos conectados a nadie
			return ReceptorNulo.getInstancia();
		}
		final Receptor primerYUnicoDestino = allDestinos.get(0);
		return primerYUnicoDestino;
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.NodoBidi#calcularFiltroDeEntradaPara(net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */
	@Override
	protected Condicion calcularFiltroDeEntradaPara(final PataBidireccional pataConectora) {
		final Condicion filtroDeEntradaParaEstePortal = ConjuntoSincronizado.unificarCondiciones(condicionesDelPortal);
		return filtroDeEntradaParaEstePortal;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(identificador_FIELD, getIdentificador()).con("destino", getDestino().toShortString()).toString();
	}

}
