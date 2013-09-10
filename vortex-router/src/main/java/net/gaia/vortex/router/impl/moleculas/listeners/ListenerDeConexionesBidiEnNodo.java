/**
 * 28/01/2013 16:30:29 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

/**
 * Esta interfaz define el contrato esperado de un listener de conexiones bidi de un nodo
 * 
 * @author D. García
 */
public interface ListenerDeConexionesBidiEnNodo {

	/**
	 * Invocado al producirse una conexión bidireccional que comunica al nodo origen con el destino
	 * 
	 * @param origen
	 *            El nodo que es origen de la detección
	 * @param destino
	 *            El nodo remoto con el que se estableció la comunicación (o un intermediario)
	 * @param pataConectada
	 *            La pata por la que se realizó la conexión
	 */
	void onConexionBidiDe(NodoBidireccional origen, Receptor destino, PataBidireccional pataConectada);

}
