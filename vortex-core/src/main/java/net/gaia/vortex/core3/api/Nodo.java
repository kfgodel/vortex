/**
 * 14/06/2012 20:09:45 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api;

import net.gaia.vortex.core3.api.atomos.Emisor;
import net.gaia.vortex.core3.api.atomos.Receptor;

/**
 * Esta interfaz representa el componente principal de la red vortex que es tanto un receptor como
 * un emisor de mensajes que puede estar conectado con otros componentes.<br>
 * <br>
 * Las implementaciones concretas de esta interfaz pueden permitir que las conexiones sean multiples
 * o unicas
 * 
 * @author D. García
 */
public interface Nodo extends Emisor, Receptor {

}
