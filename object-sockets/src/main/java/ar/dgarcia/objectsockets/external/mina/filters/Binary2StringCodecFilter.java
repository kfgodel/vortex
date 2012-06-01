/**
 * Created on: 30/10/2010 23:35:13 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.dgarcia.objectsockets.external.mina.filters;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringDecoder;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringEncoder;

/**
 * Esta clase representa la capa de comunicación via sockets en la que se utiliza la transformación
 * de String a binario y viceversa para el transporte.<br>
 * Se utiliza una cabecera de tamaño fijo para indicar la longitud del mensaje
 * 
 * @author D. García
 */
public class Binary2StringCodecFilter extends ProtocolCodecFilter {

	/**
	 * Longitud máxima de una cadena
	 */
	public static final int MAX_STRING_LENGTH = 50 * 1024 * 1024;
	/**
	 * Cantidad de bytes utilizados para la cabecera
	 */
	private static final int HEADER_LENGTH = 4;
	/**
	 * Encoding utilizado para las comunicaciones
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";
	/**
	 * Encoding para utilizar
	 */
	private static final Charset STRING_CODIFICATION_CHARSET = Charset.forName(DEFAULT_ENCODING);

	/**
	 * Constructor que abstrae la configuración del filtro genérico
	 */
	public Binary2StringCodecFilter() {
		super(new PrefixedStringEncoder(STRING_CODIFICATION_CHARSET, HEADER_LENGTH, MAX_STRING_LENGTH),
				new PrefixedStringDecoder(STRING_CODIFICATION_CHARSET, HEADER_LENGTH, MAX_STRING_LENGTH));
	}

	public static Binary2StringCodecFilter create() {
		final Binary2StringCodecFilter filter = new Binary2StringCodecFilter();
		return filter;
	}
}
