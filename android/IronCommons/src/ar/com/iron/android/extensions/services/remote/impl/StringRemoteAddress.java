/**
 * 06/03/2013 20:54:46 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.remote.impl;

import android.content.Intent;
import ar.com.iron.android.extensions.services.remote.RemoteServiceAddress;

/**
 * Esta clase representa la direccion de un servicio remoto definido sólo por el string de su nombre
 * 
 * @author D. García
 */
public class StringRemoteAddress implements RemoteServiceAddress {

	private String nombreDelServicio;

	public static StringRemoteAddress create(String nombreDelServicio) {
		StringRemoteAddress direccion = new StringRemoteAddress();
		direccion.nombreDelServicio = nombreDelServicio;
		return direccion;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.RemoteServiceAddress#getAddressIntent()
	 */
	public Intent getAddressIntent() {
		return new Intent(nombreDelServicio);
	}
}
