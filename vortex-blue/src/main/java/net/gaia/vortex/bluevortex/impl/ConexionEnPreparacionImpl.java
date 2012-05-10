/**
 * 10/05/2012 00:49:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.impl;

import net.gaia.vortex.bluevortex.api.BlueVortex;
import net.gaia.vortex.bluevortex.api.ConexionEnPreparacion;
import net.gaia.vortex.bluevortex.api.ConexionVortex;
import net.gaia.vortex.bluevortex.api.FiltroDeMensajes;
import net.gaia.vortex.bluevortex.api.HandlerDeMensajes;

/**
 * Esta clase representa la conexión en estado de preparación, cuando todavía no es utilizable
 * 
 * @author D. García
 */
public class ConexionEnPreparacionImpl implements ConexionEnPreparacion {

	private BlueVortex vortex;
	private HandlerDeMensajes handler;
	private FiltroDeMensajes filtro;

	/**
	 * Crea una conexión en estado de preparación para ser usada
	 * 
	 * @return La pre-conexión creada
	 */
	public static ConexionEnPreparacionImpl create(final BlueVortex vortex) {
		final ConexionEnPreparacionImpl conexion = new ConexionEnPreparacionImpl();
		conexion.vortex = vortex;
		return conexion;
	}

	/**
	 * @see net.gaia.vortex.bluevortex.api.ConexionEnPreparacion#setMessageHandler(net.gaia.vortex.bluevortex.api.HandlerDeMensajes)
	 */
	@Override
	public void setMessageHandler(final HandlerDeMensajes handler) {
		this.handler = handler;
	}

	/**
	 * @see net.gaia.vortex.bluevortex.api.ConexionEnPreparacion#setMessageFilter(net.gaia.vortex.bluevortex.api.FiltroDeMensajes)
	 */
	@Override
	public void setMessageFilter(final FiltroDeMensajes filtro) {
		this.filtro = filtro;
	}

	/**
	 * @see net.gaia.vortex.bluevortex.api.ConexionEnPreparacion#crearConexion()
	 */
	@Override
	public ConexionVortex crearConexion() {
		final ConexionVortex conexion = vortex.crearConexion();
		return conexion;
	}
}
