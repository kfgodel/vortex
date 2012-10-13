/**
 * 13/10/2012 18:37:00 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;

/**
 * Esta clase es un paso ficticio cuando se intenta confirmar y no hay vecinos
 * 
 * @author D. García
 */
public class ConfirmarSinVecinos extends PasoSupport {

	private PublicacionDeFiltros publicacion;

	public PublicacionDeFiltros getPublicacion() {
		return publicacion;
	}

	public void setPublicacion(final PublicacionDeFiltros publicacion) {
		this.publicacion = publicacion;
	}

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		// Sin efecto asociado
	}

	public static ConfirmarSinVecinos create(final Nodo nodoLocal, final PublicacionDeFiltros publicacion) {
		final ConfirmarSinVecinos confirmar = new ConfirmarSinVecinos();
		confirmar.setNodoLocal(nodoLocal);
		return confirmar;
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
		builder.append("] sin vecinos para publicacion: ");
		builder.append(publicacion);
		return builder.toString();
	}

}
