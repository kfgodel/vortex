/**
 * 07/02/2012 15:45:32 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.spring.SpringConfig;

import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.base.Objects;
import com.tenpines.commons.profile.api.AmbientProfile;
import com.tenpines.integration.spring.SpringBasedConfigurationSupport;

/**
 * Esta clase representa la configuración de la aplicación para el ambiente de desarrollo
 * 
 * @author D. García
 */
public class VortexDevelopmentAmbientConfiguration extends SpringBasedConfigurationSupport {
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	public static VortexDevelopmentAmbientConfiguration create() {
		final VortexDevelopmentAmbientConfiguration config = new VortexDevelopmentAmbientConfiguration();
		return config;
	}

	/**
	 * @see com.tenpines.integration.spring.SpringBasedConfigurationSupport#getSpringProfileName()
	 */
	@Override
	protected String getSpringProfileName() {
		return SpringConfig.DEV_PROFILE;
	}

	/**
	 * @see com.tenpines.integration.spring.SpringBasedConfigurationSupport#adaptContext(org.springframework.context.ConfigurableApplicationContext,
	 *      com.tenpines.commons.profile.api.AmbientProfile)
	 */
	@Override
	public void adaptContext(final ConfigurableApplicationContext applicationContext,
			final AmbientProfile currentProfile) {
		super.adaptContext(applicationContext, currentProfile);
	}

	/**
	 * @see com.tenpines.integration.spring.AmbientConfiguration#getHumanName()
	 */
	@Override
	public String getHumanName() {
		return "desarrollo";
	}
}
