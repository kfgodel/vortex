/**
 * 19/08/2013 19:45:30 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.basic;

import net.gaia.vortex.api.proto.ComponenteVortex;


/**
 * Esta interfaz representa un componente de la red vortex que tiene la capacidad de enviar mensajes
 * a receptores a los que esta conectado.<br>
 * <br>
 * La implementación de esta interfaz por parte de una clase implica que el componente puede mandar
 * mensajes a otros, pero no implica que pueda recibirlos. El componente permite realizar conexiones
 * a otros componentes a los cuales puede enviarles mensajes y así formar una topología de red.<br>
 * <br>
 * Las implementaciones de esta interfaz ofrecerán conectores para realizar las conexiones
 * dependiendo de la manera en que puedan conectarse según su tipo. Por ejemplo algunos emisores
 * sólo pueden conectarse a una salida, mientras que otros a varias
 * 
 * @author D. García
 */
public interface Emisor extends ComponenteVortex {

}
