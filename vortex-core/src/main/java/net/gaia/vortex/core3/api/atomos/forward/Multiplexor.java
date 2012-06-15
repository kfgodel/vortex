/**
 * 14/06/2012 20:22:15 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos.forward;

import net.gaia.vortex.core3.api.Nodo;
import net.gaia.vortex.core3.api.atomos.Emisor;
import net.gaia.vortex.core3.api.atomos.Receptor;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje lo envía a todos los
 * multiples destinos que conoce.<br>
 * A diferencia de un {@link Nexo} esta interfaz representa multiples conexiones posibles de salida
 * 
 * @author D. García
 */
public interface Multiplexor extends Nodo {

	/**
	 * En un {@link Multiplexor} al conectar con un receptor este es agregado al conjunto de
	 * destintos conocidos por este componente
	 * 
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#conectarCon(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(Receptor destino);

	/**
	 * En yun {@link Multiplexor} al desconectar un receptor se quita del conjunto de destinos
	 * conocidos. Lo esperable al no quedar ninguno es que este componente actue como un
	 * {@link Receptor}, no pudiendo enviar a nadie
	 * 
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(Receptor destino);

}
