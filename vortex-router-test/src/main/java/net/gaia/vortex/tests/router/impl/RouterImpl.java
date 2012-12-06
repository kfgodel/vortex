/**
 * 13/10/2012 11:02:41 Copyright (C) 2011 Darío L. García
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
import java.util.List;

import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;
import net.gaia.vortex.tests.router.impl.patas.filtros.Filtro;
import net.gaia.vortex.tests.router.impl.patas.filtros.SinFiltro;
import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.api.Router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el router para la simulacion.<br>
 * El router a diferencia del portal, no tiene filtros propios (porque no recibe mensajes para sí),
 * y cuando recibe mensajes, los rutea a los interesados según los filtros de otros. También propaga
 * los filtros de los demas, como si fueran propios
 * 
 * @author D. García
 */
public class RouterImpl extends NodoSupport implements Router {
	private static final Logger LOG = LoggerFactory.getLogger(RouterImpl.class);

	public static RouterImpl create(final String nombre, final Simulador simulador) {
		final RouterImpl router = new RouterImpl();
		router.setNombre(nombre);
		router.setSimulador(simulador);
		return router;
	}

	/**
	 * Indica si este nodo utiliza los filtros indicados con el nodo pasado
	 * 
	 * @param nodo
	 *            El nodo para el que se quieren verificar los filtros usados
	 * @param filtros
	 *            Los filtros que debería usar
	 * @return false si el nodo no está conectado a este router o el filtro no esta contenido en los
	 *         usados
	 */
	public boolean usaFiltrosCon(final Nodo nodo, final String... filtros) {
		final PataConectora pataSalida = getPataPorNodo(nodo);
		if (pataSalida == null) {
			return false;
		}
		final boolean usaFiltro = pataSalida.getFiltroDeSalida().usaA(Arrays.asList(filtros));
		return usaFiltro;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#calcularFiltrosPara(net.gaia.vortex.tests.router.impl.patas.PataConectora)
	 */
	@Override
	protected Filtro calcularFiltrosPara(final PataConectora pataSalida) {
		Filtro filtroResultante = null;
		final List<PataConectora> allPatas = getDestinos();
		for (final PataConectora pataConectora : allPatas) {
			if (pataSalida.equals(pataConectora)) {
				// No queremos republicarle sus propios filtros!
				continue;
			}
			final Filtro filtroDePata = pataConectora.getFiltroDeSalida();
			if (filtroResultante == null) {
				// Es la primera iteracion
				filtroResultante = filtroDePata;
			} else {
				filtroResultante = filtroResultante.mergearCon(filtroDePata);
			}
		}
		if (filtroResultante == null) {
			filtroResultante = SinFiltro.create();
		}
		return filtroResultante;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#recibirPublicacion(net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros)
	 */
	@Override
	public void recibirPublicacion(final PublicacionDeFiltros publicacion) {
		super.recibirPublicacion(publicacion);
		propagarFiltros();
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#propagarFiltrosDe(net.gaia.vortex.tests.router.impl.patas.PataConectora)
	 */
	protected void propagarFiltros() {
		final List<PataConectora> allPatas = getDestinos();
		LOG.debug("Propagando filtros desde [{}] a {} patas", getNombre(), allPatas.size());
		for (final PataConectora pataConectora : allPatas) {
			publicarFiltrosEn(pataConectora);
		}
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.NodoSupport#realizarAccionEspecificaAlRecibir(net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal)
	 */
	@Override
	protected void realizarAccionEspecificaAlRecibir(final MensajeNormal mensaje) {
		// Se lo enviamos al resto si corresponde
		propagarMensaje(mensaje);
	}
}
