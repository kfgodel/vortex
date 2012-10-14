/**
 * 13/10/2012 11:08:14 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.tests.router.Mensaje;
import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router.impl.mensajes.RespuestaDeIdRemoto;
import net.gaia.vortex.tests.router.impl.pasos.ConectarBidi;
import net.gaia.vortex.tests.router.impl.pasos.ConectarUni;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;

/**
 * Esta clase implementa comportamiento comun del nodo
 * 
 * @author D. García
 */
public class NodoSupport implements Nodo {

	private String nombre;

	private Simulador simulador;

	private final AtomicLong proximoIdPata = new AtomicLong(0);

	private List<PataConectora> destinos;

	private List<Mensaje> enviados;
	private List<Mensaje> recibidos;

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#getNombre()
	 */
	@Override
	public String getNombre() {
		return nombre;
	}

	protected void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	protected Simulador getSimulador() {
		return simulador;
	}

	protected void setSimulador(final Simulador simulador) {
		this.simulador = simulador;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#conectarCon(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo otro) {
		final ConectarUni nuevoPaso = ConectarUni.create(this, otro);
		simulador.agregar(nuevoPaso);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#agregarDestino(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public void agregarDestino(final Nodo nodoDestino) {
		final Long idNuevaPata = this.proximoIdPata.getAndIncrement();
		final PataConectora nuevaPata = PataConectora.create(idNuevaPata, nodoDestino);
		getDestinos().add(nuevaPata);
	}

	public List<PataConectora> getDestinos() {
		if (destinos == null) {
			destinos = new ArrayList<PataConectora>();
		}
		return destinos;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#tieneComoDestinoA(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public boolean tieneComoDestinoA(final Nodo otro) {
		final List<PataConectora> allDestinos = getDestinos();
		for (final PataConectora pataConectora : allDestinos) {
			if (pataConectora.conectaA(otro)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#conectarBidi(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public void conectarBidi(final Nodo otro) {
		simulador.agregar(ConectarBidi.create(this, otro));
	}

	public List<Mensaje> getEnviados() {
		if (enviados == null) {
			enviados = new ArrayList<Mensaje>();
		}
		return enviados;
	}

	public void setEnviados(final List<Mensaje> enviados) {
		this.enviados = enviados;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#recibirPublicacion(net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros)
	 */
	@Override
	public void recibirPublicacion(final PublicacionDeFiltros publicacion) {
		getRecibidos().add(publicacion);
	}

	public List<Mensaje> getRecibidos() {
		if (recibidos == null) {
			recibidos = new ArrayList<Mensaje>();
		}
		return recibidos;
	}

	public void setRecibidos(final List<Mensaje> recibidos) {
		this.recibidos = recibidos;
	}

	public void agregarComoEnviado(final Mensaje mensaje) {
		getEnviados().add(mensaje);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#recibirPedidoDeId(net.gaia.vortex.tests.router.impl.mensajes.PedidoDeIdRemoto)
	 */
	@Override
	public void recibirPedidoDeId(final PedidoDeIdRemoto pedido) {
		getRecibidos().add(pedido);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#recibirRespuestaDeIdRemoto(net.gaia.vortex.tests.router.impl.mensajes.RespuestaDeIdRemoto)
	 */
	@Override
	public void recibirRespuestaDeIdRemoto(final RespuestaDeIdRemoto respuesta) {
		getRecibidos().add(respuesta);
	}

	protected PataConectora getPataPorIdLocal(final Long idLocal) {
		for (final PataConectora pataSalida : getDestinos()) {
			final Long idDePata = pataSalida.getIdLocal();
			if (idDePata.equals(idLocal)) {
				return pataSalida;
			}
		}
		return null;
	}

	/**
	 * Devuelve la pata de salida identificandola por nodo
	 * 
	 * @param nodo
	 *            El nodo cuya pata quiere obtenerse
	 * @return La pata del nodo o null si no existe
	 */
	protected PataConectora getPataPorNodo(final Nodo nodo) {
		final List<PataConectora> allDestinos = getDestinos();
		for (final PataConectora pataConectora : allDestinos) {
			if (pataConectora.getNodoRemoto().equals(nodo)) {
				return pataConectora;
			}
		}
		return null;
	}

}
