/**
 * 
 */
package net.gaia.vortex.router.impl.sets.converter;

/**
 * Esta clase reune información de la conversion de mapas a condiciones
 * 
 * @author kfgodel
 */
public class MetadataDeCondiciones {

	public static final String ATRIBUTO_TIPO = "tipo";
	
	public static class TipoAnd {
		public static final String TIPO_AND = "AND";
		public static final String ATRIBUTO_FILTROS = "filtros";
	}
	

	public static class TipoOr {
		public static final String TIPO_OR = "OR";
		public static final String ATRIBUTO_FILTROS = "filtros";
	}

	public static class TipoNot {
		public static final String TIPO_NOT = "NOT";
		public static final String ATRIBUTO_FILTROS = "filtro";
	}

	public static class TipoClaveValor {
		public static final String TIPO_CLAVE_VALOR = "CLAVE_VALOR";
		public static final String ATRIBUTO_CLAVE = "clave";
		public static final String ATRIBUTO_VALOR = "valor";
	}


}
