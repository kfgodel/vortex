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

import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.Simulador;
import net.gaia.vortex.tests.router.impl.pasos.ConectarBidi;
import net.gaia.vortex.tests.router.impl.pasos.ConectarUni;

/**
 * Esta clase implementa comportamiento comun del nodo
 * 
 * @author D. García
 */
public class NodoSupport implements Nodo {

	private String nombre;

	private Simulador simulador;

	private List<Nodo> destinos;

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

	public List<Nodo> getDestinos() {
		if (destinos == null) {
			destinos = new ArrayList<Nodo>();
		}
		return destinos;
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
		getDestinos().add(nodoDestino);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#tieneComoDestinoA(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public boolean tieneComoDestinoA(final Nodo otro) {
		return getDestinos().contains(otro);
	}

	/**
	 * @see net.gaia.vortex.tests.router.Nodo#conectarBidi(net.gaia.vortex.tests.router.Nodo)
	 */
	@Override
	public void conectarBidi(final Nodo otro) {
		simulador.agregar(ConectarBidi.create(this, otro));
	}

}
