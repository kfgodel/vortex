/**
 * 13/06/2012 01:50:02 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.moleculas;

import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.annon.Molecula;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Multiplexor;
import net.gaia.vortex.core2.api.nodos.Nodo;

/**
 * Esta clase representa un {@link Nodo} en su implementación mínima
 * 
 * @author D. García
 */
@Molecula
public class NodoMinimo implements Nodo {

	private Multiplexor comportamientoDeSalida;
	private ComponenteProxy comportamientoDeEntrada;

	/**
	 * @see net.gaia.vortex.core2.api.atomos.Multiplexor#agregarDestino(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void agregarDestino(final ComponenteVortex destino) {
		comportamientoDeSalida.agregarDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.Multiplexor#quitarDestino(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void quitarDestino(final ComponenteVortex destino) {
		comportamientoDeSalida.quitarDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core2.api.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		comportamientoDeEntrada.recibirMensaje(mensaje);
	}

	public Multiplexor getComportamientoDeSalida() {
		return comportamientoDeSalida;
	}

	public void setComportamientoDeSalida(final Multiplexor comportamientoDeSalida) {
		if (comportamientoDeSalida == null) {
			throw new IllegalArgumentException("El multiplexor del nodo minimo no puede ser null");
		}
		this.comportamientoDeSalida = comportamientoDeSalida;
	}

	public ComponenteProxy getComportamientoDeEntrada() {
		return comportamientoDeEntrada;
	}

	public void setComportamientoDeEntrada(final ComponenteProxy comportamientoDeEntrada) {
		if (comportamientoDeEntrada == null) {
			throw new IllegalArgumentException("El comportamiento de entrada del nodo minimo no puede ser null");
		}
		// Vinculamos la entrada con la salida
		comportamientoDeEntrada.setDelegado(comportamientoDeSalida);
		this.comportamientoDeEntrada = comportamientoDeEntrada;
	}

	public static NodoMinimo create(final ComponenteProxy entrada, final Multiplexor salida) {
		final NodoMinimo nodo = new NodoMinimo();
		nodo.setComportamientoDeSalida(salida);
		nodo.setComportamientoDeEntrada(entrada);
		return nodo;
	}
}
