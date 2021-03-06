/**
 * 29/07/2012 00:53:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.api;

import net.gaia.vortex.deprecated.GeneradorDeNexosViejo;
import net.gaia.vortex.http.impl.VortexHttpException;

/**
 * Esta interfaz define el contrato esperado de un servidor http vortex
 * 
 * @author D. García
 */
public interface ServidorDeHttpVortex extends GeneradorDeNexosViejo {
	/**
	 * Inicia el servidor http interno utilizado para aceptar requests con mensajes
	 */
	public abstract void iniciarServidorHttp() throws VortexHttpException;

	/**
	 * Detiene el servidor interno, dejando de aceptar requests y liberando los recursos asociados a
	 * este servidor
	 */
	public abstract void detenerServidor() throws VortexHttpException;
}
