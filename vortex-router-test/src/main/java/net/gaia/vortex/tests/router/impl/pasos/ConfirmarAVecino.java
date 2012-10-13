/**
 * 13/10/2012 18:41:32 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.tests.router.impl.mensajes.ConfirmacionDePublicacion;

/**
 * Esta clase representa el paso del nodo router para confirmar a una publicacion de un vecino
 * 
 * @author D. García
 */
public class ConfirmarAVecino extends PasoSupport {

	private Nodo vecino;
	private ConfirmacionDePublicacion confirmacion;

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		vecino.recibirConfirmacionDePublicacion(confirmacion);
	}

	public static ConfirmarAVecino create(final Nodo nodoLocal, final Nodo vecino,
			final ConfirmacionDePublicacion confirmacion) {
		final ConfirmarAVecino confirmar = new ConfirmarAVecino();
		confirmar.setNodoLocal(nodoLocal);
		confirmar.vecino = vecino;
		confirmar.confirmacion = confirmacion;
		return confirmar;

	}

	public ConfirmacionDePublicacion getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(final ConfirmacionDePublicacion confirmacion) {
		this.confirmacion = confirmacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Confirmación de [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] a [");
		if (vecino != null) {
			builder.append(vecino.getNombre());
		}
		builder.append("] de publicacion");
		if (this.confirmacion != null) {
			builder.append(this.confirmacion.getPublicacion());
			builder.append(" ID: ");
			builder.append(this.confirmacion.getId());
		}
		return builder.toString();
	}
}
