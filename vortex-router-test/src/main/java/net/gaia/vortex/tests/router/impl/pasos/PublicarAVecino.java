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

import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;
import net.gaia.vortex.tests.router.impl.patas.filtros.Filtro;
import net.gaia.vortex.tests.router2.api.Nodo;

/**
 * Esta clase representa el paso donde un nodo le publica sus filtros al vecino
 * 
 * @author D. García
 */
public class PublicarAVecino extends PasoSupport {

	private PataConectora vecino;
	private PublicacionDeFiltros mensaje;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		// Seteamos como ultimo publicado
		final Filtro filtroAPublicar = mensaje.getFiltro();
		vecino.setFiltroPublicado(filtroAPublicar);

		// Notificamos al nodo
		final Nodo nodoVecino = vecino.getNodoRemoto();
		nodoVecino.recibirPublicacion(mensaje);
	}

	public static PublicarAVecino create(final Nodo origen, final PataConectora pataSalida,
			final PublicacionDeFiltros publicacion) {
		final PublicarAVecino name = new PublicarAVecino();
		name.setNodoLocal(origen);
		name.setVecino(pataSalida);
		name.mensaje = publicacion;
		return name;
	}

	public PataConectora getVecino() {
		return vecino;
	}

	public void setVecino(final PataConectora vecino) {
		this.vecino = vecino;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Publicación[");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getId());
		}
		builder.append("] desde [");
		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append(",");
		if (getVecino() != null) {
			builder.append(getVecino().getIdLocal());
		}
		builder.append("] a [");
		if (getVecino() != null) {
			builder.append(getVecino().getNodoRemoto().getNombre());
			builder.append(",");
			builder.append(getVecino().getIdRemoto());
		}
		builder.append("] de filtros");
		return builder.toString();
	}

}
