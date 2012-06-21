/**
 * 21/06/2012 10:48:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito;

import java.net.InetSocketAddress;
import java.util.Map.Entry;
import java.util.Set;

import net.gaia.vortex.server.mosquito.config.ContextConfiguration;
import net.gaia.vortex.server.mosquito.config.ProfileConConfiguracion;
import net.gaia.vortex.server.mosquito.config.ServerConfiguration;
import net.gaia.vortex.server.mosquito.config.ServerProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.colecciones.sets.Sets;

import com.tenpines.commons.profile.api.AmbientProfile;
import com.tenpines.commons.profile.api.AmbientSensor;
import com.tenpines.commons.profile.api.DetectorConfiguration;
import com.tenpines.commons.profile.api.ProfileDetection;
import com.tenpines.commons.profile.impl.ProfileDetectorImpl;
import com.tenpines.commons.profile.sensors.HostnameSensor;

/**
 * Esta clase representa el punto de ejecución del servidor mosquito
 * 
 * @author D. García
 */
public class MosquitoMain {
	private static final Logger LOG = LoggerFactory.getLogger(MosquitoMain.class);

	public static MosquitoSever currentServer;

	public static void main(final String[] args) {
		final ContextConfiguration configuration = detectCurrentConfiguration();
		currentServer = MosquitoSever.create(configuration);
		currentServer.aceptarConexiones();
	}

	/**
	 * Detecta el ambiente de ejecución para determinar la configuración adecuada
	 * 
	 * @return La configuración que corresponde al contexto actual
	 */
	private static ContextConfiguration detectCurrentConfiguration() {
		final AmbientProfile currentProfile = detectCurrentProfile();
		final ProfileConConfiguracion profileConConfig = (ProfileConConfiguracion) currentProfile;
		final ContextConfiguration currentConfig = profileConConfig.getConfig();
		return currentConfig;
	}

	/**
	 * Detecta cual de los ambientes se corresponde al actual utilizando los sensores y perfil
	 * definidos por la subclase
	 * 
	 * @return Detecta el perfil actual
	 * @throws UnhandledConditionException
	 *             Si no se puede determinar el perfil
	 */
	private static AmbientProfile detectCurrentProfile() throws UnhandledConditionException {
		final ProfileDetectorImpl detector = ProfileDetectorImpl.create();
		final DetectorConfiguration detectorConfiguration = detector.getConfiguration();

		// Agregamos los sensores
		final HostnameSensor hostNameSensor = HostnameSensor.create();
		detectorConfiguration.addSensor(hostNameSensor);

		// Agregamos los perfiles
		final Set<AmbientProfile> knownProfiles = getKnownProfiles();
		for (final AmbientProfile knownProfile : knownProfiles) {
			detectorConfiguration.addProfile(knownProfile);
		}

		// Tratamos de determinar cual es el actual
		final ProfileDetection detection = detector.detectProfile();
		// Mostramos los valores detectados
		final Set<Entry<AmbientSensor, Object>> detectedValues = detection.detectedValues().entrySet();
		for (final Entry<AmbientSensor, Object> entry : detectedValues) {
			final AmbientSensor sensor = entry.getKey();
			final Object sensedValue = entry.getValue();
			LOG.info("Detectado en sensor \"{}\": [{}]", sensor, sensedValue);
		}
		if (!detection.getResult().isSuccessful()) {
			throw new UnhandledConditionException("No es posible determinar el ambiente de ejecución actual: "
					+ detection);
		}
		final AmbientProfile currentProfile = detection.getElectedProfile();
		return currentProfile;
	}

	/**
	 * Devuelve el conjunto de los perfiles conocidos
	 */
	private static Set<AmbientProfile> getKnownProfiles() {
		final AmbientProfile dario_pc = ServerProfile.create("Desktop Dario (desarrollo)", "Ikari01",
				ServerConfiguration.create(new InetSocketAddress("192.168.1.130", 61616)));
		final AmbientProfile dario_notebook = ServerProfile.create("Notebook Dario (desarrollo)", "ExpeUEW7",
				ServerConfiguration.create(new InetSocketAddress("localhost", 61616)));
		final AmbientProfile mosquito = ServerProfile.create("Server Mosquito (produccion)", "mosquito",
				ServerConfiguration.create(new InetSocketAddress("localhost", 61616)));
		return Sets.newLinkedHashSet(dario_pc, dario_notebook, mosquito);
	}
}
