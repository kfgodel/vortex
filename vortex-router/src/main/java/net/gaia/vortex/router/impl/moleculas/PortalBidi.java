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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.portal.api.mensaje.HandlerDePortal;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.api.moleculas.PortalViejo;
import net.gaia.vortex.router.api.moleculas.PortalBidireccional;
import net.gaia.vortex.router.impl.filtros.ConjuntoSincronizado;
import net.gaia.vortex.router.impl.moleculas.comport.ComportamientoPortal;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el componente vortex que implementa el portal con comunicaciones
 * bidireccionales, lo que le permite comunicar sus filtros y optimizar el ruteo de mensajes
 * 
 * @author D. García
 */
public class PortalBidi extends NodoBidi implements PortalBidireccional {
	private static final Logger LOG = LoggerFactory.getLogger(PortalBidi.class);

	private PortalViejo portalInterno;
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
	 * @see net.gaia.vortex.portal.api.moleculas.PortalViejo#enviar(java.lang.Object)
	 */

	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		// Chequeo por debug para evitar el costo de toShortString()
		if (Loggers.BIDI_MSG.isDebugEnabled()) {
			Loggers.BIDI_MSG.debug("Ingresado en portal[{}] el objeto[{}] como mensaje a enviar", this.toShortString(),
					mensaje);
		}
		portalInterno.enviar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.PortalViejo#recibirCon(net.gaia.vortex.portal.api.mensaje.HandlerDePortal)
	 */

	public void recibirCon(final HandlerDePortal<?> handlerDeMensajes) {
		// Registramos la condición para poder unificarlas
		final Condicion nuevaCondicion = handlerDeMensajes.getBicondicional();
		condicionesDelPortal.add(nuevaCondicion);

		// Le pasamos el handler al portal real
		portalInterno.recibirCon(handlerDeMensajes);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug(" En [{}] se modificó el estado de los filtros locales: {}", this.toShortString(),
					getFiltroLocal());
		}
		// Notificamos del cambio interno
		evento_cambioEstadoFiltrosLocales();
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoViejo#setDestino(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setDestino(final Receptor nuevoDestino) {
		if (nuevoDestino == null) {
			throw new IllegalArgumentException("El delegado del portal no puede ser null. A lo sumo un "
					+ ReceptorNulo.class);
		}
		final Receptor destinoAnterior = getDestino();
		if (destinoAnterior.equals(nuevoDestino)) {
			// Ya estamos conectados
			return;
		}
		super.desconectarDe(destinoAnterior);
		super.conectarCon(nuevoDestino);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoViejo#getDestino()
	 */

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
	 * @see net.gaia.vortex.router.impl.moleculas.NodoBidi#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	@Override
	public void conectarCon(final Receptor destino) {
		setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.NodoBidi#calcularFiltroDeEntradaPara(net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */

	@Override
	protected Condicion calcularFiltroDeEntradaPara(final PataBidireccional pataConectora) {
		final Condicion filtroDeEntradaParaEstePortal = getFiltroLocal();
		return filtroDeEntradaParaEstePortal;
	}

	/**
	 * Devuelve el filtro unificado para todas las condiciones indicadas por el usuario
	 * 
	 * @return La condición global
	 */
	private Condicion getFiltroLocal() {
		return ConjuntoSincronizado.unificarCondiciones(condicionesDelPortal);
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
