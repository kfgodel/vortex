/**
 * 14/06/2012 20:12:58 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api.atomos.forward;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.atomos.Receptor;

/**
 * Esta interfaz representa un componente de vortex que permite una única conexión saliente, por lo
 * que al conectar este componente con un nuevo receptor se pierde la conexión previa.<br>
 * <br>
 * Mediante este componente se pueden encadenar acciones que la red realiza al recibir los mensajes
 * 
 * 
 * @author D. García
 */
public interface Nexo extends Nodo {

	/**
	 * En un {@link Nexo} al conectar con un nuevo receptor la conexion previa se pierde, pudiendo
	 * tener sólo una conexión a la vez
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(Receptor destino);

	/**
	 * En un {@link Nexo} la desconexión puede ser ignorada, o puede dejar a este componente sin
	 * destino. Si el nexo se queda sin destino (si admite esa posibilidad) al recibir un mensaje lo
	 * esperado es que no haga ningún acción de salida (equivalente a un {@link Receptor} puro)
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(Receptor destino);

	/**
	 * Establece el único destino de este componente. Este método tiene el mismo efecto que
	 * {@link #conectarCon(Receptor)} sólo que su invocación es menos ambigua respecto de la
	 * cantidad de destinos
	 * 
	 * @param destino
	 *            El nuevo destino que reemplazará al anterior
	 */
	public void setDestino(Receptor destino);

	/**
	 * Devuelve el único destino de este componente que recibe los mensajes que pasan por este
	 * componente
	 * 
	 * @return El receptor que recibe los mensajes de este nexo
	 */
	public Receptor getDestino();

}
