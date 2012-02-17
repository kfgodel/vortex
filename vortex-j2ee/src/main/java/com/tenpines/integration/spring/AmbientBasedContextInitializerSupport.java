package com.tenpines.integration.spring;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.tenpines.commons.exceptions.UnhandledConditionException;
import com.tenpines.commons.profile.api.AmbientProfile;
import com.tenpines.commons.profile.api.AmbientSensor;
import com.tenpines.commons.profile.api.DetectorConfiguration;
import com.tenpines.commons.profile.api.ProfileDetection;
import com.tenpines.commons.profile.impl.ProfileDetectorImpl;

/**
 * Esta clase inicializa el contexto de spring determinando el perfil de beans que aplica según el
 * ambiente actual detectado
 * 
 * @author D. García
 */
public abstract class AmbientBasedContextInitializerSupport implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final Logger LOG = LoggerFactory.getLogger(AmbientBasedContextInitializerSupport.class);

	/**
	 * @see org.springframework.context.ApplicationContextInitializer#initialize(org.springframework.context.ConfigurableApplicationContext)
	 */
	@Override
	public void initialize(final ConfigurableApplicationContext applicationContext) {
		LOG.debug("Inicializando contexto de Spring según ambiente");

		final AmbientProfile currentProfile = detectCurrentProfile();
		LOG.debug("Perfil de ambiente detectado: \"{}\"", currentProfile.getHumanName());

		final SpringBasedConfigurationSupport ambientConfiguration = getConfigurationFor(currentProfile);
		LOG.debug("Configuración acorde al ambiente: \"{}\"", ambientConfiguration.getHumanName());

		ambientConfiguration.adaptContext(applicationContext, currentProfile);
	}

	/**
	 * Devuelve la configuración de parámetros para el ambiente indicado
	 * 
	 * @param currentProfile
	 *            El perfil de ambiente considerado como actual
	 * @return La configuración de ambiente para configurar el resto de la app
	 */
	protected abstract SpringBasedConfigurationSupport getConfigurationFor(AmbientProfile currentProfile);

	/**
	 * Detecta cual de los ambientes se corresponde al actual utilizando los sensores y perfil
	 * definidos por la subclase
	 * 
	 * @return Detecta el perfil actual
	 * @throws UnhandledConditionException
	 *             Si no se puede determinar el perfil
	 */
	private AmbientProfile detectCurrentProfile() throws UnhandledConditionException {
		final ProfileDetectorImpl detector = ProfileDetectorImpl.create();
		final DetectorConfiguration detectorConfiguration = detector.getConfiguration();

		// Agregamos los sensores
		final List<AmbientSensor> usedSensores = getUsedSensors();
		for (final AmbientSensor usedSensor : usedSensores) {
			detectorConfiguration.addSensor(usedSensor);
		}

		// Agregamos los perfiles
		final Set<AmbientProfile> knownProfiles = getKnownProfiles();
		for (final AmbientProfile knownProfile : knownProfiles) {
			detectorConfiguration.addProfile(knownProfile);
		}

		// Tratamos de determinar cual es el actual
		final ProfileDetection detection = detector.detectProfile();
		if (!detection.getResult().isSuccessful()) {
			throw new UnhandledConditionException("No es posible determinar el ambiente de ejecución actual: "
					+ detection);
		}
		final AmbientProfile currentProfile = detection.getElectedProfile();
		return currentProfile;
	}

	/**
	 * Devuelve el conjunto de perfiles conocidos en esta aplicación
	 * 
	 * @return El conjunto de perfiles de ambientes conocidos
	 */
	protected abstract Set<AmbientProfile> getKnownProfiles();

	/**
	 * Devuelve la lista de los sensores utilizados para detectar
	 * 
	 * @return El conjunto de sensores a utilizar para determinar el ambiente
	 */
	protected abstract List<AmbientSensor> getUsedSensors();
}