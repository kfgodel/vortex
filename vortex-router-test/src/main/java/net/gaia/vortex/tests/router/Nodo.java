/**
 * 13/10/2012 10:57:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

/**
 * Esta interfaz representa los elementos comunes a portales y routers
 * 
 * @author D. García
 */
public interface Nodo {

	public String getNombre();

	/**
	 * Crea un enlace unidireccional desde este nodo al otro. Sin la contraparte
	 * 
	 * @param otro
	 *            El otro nodo
	 */
	void conectarCon(Nodo otro);

	/**
	 * Agrega el nodo pasado a los destinos sin requerir un paso de simulador
	 * 
	 * @param nodoDestino
	 */
	public void agregarDestino(Nodo nodoDestino);

	/**
	 * Indica si este nodo tiene al pasado como destino
	 * 
	 * @param otro
	 *            El nodo a comprobar
	 * @return true si es parte de los receptores de este nodo
	 */
	boolean tieneComoDestinoA(Nodo otro);

	/**
	 * Crea una conexión bidireccional entre este nodo y el pasado en un solo paso
	 * 
	 * @param otro
	 *            El otro nodo al que se conectara
	 */
	public void conectarBidi(final Nodo otro);

}
