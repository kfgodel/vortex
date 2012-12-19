/**
 * 18/12/2012 19:46:30 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.portal.api.moleculas.Portal;

/**
 * Esta interfaz define el comportamiento de un portal que puede establecer comunicaciones
 * bidireccionales con los nodos a los que se conecta
 * 
 * @author D. García
 */
public interface PortalBidireccional extends NodoBidireccional, Portal {

}
