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

import java.util.Set;

import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.impl.patas.filtros.Filtro;
import net.gaia.vortex.tests.router.impl.patas.filtros.FiltroPorStrings;
import net.gaia.vortex.tests.router.impl.patas.filtros.SinFiltro;

/**
 * Esta clase representa el punto de conexión que un nodo tiene con otro para identificarlo, e
 * identificarse en la comunicación
 * 
 * @author D. García
 */
public class PataConectora {

	private Long idLocal;
	private Long idRemoto;
	private Nodo nodoRemoto;
	private Filtro filtroActual;

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

	public static PataConectora create(final Long idLocal, final Nodo nodoRemoto) {
		final PataConectora name = new PataConectora();
		name.idLocal = idLocal;
		name.nodoRemoto = nodoRemoto;
		name.filtroActual = SinFiltro.create();
		return name;
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
	public void filtrarCon(final Set<String> nuevosFiltros) {
		this.filtroActual = FiltroPorStrings.create(nuevosFiltros);
	}

	public Filtro getFiltroActual() {
		return filtroActual;
	}

	public void setFiltroActual(final Filtro filtroActual) {
		this.filtroActual = filtroActual;
	}

}
