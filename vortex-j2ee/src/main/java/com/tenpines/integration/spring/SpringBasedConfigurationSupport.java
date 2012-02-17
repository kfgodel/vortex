/**
 * 10/02/2012 10:06:48 Copyright (C) 2011 Darío L. García
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.tenpines.commons.exceptions.FaultyCodeException;
import com.tenpines.commons.profile.api.AmbientProfile;

/**
 * Esta clase representa una configuración de aplicación que está basada en Spring como configurador
 * principal
 * 
 * @author D. García
 */
public abstract class SpringBasedConfigurationSupport implements AmbientConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(SpringBasedConfigurationSupport.class);

	/**
	 * @see com.tenpines.integration.spring.AmbientConfiguration#adaptToAmbient(com.tenpines.commons.profile.api.AmbientProfile)
	 */
	@Override
	public void adaptToAmbient(final AmbientProfile currentProfile) {
		throw new FaultyCodeException(
				"Esta configuración debe recibir un contexto de spring para adaptarse al ambiente");
	}

	/**
	 * Devuelve el nombre del perfil de spring para utilizar en la definición de beans
	 * 
	 * @return El nombre del perfil que se utilizará para la creación de beans de spring
	 */
	protected abstract String getSpringProfileName();

	/**
	 * Modifica el estado general de la aplicación de acuerdo a esta configuración.<br>
	 * Dependiendo de la configuración esta tarea puede involucrar modificar los logs, cambiar el
	 * contexto de spring, etc.
	 * 
	 * @param applicationContext
	 *            Contexto configurable de spring que está siendo inicializado
	 */
	public void adaptContext(final ConfigurableApplicationContext applicationContext,
			final AmbientProfile currentProfile) {
		// Cambiamos el perfil de spring
		final String springProfileName = getSpringProfileName();
		LOG.debug("Activando perfil de Spring: \"{}\"", springProfileName);
		final ConfigurableEnvironment springEnvironment = applicationContext.getEnvironment();
		springEnvironment.setActiveProfiles(springProfileName);
	}

}