/**
 * 12/06/2012 22:17:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.api.atomos.mensaje;

/**
 * Esta interfaz representa un mensaje vortex que es información que circula dentro de la red y
 * puede ser procesada, filtrada, transformada, etc por componentes vortex.<br>
 * A través de esta interfaz se abstrae la implementación concreta pero puede pensarse un mensaje
 * vortex como un mapa de clave valor con niveles de profundidad.<br>
 * <br>
 * Un mensaje puede ser alterado durante la circulación en la red para rutearlo pero de manera
 * no-destructiva de la información original, lo que permite recibirlo en destino sin pérdida.
 * 
 * @author D. García
 */
public interface MensajeVortex {

}
