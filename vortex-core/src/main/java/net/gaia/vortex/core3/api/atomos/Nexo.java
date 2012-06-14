/**
 * 14/06/2012 20:12:58 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos;

/**
 * Esta interfaz representa un componente de vortex que permite una unica conexion saliente. Al
 * conectar este componente con un receptor se pierde la conexion previa.<br>
 * Esta interfaz es la forma básica de muchos componentes básicos de la red vortex predefinidos
 * 
 * 
 * @author D. García
 */
public interface Nexo extends Nodo {

	/**
	 * En un {@link Nexo} al conectar con un nuevo receptor la conexion previa se pierde, pudiendo
	 * tener sólo una conexión a la vez
	 * 
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#conectarCon(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(Receptor destino);

	/**
	 * En un {@link Nexo} la desconexión puede ser ignorada, o puede dejar a este componente sin
	 * destino. Si el nexo se queda sin destino (si admite esa posibilidad) al recibir un mensaje lo
	 * esperado es que no haga ningún acción de salida (equivalente a un {@link Receptor} puro)
	 * 
	 * @see net.gaia.vortex.core3.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(Receptor destino);

}
