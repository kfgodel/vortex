/**
 * 18/12/2012 19:48:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.moleculas;

/**
 * Esta interfaz representa un nodo router de mensajes.<br>
 * Este tipo de nodos puede discriminar los nodos a los que está conectado, enviando los mensajes
 * que sólo son pertinentes en función de las condiciones que los otros nodos le publiquen
 * 
 * @author D. García
 */
public interface Router extends NodoBidireccional {

}
