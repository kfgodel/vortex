/**
 * 14/06/2012 20:09:45 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api;

import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;

/**
 * Esta interfaz representa el componente protegonico de la red vortex que es tanto un receptor como
 * un emisor de mensajes que puede estar conectado con otros componentes.<br>
 * <br>
 * Las implementaciones concretas de esta interfaz definen el comportamiento esperable del tipo de
 * nodo y pueden permitir que las conexiones sean multiples o únicas.<br>
 * 
 * @author D. García
 */
public interface Nodo extends Emisor, Receptor {

}
