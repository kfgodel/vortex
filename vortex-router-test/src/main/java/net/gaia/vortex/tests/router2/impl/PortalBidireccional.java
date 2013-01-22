/**
 * 08/12/2012 14:12:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.impl;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.api.Portal;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaNada;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaTodo;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPorStrings;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.simulador.Simulador;
import net.gaia.vortex.tests.router2.simulador.pasos.filtros.SetearFiltros;
import net.gaia.vortex.tests.router2.simulador.pasos.mensajes.EnviarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un portal que puede tener conexiones bidireccionales e intercambio de
 * filtros
 * 
 * @author D. García
 */
public class PortalBidireccional extends NodoBidireccional implements Portal {
	private static final Logger LOG = LoggerFactory.getLogger(PortalBidireccional.class);

	private Filtro filtroCliente;

	public static PortalBidireccional create(final String nombre, final Simulador simulador) {
		final PortalBidireccional portal = new PortalBidireccional();
		portal.setNombre(nombre);
		portal.setSimulador(simulador);
		// Inicialmente tomamos que el cliente no quiere nada, y que lo cambie
		portal.filtroCliente = FiltroPasaNada.create();
		return portal;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.NodoBidireccional#calcularFiltroDeEntradaPara(net.gaia.vortex.tests.router2.impl.patas.PataBidireccional)
	 */
	@Override
	protected Filtro calcularFiltroDeEntradaPara(final PataBidireccional pataConectora) {
		// Para el portal el filtro de entrada de las patas es lo que quiere el cliente
		return filtroCliente;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.NodoBidireccional#procesarConHandlersInternos(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	@Override
	protected void procesarConHandlersInternos(final Mensaje mensaje) {
		LOG.info(" En [{}] se recibió el mensaje[{}]", this.getNombre(), mensaje.getIdDeMensaje());
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Portal#setFiltros(java.lang.String[])
	 */
	@Override
	public void setFiltros(final String... filtros) {
		Filtro filtroPedido;
		if (filtros.length == 0) {
			// Es un "pasa todos"
			filtroPedido = FiltroPasaTodo.create();
		} else {
			final LinkedHashSet<String> pedidos = new LinkedHashSet<String>(Arrays.asList(filtros));
			filtroPedido = FiltroPorStrings.create(pedidos);
		}
		this.setFiltroCliente(filtroPedido);
		evento_cambioEstadoFiltrosLocales();
	}

	private void setFiltroCliente(final Filtro filtroCliente) {
		this.filtroCliente = filtroCliente;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Portal#enviar(net.gaia.vortex.tests.router2.mensajes.MensajeNormal)
	 */
	@Override
	public void enviar(final MensajeNormal mensaje) {
		final List<PataBidireccional> allPatas = getPatas();
		for (final PataBidireccional pataConectora : allPatas) {
			pataConectora.enviarMensajeNormal(mensaje);
		}
	}

	/**
	 * Genera un paso en la simulación por un cambio de filtros en este portal
	 * 
	 * @param filtros
	 *            Los filtros a cambiar
	 */
	public void simularSeteoDeFiltros(final String... filtros) {
		procesar(SetearFiltros.create(this, filtros));
	}

	/**
	 * Genera un paso en la simulación por el envio del mensaje originado en este portal
	 * 
	 * @param mensajeAEnviar
	 *            El mensaje a enviar
	 */
	public void simularEnvioDe(final MensajeNormal mensajeAEnviar) {
		procesar(EnviarMensaje.create(this, mensajeAEnviar));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.NodoBidireccional#evento_recibirMensajeEnNodo(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	@Override
	protected void evento_recibirMensajeEnNodo(final Mensaje mensaje) {
		if (mensaje instanceof MensajeNormal) {
			// Si es un mensaje normal no queremos que se propague a las patas
			procesarConHandlersInternos(mensaje);
		} else {
			// Es un metamensaje
			super.evento_recibirMensajeEnNodo(mensaje);
		}
	}
}
