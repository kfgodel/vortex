/**
 * 13/10/2012 20:57:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.patas;

import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.impl.patas.filtros.Filtro;
import net.gaia.vortex.tests.router.impl.patas.filtros.SinFiltro;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el punto de conexión que un nodo tiene con otro para identificarlo, e
 * identificarse en la comunicación
 * 
 * @author D. García
 */
public class PataConectora {

	private Long idLocal;
	public static final String idLocal_FIELD = "idLocal";

	private Long idRemoto;
	public static final String idRemoto_FIELD = "idRemoto";
	private Nodo nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private Filtro filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	private Filtro filtroPublicado;
	public static final String filtroPublicado_FIELD = "filtroPublicado";

	public Long getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(final Long idLocal) {
		this.idLocal = idLocal;
	}

	public Long getIdRemoto() {
		return idRemoto;
	}

	public void setIdRemoto(final Long idRemoto) {
		this.idRemoto = idRemoto;
	}

	public Nodo getNodoRemoto() {
		return nodoRemoto;
	}

	public void setNodoRemoto(final Nodo vecino) {
		this.nodoRemoto = vecino;
	}

	/**
	 * Crea una pata para ser usada en las comunicaciones
	 * 
	 * @param idLocal
	 *            El identificador dentro del nodo
	 * @param nodoRemoto
	 *            El nodo destino de las comunicaciones
	 * @return La pata creada
	 */
	public static PataConectora create(final Long idLocal, final Nodo nodoRemoto) {
		final PataConectora pata = new PataConectora();
		pata.idLocal = idLocal;
		pata.nodoRemoto = nodoRemoto;
		pata.filtroDeSalida = SinFiltro.create();
		pata.filtroPublicado = SinFiltro.create();
		return pata;
	}

	/**
	 * Indica si esta pata ya tiene definido un ID remoto en el vecino
	 * 
	 * @return false si aun no se solicito el ID
	 */
	public boolean tieneIdRemoto() {
		return this.idRemoto != null;
	}

	/**
	 * Indica si esta pata conecta con el nodo indicado como remoto
	 * 
	 * @param otro
	 *            El nodo a comparar
	 * @return true si es el mismo nodo
	 */
	public boolean conectaA(final Nodo otro) {
		return otro == this.nodoRemoto;
	}

	/**
	 * Registra en esta pata los filtros validos para los mensajes de salida
	 * 
	 * @param nuevosFiltros
	 *            Los filtros usables en esta pata
	 */
	public void filtrarCon(final Filtro nuevosFiltros) {
		this.filtroDeSalida = nuevosFiltros;
	}

	public Filtro getFiltroDeSalida() {
		return filtroDeSalida;
	}

	public void setFiltroDeSalida(final Filtro filtroActual) {
		this.filtroDeSalida = filtroActual;
	}

	public Filtro getFiltroPublicado() {
		return filtroPublicado;
	}

	public void setFiltroPublicado(final Filtro filtroPublicado) {
		this.filtroPublicado = filtroPublicado;
	}

	/**
	 * Indica si el filtro pasado es igual logicamente al ultimo filtro publicado
	 * 
	 * @param filtroModificado
	 *            Nuevo filtro para esta pata
	 * @return false si son iguales
	 */
	public boolean yaSePublico(final Filtro filtroModificado) {
		final Filtro ultimaPublicacion = this.getFiltroPublicado();
		final boolean sonIguales = ultimaPublicacion.equals(filtroModificado);
		return sonIguales;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocal_FIELD, idLocal).con(idRemoto_FIELD, idRemoto)
				.con(filtroDeSalida_FIELD, filtroDeSalida).con(filtroPublicado_FIELD, filtroPublicado)
				.con(nodoRemoto_FIELD, nodoRemoto).toString();
	}

}
