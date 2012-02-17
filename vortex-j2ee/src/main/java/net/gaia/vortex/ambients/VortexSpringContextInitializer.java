/**
 * 07/02/2012 15:22:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.ambients;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tenpines.commons.exceptions.UnhandledConditionException;
import com.tenpines.commons.profile.api.AmbientProfile;
import com.tenpines.commons.profile.api.AmbientSensor;
import com.tenpines.commons.profile.sensors.HostnameSensor;
import com.tenpines.commons.profile.support.AmbientProfileSupport;
import com.tenpines.integration.spring.AmbientBasedContextInitializerSupport;
import com.tenpines.integration.spring.SpringBasedConfigurationSupport;

/**
 * Esta clase es el componente de la aplicación que se encarga de inicializar la configuración de
 * acuerdo al ambiente actual
 * 
 * @author D. García
 */
public class VortexSpringContextInitializer extends AmbientBasedContextInitializerSupport {

	/**
	 * Este perfil corresponde al ambiente de desarrollo de la casa de Dario
	 */
	public static AmbientProfileSupport DEV_DARIO;
	public static AmbientProfileSupport PROD_MOSQUITO;

	private static final AmbientSensor HOSTNAME = HostnameSensor.create();

	private HashMap<AmbientProfile, SpringBasedConfigurationSupport> configurationsPerProfile;

	/**
	 * Invocado por spring al crear esta instancia
	 */
	public VortexSpringContextInitializer() {
		init();
	}

	/**
	 * Inicializa el estado de esta intancia
	 */
	private void init() {
		DEV_DARIO = AmbientProfileSupport.create("Desktop Dario (desarrollo)");
		DEV_DARIO.setValueFor(HOSTNAME, "Ikari01");
		PROD_MOSQUITO = AmbientProfileSupport.create("Server Mosquito (produccion)");
		PROD_MOSQUITO.setValueFor(HOSTNAME, "mosquito");

		configurationsPerProfile = new HashMap<AmbientProfile, SpringBasedConfigurationSupport>();
		configurationsPerProfile.put(DEV_DARIO, VortexDevelopmentAmbientConfiguration.create());
		configurationsPerProfile.put(PROD_MOSQUITO, VortexProductionAmbientConfiguration.create());
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	/**
	 * @see com.tenpines.integration.spring.AmbientBasedContextInitializerSupport#getConfigurationFor(com.tenpines.commons.profile.api.AmbientProfile)
	 */
	@Override
	protected SpringBasedConfigurationSupport getConfigurationFor(final AmbientProfile currentProfile) {
		final SpringBasedConfigurationSupport ambientConfiguration = configurationsPerProfile.get(currentProfile);
		if (ambientConfiguration == null) {
			throw new UnhandledConditionException("No se conoce la configuración de ambiente para el perfil: "
					+ currentProfile);
		}
		return ambientConfiguration;
	}

	/**
	 * @see com.tenpines.integration.spring.AmbientBasedContextInitializerSupport#getKnownProfiles()
	 */
	@Override
	protected Set<AmbientProfile> getKnownProfiles() {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		final Set<AmbientProfile> profiles = (Set) Sets.newHashSet(DEV_DARIO, PROD_MOSQUITO);
		return profiles;
	}

	/**
	 * @see com.tenpines.integration.spring.AmbientBasedContextInitializerSupport#getUsedSensors()
	 */
	@Override
	protected List<AmbientSensor> getUsedSensors() {
		return Lists.newArrayList(HOSTNAME);
	}

}
