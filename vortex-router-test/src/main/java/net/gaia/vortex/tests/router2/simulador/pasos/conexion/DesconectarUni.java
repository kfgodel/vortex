/**
 * 13/10/2012 11:14:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.conexion;

import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa la accion de desconectar un nodo hacia otro en forma unidireccional
 * 
 * @author D. García
 */
public class DesconectarUni extends PasoSupport {

	private NodoSimulacion nodoDestino;

	public NodoSimulacion getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(final NodoSimulacion nodoDestino) {
		this.nodoDestino = nodoDestino;
	}

	public static DesconectarUni create(final NodoSimulacion nodoOrigen, final NodoSimulacion nodoDestino) {
		final DesconectarUni name = new DesconectarUni();
		name.setNodoLocal(nodoOrigen);
		name.setNodoDestino(nodoDestino);
		return name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Desconexión [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] X-> [");
		if (getNodoDestino() != null) {
			builder.append(getNodoDestino().getNombre());
		}
		builder.append("]");
		return builder.toString();

	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	public void ejecutar() {
		getNodoLocal().desconectarDe(getNodoDestino());
	}

}
