/**
 * 21/06/2012 11:24:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito.config;

import ar.com.dgarcia.lang.strings.ToString;

import com.tenpines.commons.profile.api.AmbientSensor;
import com.tenpines.commons.profile.sensors.HostnameSensor;

/**
 * @author D. García Esta clase representa
 */
public class ServerProfile implements ProfileConConfiguracion {

	private String humanName;
	public static final String humanName_FIELD = "humanName";

	private String hostname;
	public static final String hostname_FIELD = "hostname";

	private ContextConfiguration configuration;
	public static final String configuration_FIELD = "configuration";

	private String workingDir;
	public static final String workingDir_FIELD = "workingDir";

	/**
	 * @see com.tenpines.commons.profile.api.AmbientProfile#getValueFor(com.tenpines.commons.profile.api.AmbientSensor)
	 */
	@Override
	public String getValueFor(final AmbientSensor sensor) {
		if (sensor instanceof HostnameSensor) {
			return hostname;
		} else {
			return workingDir;
		}
	}

	/**
	 * @see com.tenpines.commons.profile.api.AmbientProfile#getHumanName()
	 */
	@Override
	public String getHumanName() {
		return humanName;
	}

	/**
	 * @see net.gaia.vortex.server.mosquito.config.ProfileConConfiguracion#getConfig()
	 */
	@Override
	public ContextConfiguration getConfig() {
		return configuration;
	}

	/**
	 * Crea un perfil de ambiente que utiliza la config de desarrollo
	 * 
	 * @param humanName
	 *            El nombre identificador del ambiente
	 * @param hostname
	 *            El host asociado a este ambiente
	 * @return El perfil creado
	 */
	public static ServerProfile create(final String humanName, final String hostname, final String classesDir,
			final ContextConfiguration configuration) {
		final ServerProfile profile = new ServerProfile();
		profile.hostname = hostname;
		profile.humanName = humanName;
		profile.configuration = configuration;
		profile.workingDir = classesDir;
		return profile;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(humanName_FIELD, humanName).con(hostname_FIELD, hostname)
				.con(workingDir_FIELD, workingDir).add(configuration_FIELD, configuration).toString();
	}

}
