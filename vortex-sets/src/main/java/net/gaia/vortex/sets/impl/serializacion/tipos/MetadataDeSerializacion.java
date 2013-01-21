/**
 * 21/01/2013 16:00:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos;

/**
 * Esta clase reune las constantes de los nombres y tipos publicos para las condiciones
 * 
 * @author D. García
 */
public class MetadataDeSerializacion {

	public final static String ATRIBUTO_TIPO = "tipo";

	public final static String TIPO_ANONIMO = "ANONIMO";
	public final static String TIPO_ANONIMO_CLASE = "clase";

	public final static String TIPO_AND = "AND";
	public final static String TIPO_AND_FILTROS = "filtros";

	public final static String TIPO_OR = "OR";
	public final static String TIPO_OR_FILTROS = "filtros";

	public final static String TIPO_NOT = "NOT";
	public final static String TIPO_NOT_FILTRO = "filtro";

	public final static String TIPO_EQUALS = "EQ";
	public final static String TIPO_EQUALS_CLAVE = "clave";
	public final static String TIPO_EQUALS_VALOR = "valor";

	public final static String TIPO_TRUE = "TRUE";
	public final static String TIPO_FALSE = "FALSE";

	public final static String TIPO_EMPIEZA = "EMPIEZA";
	public final static String TIPO_EMPIEZA_CLAVE = "clave";
	public final static String TIPO_EMPIEZA_PREFIJO = "prefijo";

	public final static String TIPO_PRESENTE = "PRESENTE";
	public final static String TIPO_PRESENTE_CLAVE = "clave";

	public final static String TIPO_CONTIENE = "CONTIENE";
	public final static String TIPO_CONTIENE_CLAVE = "clave";
	public final static String TIPO_CONTIENE_VALOR = "valor";

	public final static String TIPO_REGEX = "PERL_REGEX";
	public final static String TIPO_REGEX_CLAVE = "clave";
	public final static String TIPO_REGEX_EXPRESION = "expresion";

}
