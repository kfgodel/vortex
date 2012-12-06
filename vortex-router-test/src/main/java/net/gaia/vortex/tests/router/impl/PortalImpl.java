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

import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;
import net.gaia.vortex.tests.router.impl.patas.filtros.Filtro;
import net.gaia.vortex.tests.router.impl.patas.filtros.FiltroPorStrings;
import net.gaia.vortex.tests.router.impl.patas.filtros.SinFiltro;
import net.gaia.vortex.tests.router2.api.ListenerDeFiltros;
import net.gaia.vortex.tests.router2.api.Portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el comportamiento para los test.<br>
 * El portal, a diferencia del router, no reenvia los mensajes que recibe, y tiene un conjunto de
 * filtros propios que al recibir un mensaje invoca un handler
 * 
 * @author D. García
 */
public class PortalImpl extends NodoSupport implements Portal {
	private static final Logger LOG = LoggerFactory.getLogger(PortalImpl.class);

	private Filtro filtroPedidoPorUsuario;

	public static PortalImpl create(final String nombre, final Simulador simulador) {
		final PortalImpl portal = new PortalImpl();
		portal.setNombre(nombre);
		portal.setSimulador(simulador);
		// El estado inicial es que se piden todos los mensajes
		portal.filtroPedidoPorUsuario = SinFiltro.create();
		return portal;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Portal#setFiltros(java.lang.String[])
	 */
	@Override
	public void setFiltros(final String... filtros) {
		final LinkedHashSet<String> pedidos = new LinkedHashSet<String>(Arrays.asList(filtros));
		this.filtroPedidoPorUsuario = FiltroPorStrings.create(pedidos);
	}

	/**
	 * @param filtros
	 */
	public void setearYPublicarFiltros(final String... filtros) {
		setFiltros(filtros);
		publicarFiltros();
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#calcularFiltrosPara(net.gaia.vortex.tests.router.impl.patas.PataConectora)
	 */
	@Override
	protected Filtro calcularFiltrosPara(final PataConectora pataSalida) {
		return filtroPedidoPorUsuario;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Portal#enviar(net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal)
	 */
	@Override
	public void enviar(final MensajeNormal mensaje) {
		propagarMensaje(mensaje);
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#realizarAccionEspecificaAlRecibir(net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal)
	 */
	@Override
	protected void realizarAccionEspecificaAlRecibir(final MensajeNormal mensaje) {
		LOG.info(" En [{}] se recibió el mensaje[{}]", this.getNombre(), mensaje.getId());
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Portal#setListenerDeFiltrosExternos(net.gaia.vortex.tests.router2.api.ListenerDeFiltros)
	 */
	@Override
	public void setListenerDeFiltrosExternos(final ListenerDeFiltros listenerDeExternos) {
		// TODO Auto-generated method stub

	}
}
