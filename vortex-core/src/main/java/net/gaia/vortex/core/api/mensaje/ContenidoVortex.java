/**
 * 02/07/2012 00:46:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.mensaje;

import java.util.Map;

/**
 * Esta interfaz define la representación del estado de un mensaje vortex como un mapa de primitivas
 * con varios niveles. Es el tipo que representa un contenido JSON, pero agrega algunas operaciones
 * propias de vortex
 * 
 * @author D. García
 */
public interface ContenidoVortex extends Map<String, Object> {
	/**
	 * Key utilizada para guardar el valor de una primitiva como mapa en este mensaje
	 */
	public static final String PRIMITIVA_VORTEX_KEY = "PRIMITIVA_VORTEX_KEY";

	/**
	 * Clave usada para agregar como dato el nombre completo de la clase a partir de la cual se
	 * origina este mensaje
	 */
	public static final String CLASSNAME_KEY = "CLASSNAME_KEY";

	/**
	 * Establece el nombre que identifica el tipo de mensaje original como parte de los datos del
	 * contenido de este mensaje
	 * 
	 * @param nombreDeClaseCompleto
	 *            El nombre completo de la clase concreta para ser utilizado en las condiciones
	 */
	public void setNombreDelTipoOriginal(String nombreDeClaseCompleto);

	/**
	 * Devuelve el nombre completo de la clase a partir de la cual se generó este mensaje si está
	 * disponible como parte del contenido
	 * 
	 * @return El nombre completo de la clase original, o null si no existe ese dato en este mensaje
	 */
	public String getNombreDelTipoOriginal();

	/**
	 * Devuelve el valor que este contenido tiene definido como primitiva.<br>
	 * El valor como primitiva está registrado bajo la key {@link #PRIMITIVA_VORTEX_KEY}.<br>
	 * Si este contenido no representa una primitiva se devuelve null
	 */
	public Object getValorComoPrimitiva();

	/**
	 * Establece en este contenido el valor que representa como primitiva. Para lo cual se utiliza
	 * una clave especial reservada {@link #PRIMITIVA_VORTEX_KEY}
	 * 
	 * @param valor
	 *            Un objeto que representa una primitiva a la JSON (números, String, o array de
	 *            primitiva)
	 * @throws IllegalArgumentException
	 *             Si el objeto pasado no es un primitiva
	 */
	public void setValorComoPrimitiva(Object valor);

	/**
	 * Indica si este mensaje tiene definida la key para valor de primitiva
	 * 
	 * @return true si debe considerarse el contenido de este mensaje como una primitiva
	 */
	public boolean tieneValorComoPrimitiva();

}
