/**
 * 14/02/2012 21:47:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.api.config;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.http.externals.http.ConectorHttp;
import net.gaia.vortex.http.externals.http.ConectorHttpNaked;
import ar.dgarcia.http.simple.api.HttpResponseProvider;

import com.google.common.base.Objects;

/**
 * Esta clase representa la configuración del nodo a utilizar cuando no se utiliza encriptación para
 * el envío de mensajes
 * 
 * @author D. García
 */
public class ConfiguracionSinEncriptacion extends ConfiguracionNodoHttpSupport {

	private String urlForNakedMessages;
	public static final String urlForNakedMessages_FIELD = "urlForNakedMessages";

	public static ConfiguracionSinEncriptacion create(final String urlForNakedMessages,
			final HttpResponseProvider optionalHttpResponseProvider) {
		final ConfiguracionSinEncriptacion config = new ConfiguracionSinEncriptacion();
		config.initialize(optionalHttpResponseProvider);
		config.urlForNakedMessages = urlForNakedMessages;
		return config;
	}

	/**
	 * @see net.gaia.vortex.http.api.ConfiguracionDeNodoRemotoHttp#getConectorHttp(net.gaia.vortex.dependencies.json.InterpreteJson)
	 */
	@Override
	public ConectorHttp getConectorHttp(final InterpreteJson interprete) {
		final ConectorHttpNaked conector = ConectorHttpNaked.createNaked(urlForNakedMessages, interprete,
				getHttpResponseProvider());
		return conector;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(urlForNakedMessages_FIELD, urlForNakedMessages)
				.add(maximaCantidadDeSegundosSinActividad_FIELD, getMaximaCantidadDeSegundosSinActividad())
				.add(periodoDePollingEnSegundos_FIELD, getPeriodoDePollingEnSegundos()).toString();
	}

}