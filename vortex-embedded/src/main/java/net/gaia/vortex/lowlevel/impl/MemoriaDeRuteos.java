/**
 * 21/01/2012 03:22:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta interfaz representa la memoria del nodo respecto de ruteos activos
 * 
 * @author D. García
 */
public interface MemoriaDeRuteos {

	/**
	 * Registra en esta memoria que existe un ruteo en progreso para el ID de envío recibido
	 * 
	 * @param idEnvioRecibido
	 *            El identificador del envío que recibimos como nodo
	 */
	void registrarRuteoActivo(IdentificadorDeEnvio idEnvioRecibido);

	/**
	 * Elimina de esta memoria el ruteo como activo
	 * 
	 * @param idEnvioRecibido
	 *            El identificador del envío en proceso de ruteo
	 */
	public abstract void registrarRuteoTerminado(IdentificadorDeEnvio idEnvioRecibido);

	/**
	 * Indica si existe un ruteo activo para el Identificador de envio indicado
	 * 
	 * @param idEnvioRealizado
	 * @return
	 */
	public abstract boolean tieneRuteoActivoPara(IdentificadorDeEnvio idEnvioRealizado);

	/**
	 * Elimina todos los ruteos registrados para el receptor indicado
	 * 
	 * @param receptorCerrado
	 *            El receptor a eliminar
	 */
	void eliminarRuteosActivosPara(ReceptorVortex receptorCerrado);

}