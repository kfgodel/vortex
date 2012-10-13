/**
 * 13/10/2012 11:41:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.pasos;

import net.gaia.vortex.tests.router.Nodo;

/**
 * Este paso representa la conexion bidireccional
 * 
 * @author D. García
 */
public class ConectarBidi extends PasoSupport {

	private Nodo nodoDestino;

	public Nodo getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(final Nodo nodoDestino) {
		this.nodoDestino = nodoDestino;
	}

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		getNodoLocal().agregarDestino(getNodoDestino());
		getNodoDestino().agregarDestino(getNodoLocal());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Conexión [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] <-> [");
		if (getNodoDestino() != null) {
			builder.append(getNodoDestino().getNombre());
		}
		builder.append("]");
		return builder.toString();
	}

	public static ConectarBidi create(final Nodo origen, final Nodo destino) {
		final ConectarBidi name = new ConectarBidi();
		name.setNodoLocal(origen);
		name.setNodoDestino(destino);
		return name;
	}
}
