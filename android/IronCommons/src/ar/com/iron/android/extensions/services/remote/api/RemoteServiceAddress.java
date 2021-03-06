/**
 * 06/03/2013 20:21:46 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.api;

import android.content.Intent;

/**
 * Esta interfaz representa una dirección de servicio remoto android para ser accedido por los
 * conectores
 * 
 * @author D. García
 */
public interface RemoteServiceAddress {

	/**
	 * Genera y devuelve el intent que permite describir la dirección para Android y acceder al
	 * servicio
	 * 
	 * @return Esta dirección como intent para ser usado en el bindeo al servicio
	 */
	Intent getAddressIntent();

}
