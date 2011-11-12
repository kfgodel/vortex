/**
 * 20/08/2011 13:35:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.servidor.localizadores;

import net.gaia.vortex.persistibles.ReceptorHttp;

/**
 * Esta interfaz representa un localizador que sabe como obtener la instancia del receptor que
 * genera la comunicación (emisor del mensaje, o receptor según corresponda)
 * 
 * @author D. García
 */
public interface ReferenciaAReceptor {

	/**
	 * @return
	 */
	ReceptorHttp localizar();

	/**
	 * Reemplaza la lógica de este localizador por el pasado
	 * 
	 * @param delegate
	 */
	void reemplazarPor(ReferenciaAReceptor delegate);

	/**
	 * Indica si el receptor de esta referencia es identificable en la base
	 * 
	 * @return false si no tiene ID
	 */
	boolean esIdentificableEnLaBase();

}
