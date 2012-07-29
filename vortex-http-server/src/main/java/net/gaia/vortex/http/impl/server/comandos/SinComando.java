/**
 * 25/07/2012 13:42:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server.comandos;

import net.gaia.vortex.http.external.jetty.ComandoHttp;
import net.gaia.vortex.http.external.jetty.RespuestaHttp;
import net.gaia.vortex.http.impl.server.respuestas.RespuestaDeErrorDeCliente;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comando http tomado cuando no se puede interpretar el comando solicitado
 * 
 * @author D. García
 */
public class SinComando implements ComandoHttp {

	private String urlUsada;
	public static final String urlUsada_FIELD = "urlUsada";

	/**
	 * @see net.gaia.vortex.http.external.jetty.ComandoHttp#ejecutar()
	 */
	@Override
	public RespuestaHttp ejecutar() {
		return RespuestaDeErrorDeCliente.create("No existe comando para la URL: " + urlUsada);
	}

	public static SinComando create(final String urlUsada) {
		final SinComando comando = new SinComando();
		comando.urlUsada = urlUsada;
		return comando;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(urlUsada_FIELD, urlUsada).toString();
	}

}
