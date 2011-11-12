/**
 * 01/11/2011 00:17:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages.meta;

import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.persistibles.ReceptorHttp;

/**
 * Esta interrfaz es el contrato que respetan los metamensajes aplicados a los tags de un receptor
 * 
 * @author D. García
 */
public interface MetamensajeSobreTags {

	/**
	 * Realiza la acción representada por este metamensaje sobre el receptor pasado
	 * 
	 * @param receptorHttp
	 *            El receptor a modificar en base al metamensaje recibido
	 */
	void modificarTagsInteresantesPara(ReceptorHttp receptorHttp);

	/**
	 * Devuelve el tipo de metamensaje correspondiente a este metamensaje
	 */
	TipoMetaMensaje getTipoDeMetamensaje();

	/**
	 * Modifica el estado de los tags notificados al receptor pasado
	 * 
	 * @param receptorHttp
	 *            El receptor que se actualizar con los cambios notificados
	 */
	void modificarTagsNotificadosA(ReceptorHttp receptorHttp);

}
