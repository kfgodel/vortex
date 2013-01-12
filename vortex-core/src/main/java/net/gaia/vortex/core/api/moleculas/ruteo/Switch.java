/**
 * 13/06/2012 16:24:17 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api.moleculas.ruteo;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annotations.Molecula;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje se lo reenvía sólo a los
 * destinos interesados. Para ello cada destino indica qué tipo de mensaje le interesa recibir
 * 
 * @author D. García
 */
@Molecula
public interface Switch extends Nodo {

}
