/**
 * 13/10/2012 12:28:55 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el paso donde un nodo le publica sus filtros al vecino
 * 
 * @author D. García
 */
public class PublicarAVecino extends PasoSupport {

	private Nodo vecino;
	private PublicacionDeFiltros mensaje;

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		vecino.recibirPublicacion(mensaje);
	}

	public static PublicarAVecino create(final Nodo origen, final Nodo vecino, final PublicacionDeFiltros publicacion) {
		final PublicarAVecino name = new PublicarAVecino();
		name.setNodoLocal(origen);
		name.setVecino(vecino);
		name.mensaje = publicacion;
		return name;
	}

	public Nodo getVecino() {
		return vecino;
	}

	public void setVecino(final Nodo vecino) {
		this.vecino = vecino;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Publicación de [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] a [");
		if (getVecino() != null) {
			builder.append(getVecino().getNombre());
		}
		builder.append("] de filtros: ");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getFiltros());
			builder.append(" ID: ");
			builder.append(this.mensaje.getId());
		}
		return builder.toString();
	}

}
