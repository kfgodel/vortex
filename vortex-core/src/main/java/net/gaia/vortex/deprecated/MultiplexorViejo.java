/**
 * 14/06/2012 20:22:15 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje lo envía a todos los
 * multiples destinos que conoce.<br>
 * A diferencia de un {@link NexoViejo} esta interfaz posee multiples conexiones posibles de salida
 * 
 * @author D. García
 */
@Deprecated
@Atomo
public interface MultiplexorViejo extends NodoViejo {

	/**
	 * En un {@link MultiplexorViejo} al conectar con un receptor este es agregado al conjunto de
	 * destinos conocidos por este componente
	 * 
	 * @see net.gaia.vortex.deprecated.EmisorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	public void conectarCon(Receptor destino);

	/**
	 * En un {@link MultiplexorViejo} al desconectar un receptor se quita del conjunto de destinos
	 * conocidos. Al quitar todos los destinos es normal que los mensajes recibidos se descarten
	 * 
	 * @see net.gaia.vortex.deprecated.EmisorViejo#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	public void desconectarDe(Receptor destino);

}
