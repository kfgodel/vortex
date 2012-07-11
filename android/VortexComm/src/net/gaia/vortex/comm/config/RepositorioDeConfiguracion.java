/**
 * 09/07/2012 17:59:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.config;

import android.content.Context;
import ar.com.iron.android.extensions.preferences.PreferencesConfiguration;
import ar.dgarcia.textualizer.json.JsonTextualizer;

/**
 * Esta clase representa la configuración persistente en un archivo de preferences para la
 * aplicación
 * 
 * @author D. García
 */
public class RepositorioDeConfiguracion extends PreferencesConfiguration {

	private static final String CONFIGURACION_SERIALIZADA_KEY = "CONFIGURACION_SERIALIZADA";

	private static final String CONFIG_FILE_NAME = "vortexcomm";

	private final JsonTextualizer textualizer;

	private ConfiguracionVortexComm configuracionActual;

	public RepositorioDeConfiguracion(Context context) {
		super(context, CONFIG_FILE_NAME);
		textualizer = JsonTextualizer.createWithoutTypeMetadata();
	}

	public void setConfiguracion(ConfiguracionVortexComm nuevaConfiguracion) {
		configuracionActual = nuevaConfiguracion;
	}

	public ConfiguracionVortexComm getConfiguracion() {
		if (configuracionActual == null) {
			configuracionActual = cargarConfiguracion();
		}
		return configuracionActual;
	}

	/**
	 * Carga la configuración actual deserializandola del texto o creando una nueva si no existe
	 * previamente
	 * 
	 * @return La configuración cargada
	 */
	private ConfiguracionVortexComm cargarConfiguracion() {
		String configuracionComoJson = getPreferences().getString(CONFIGURACION_SERIALIZADA_KEY, null);
		if (configuracionComoJson == null) {
			// No existe version previa
			return ConfiguracionVortexComm.create();
		}
		ConfiguracionVortexComm configuracion = textualizer.convertFromStringAs(ConfiguracionVortexComm.class,
				configuracionComoJson);
		return configuracion;
	}

	/**
	 * @see ar.com.iron.android.extensions.preferences.PreferencesConfiguration#saveChanges()
	 */
	@Override
	public void saveChanges() {
		if (configuracionActual == null) {
			// No hubo cambios
			return;
		}
		String configuracionComoJson = textualizer.convertToString(configuracionActual);
		getPreferenceEditor().putString(CONFIGURACION_SERIALIZADA_KEY, configuracionComoJson);
		super.saveChanges();
	}

}
