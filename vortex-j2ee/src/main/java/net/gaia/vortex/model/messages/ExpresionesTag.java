/**
 * 20/08/2011 14:26:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.util.StringUtils;

/**
 * Esta clase reune conocimiento sobre las expresiones de destino utilizando tags
 * 
 * @author D. García
 */
public class ExpresionesTag {

	/**
	 * El caracter utiilizado como delimitador de tags
	 */
	public static final String DELIMITADOR_DE_TAGS = ",";

	/**
	 * Devuelve cada uno de los tags contenidos en la expresión pasada
	 * 
	 * @param tagsSeparadosPorComa
	 *            La expresión del mensaje vortex que indica los tags involucrados
	 * @return Cada uno de los tags o un conjunto vacío si no hay ninguno
	 */
	public static Set<String> parsearTagsDe(final String tagsSeparadosPorComa) {
		final Set<String> tags = new LinkedHashSet<String>();
		final StringTokenizer tokenizer = new StringTokenizer(tagsSeparadosPorComa, DELIMITADOR_DE_TAGS);
		while (tokenizer.hasMoreTokens()) {
			final String possibleTag = tokenizer.nextToken();
			final String sinEspacios = possibleTag.trim();
			tags.add(sinEspacios);
		}
		return tags;
	}

	/**
	 * Genera una versión compacta de los tags pasados expresados como cadena separados por coma
	 * 
	 * @param tags
	 *            Los tags que deben utilizarse en la expresión
	 * @return La cadena que representa los tags pasados
	 */
	public static String expresarTagsDe(final Set<String> tags) {
		final String expresado = StringUtils.collectionToDelimitedString(tags, DELIMITADOR_DE_TAGS);
		return expresado;
	}

}
