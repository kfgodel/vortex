/**
 * 20/06/2012 16:21:34 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.strings;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Esta clase permite implementar el toString de una clase basándose en el string de otras
 * instancias pero a la vez contemplando la posible recursividad de las llamadas
 * 
 * @author D. García
 */
public class ToString {

	/**
	 * Texto usado para representar null
	 */
	private static final String REPRESENTACION_PARA_NULL = "null";

	/**
	 * Mapa de referencias para no repetir las instancias e identificarlas
	 */
	private static ThreadLocal<IdentityHashMap<Object, Integer>> referenciasDelThread = new ThreadLocal<IdentityHashMap<Object, Integer>>();

	private static IdentityHashMap<Object, Integer> getIdsPorObjeto() {
		return referenciasDelThread.get();
	}

	/**
	 * Objeto a describir como String por este helper
	 */
	private Object root;

	/**
	 * Atributos adicionales y opcionales al objeto
	 */
	private LinkedHashMap<String, Object> atributos;

	/**
	 * Crea un helper para crear una descripción en string del objeto pasado
	 * 
	 * @param rootObject
	 *            El objeto a describir
	 * @return El helper a utilizar para la descripción
	 */
	public static ToString de(final Object rootObject) {
		final ToString helper = new ToString();
		helper.root = rootObject;
		return helper;
	}

	private Map<String, Object> safeGetAtributos() {
		if (atributos == null) {
			atributos = new LinkedHashMap<String, Object>();
		}
		return atributos;
	}

	/**
	 * Establece el valor de un atributo a incluir en la descripción del objeto
	 * 
	 * @param atributo
	 *            El atributo a describir
	 * @param valor
	 *            El valor del atributo
	 * @return Esta instancia para encadenar llamadas
	 */
	public ToString add(final String atributo, final Object valor) {
		return con(atributo, valor);
	}

	/**
	 * Establece el valor de un atributo a incluir en la descripción del objeto
	 * 
	 * @param atributo
	 *            El atributo a describir
	 * @param valor
	 *            El valor del atributo
	 * @return Esta instancia para encadenar llamadas
	 */
	public ToString con(final String atributo, final Object valor) {
		safeGetAtributos().put(atributo, valor);
		return this;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		final boolean esLlamadaRecursiva = referenciasDelThread.get() != null;
		if (!esLlamadaRecursiva) {
			// Si es la primera llamada podemos definir el nuevo sistema de referencias
			referenciasDelThread.set(new IdentityHashMap<Object, Integer>());
		}
		try {
			final String representacion = representarInstancia();
			return representacion;
		} finally {
			if (!esLlamadaRecursiva) {
				// Si es la primera llamada entonces podemos quitar el sistema de referencias cuando
				// terminamos
				referenciasDelThread.remove();
			}
		}
	}

	/**
	 * Representa la instancia actual como texto
	 * 
	 * @return La representación textual de la instancia indicada como root
	 */
	private String representarInstancia() {
		if (root == null) {
			return REPRESENTACION_PARA_NULL;
		}
		// La descripción incluye el nombre de clase
		final StringBuilder builder = new StringBuilder(root.getClass().getSimpleName());
		final boolean teniaIdAsignado = describirRoot(builder);

		// Si ya tenía ID omitimos los atributos para evitar la recursividad
		if (atributos != null) {
			if (!teniaIdAsignado) {
				representarAtributosEn(builder, atributos);
			} else {
				builder.append("<-");
			}
		}
		final String representacion = builder.toString();
		return representacion;
	}

	/**
	 * Realiza una descripción de root en el builder pasado
	 * 
	 * @param builder
	 *            El builder que recibirá la descripción del objeto root
	 * @return true si el root ya tenía ID previo
	 */
	private boolean describirRoot(final StringBuilder builder) {
		// El id de instancia en esta descripción
		final IdentityHashMap<Object, Integer> idsPorObjeto = getIdsPorObjeto();
		Integer idDelRoot = idsPorObjeto.get(root);
		final boolean teniaIdAsignado = idDelRoot != null;
		if (!teniaIdAsignado) {
			// Es la primera vez que lo vemos, le asignamos un nuevo ID
			idDelRoot = getIdsPorObjeto().size();
			getIdsPorObjeto().put(root, idDelRoot);
		}
		builder.append("(");
		builder.append(idDelRoot);
		builder.append(")");
		return teniaIdAsignado;
	}

	/**
	 * Agrega un representación para los atributos pasados en el builder indicado
	 * 
	 * @param builder
	 *            El builder en el que agregar los atributos
	 * @param atributos
	 *            Los atributos a incorporar
	 */
	private void representarAtributosEn(final StringBuilder builder, final LinkedHashMap<String, Object> atributos) {
		builder.append("{");
		final Iterator<Entry<String, Object>> entries = atributos.entrySet().iterator();
		while (entries.hasNext()) {
			final Entry<String, Object> entry = entries.next();
			final String nombreAtributo = entry.getKey();
			builder.append(nombreAtributo);
			builder.append("=");
			final Object valor = entry.getValue();
			final String valorComoTexto = representarValor(valor);
			builder.append(valorComoTexto);
			if (entries.hasNext()) {
				builder.append(", ");
			}
		}
		builder.append("}");
	}

	/**
	 * Devuelve una representación del objeto como texto tomando en cuenta la posible recursividad
	 * de esta acción
	 * 
	 * @param valor
	 *            El valor a representar
	 * @return La representación como texto
	 */
	private String representarValor(final Object valor) {
		return String.valueOf(valor);
	}
}
