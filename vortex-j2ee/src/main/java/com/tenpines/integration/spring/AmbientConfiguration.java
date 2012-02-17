/**
 * 07/02/2012 14:22:56 Copyright (C) 2011 Darío L. García
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
package com.tenpines.integration.spring;

import com.tenpines.commons.profile.api.AmbientProfile;

/**
 * Esta interfaz representa una configuración de parámetros de la aplicación aplicable en ciertos
 * ambientes.<br>
 * A través de las subclases de esta interfaz se puede armar una jerarquía de parámetros
 * reutilizables de configuraciones
 * 
 * @author D. García
 */
public interface AmbientConfiguration {

	/**
	 * Modifica el estado general de la aplicación de acuerdo a esta configuración y el ambiente
	 * actual.<br>
	 * 
	 * @param currentProfile
	 *            El ambiente detectado como actual por si implica alguna variación de esta
	 *            configuración
	 */
	void adaptToAmbient(AmbientProfile currentProfile);

	/**
	 * Devuelve el nombre humano que identifica esta configuración
	 * 
	 * @return El nombre de la configuración
	 */
	String getHumanName();

}
