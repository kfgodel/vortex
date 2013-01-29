/**
 * 14/06/2012 20:22:15 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api.atomos.forward;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje lo envía a todos los
 * multiples destinos que conoce.<br>
 * A diferencia de un {@link Nexo} esta interfaz posee multiples conexiones posibles de salida
 * 
 * @author D. García
 */
@Atomo
public interface Multiplexor extends Nodo {

	/**
	 * En un {@link Multiplexor} al conectar con un receptor este es agregado al conjunto de
	 * destinos conocidos por este componente
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(Receptor destino);

	/**
	 * En un {@link Multiplexor} al desconectar un receptor se quita del conjunto de destinos
	 * conocidos. Al quitar todos los destinos es normal que los mensajes recibidos se descarten
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(Receptor destino);

}
