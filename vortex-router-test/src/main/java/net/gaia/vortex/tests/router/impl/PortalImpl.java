/**
 * 13/10/2012 11:02:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import net.gaia.vortex.tests.router.Portal;
import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router.impl.mensajes.RespuestaDeIdRemoto;
import net.gaia.vortex.tests.router.impl.pasos.PedirIdRemoto;
import net.gaia.vortex.tests.router.impl.pasos.PublicarAVecino;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el comportamiento para los test
 * 
 * @author D. García
 */
public class PortalImpl extends NodoSupport implements Portal {
	private static final Logger LOG = LoggerFactory.getLogger(PortalImpl.class);

	private LinkedHashSet<String> filtros;

	public LinkedHashSet<String> getFiltros() {
		if (filtros == null) {
			filtros = new LinkedHashSet<String>();
		}
		return filtros;
	}

	public static PortalImpl create(final String nombre, final Simulador simulador) {
		final PortalImpl portal = new PortalImpl();
		portal.setNombre(nombre);
		portal.setSimulador(simulador);
		return portal;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Portal#publicarFiltros()
	 */
	@Override
	public void publicarFiltros() {
		final List<PataConectora> destinos = getDestinos();
		if (destinos.isEmpty()) {
			LOG.debug("  Publicación de [{}] sin vecinos para filtros: {}", this.getNombre(), this.getFiltros());
			return;
		}

		for (final PataConectora pataSalida : destinos) {
			publicarFiltrosEn(pataSalida);
		}
	}

	/**
	 * Intenta publicar los filtros en la pata indicada
	 * 
	 * @param pataSalida
	 *            La pata por la que se intentará comunicar los filtros a otro nodo
	 */
	private void publicarFiltrosEn(final PataConectora pataSalida) {
		if (!pataSalida.tieneIdRemoto()) {
			// Si no tiene ID tenemos que conseguirlo antes de publicar
			conseguirIdRemotoDe(pataSalida);
		} else {
			final Long idRemoto = pataSalida.getIdRemoto();
			final PublicacionDeFiltros publicacion = PublicacionDeFiltros.create(idRemoto, filtros);
			agregarComoEnviado(publicacion);
			getSimulador().agregar(PublicarAVecino.create(this, pataSalida, publicacion));
		}
	}

	/**
	 * Intenta intercambiar ids de patas para poder publicar filtros
	 * 
	 * @param pataSalida
	 *            La pata sin id
	 */
	private void conseguirIdRemotoDe(final PataConectora pataSalida) {
		final Long idLocal = pataSalida.getIdLocal();
		final PedidoDeIdRemoto pedidoDeIdRemoto = PedidoDeIdRemoto.create(idLocal);
		agregarComoEnviado(pedidoDeIdRemoto);
		getSimulador().agregar(PedirIdRemoto.create(this, pataSalida, pedidoDeIdRemoto));
	}

	/**
	 * @see net.gaia.vortex.tests.router.Portal#setFiltros(java.lang.String[])
	 */
	@Override
	public void setFiltros(final String... filtros) {
		this.filtros = new LinkedHashSet<String>(Arrays.asList(filtros));
	}

	/**
	 * @see net.gaia.vortex.tests.router.Portal#setearYPublicarFiltros(java.lang.String[])
	 */
	@Override
	public void setearYPublicarFiltros(final String... filtros) {
		setFiltros(filtros);
		publicarFiltros();
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#recibirRespuestaDeIdRemoto(net.gaia.vortex.tests.router.impl.mensajes.RespuestaDeIdRemoto)
	 */
	@Override
	public void recibirRespuestaDeIdRemoto(final RespuestaDeIdRemoto respuesta) {
		super.recibirRespuestaDeIdRemoto(respuesta);

		final PedidoDeIdRemoto pedidoOriginal = respuesta.getPedido();
		if (!getEnviados().contains(pedidoOriginal)) {
			// No lo enviamos nosotros
			LOG.debug("  Rechazando en [{}] respuesta{} por pedido{} no realizado", new Object[] { this.getNombre(),
					respuesta, pedidoOriginal });
			return;
		}
		final PataConectora pataSalida = asociarIdRemotoAPataLocal(respuesta);
		if (pataSalida == null) {
			// Ya no está más conectado
			LOG.debug("  Publicacion de filtros desde [{},{}] a [?,{}] no posible por que ya no existe conexion",
					new Object[] { this.getNombre(), pedidoOriginal.getIdDePata(), respuesta.getIdAsignado() });
			return;
		}
		publicarFiltrosEn(pataSalida);
	}

	/**
	 * Busca la pata local referida en la respuesta y le asigna el ID remoto que nos indican
	 * 
	 * @param respuesta
	 *            La respuesta que asigna un id remoto
	 * @return La pata local a la que corresponde la respuesta
	 */
	private PataConectora asociarIdRemotoAPataLocal(final RespuestaDeIdRemoto respuesta) {
		final Long idLocal = respuesta.getPedido().getIdDePata();
		final PataConectora pataSalida = getPataPorIdLocal(idLocal);
		if (pataSalida == null) {
			// Ya no existe la paga
			return null;
		}
		final Long idRemoto = respuesta.getIdAsignado();
		pataSalida.setIdRemoto(idRemoto);
		return pataSalida;
	}

}
