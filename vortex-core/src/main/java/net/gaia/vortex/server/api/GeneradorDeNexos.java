/**
 * 25/07/2012 17:48:02 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.api;

import net.gaia.vortex.deprecated.NexoViejo;

/**
 * Esta interfaz es aplicable a componentes vortex que por su naturaleza pueden generar nuevos
 * {@link NexoViejo}s. Por ejemplo un servidor de sockets, o un servidor http.<br>
 * Este generador tiene asociada una {@link EstrategiaDeConexionDeNexos} que le indica cómo debe
 * conectar los nuevos nexos generados a la red existente, y como desconectarlos antes de
 * eliminarlos.<br>
 * 
 * @author D. García
 */
public interface GeneradorDeNexos {

	/**
	 * Devuelve la estrategia que se utiliza para conectar y desconectar los nexos de este generador
	 * 
	 * @return La estrategia utilizada para los nuevos nexos
	 */
	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion();

	/**
	 * Establece la nueva estrategia de conexión que se utilizará para conectar los nuevos nexos y
	 * para desconectar los nexos existentes
	 * 
	 * @param estrategia
	 *            La estrategia que reemplaza a la previa completamente
	 */
	public void setEstrategiaDeConexion(EstrategiaDeConexionDeNexos estrategia);

}
