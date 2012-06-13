/**
 * 13/06/2012 16:24:17 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.api.nodos;

import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Multiplexor;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje se lo reenvia a los
 * destinos conocidos que les interesa el mensaje según una definición de conjuntos indicada para
 * cada destino
 * 
 * @author D. García
 */
public interface Switch extends Multiplexor, ComponenteVortex {

}
