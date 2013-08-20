/**
 * 28/01/2013 17:06:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.listeners;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.atomos.EmisorViejo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

/**
 * Esta clase representa una conexión bidi entre dos nodos a modo de registro
 * 
 * @author D. García
 */
public class ConexionBidi {

	private NodoBidireccional origen;
	private Receptor destino;
	private PataBidireccional pata;

	public NodoBidireccional getOrigen() {
		return origen;
	}

	public void setOrigen(final NodoBidireccional origen) {
		this.origen = origen;
	}

	public Receptor getDestino() {
		return destino;
	}

	public void setDestino(final Receptor destino) {
		this.destino = destino;
	}

	public PataBidireccional getPata() {
		return pata;
	}

	public void setPata(final PataBidireccional pata) {
		this.pata = pata;
	}

	public static ConexionBidi create(final NodoBidireccional origen, final Receptor destino,
			final PataBidireccional pata) {
		final ConexionBidi conexion = new ConexionBidi();
		conexion.destino = destino;
		conexion.origen = origen;
		conexion.pata = pata;
		return conexion;
	}

	/**
	 * Indica si esta conexion es entre los nodos indicados
	 * 
	 * @param emisor
	 *            El nodo de origen
	 * @param receptor
	 *            El nodo destino
	 * @return true si esta conexion es entre el origen y destino indicados
	 */
	public boolean uneA(final EmisorViejo emisor, final Receptor receptor) {
		final boolean mismoOrigen = origen.equals(emisor);
		final boolean mismoDestino = destino.equals(receptor);
		return mismoOrigen && mismoDestino;
	}
}
