/**
 * 14/06/2012 20:09:45 Copyright (C) 2011 10Pines S.R.L.
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.es_ES"><img
 * alt="Licencia de Creative Commons" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" property="dct:title">Vortex</span> by <a
 * xmlns:cc="http://creativecommons.org/ns#" href="https://sourceforge.net/p/vortexnet"
 * property="cc:attributionName" rel="cc:attributionURL">Vortex Group</a> is licensed under a <a
 * rel="license" href="http://creativecommons.org/licenses/by/3.0/deed.es_ES">Creative Commons
 * Reconocimiento 3.0 Unported License</a>.<br />
 * Creado a partir de la obra en <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://sourceforge.net/p/vortexnet"
 * rel="dct:source">https://sourceforge.net/p/vortexnet</a>
 */
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa el componente protagónico de la red vortex que es tanto un receptor como
 * un emisor de mensajes que puede estar conectado con otros componentes.<br>
 * <br>
 * Las implementaciones concretas de esta interfaz definen el comportamiento esperable del tipo de
 * nodo y pueden permitir que las conexiones sean multiples o únicas.<br>
 * 
 * 
 * @author D. García
 */
@Deprecated
public interface NodoViejo extends EmisorViejo, Receptor {

}
