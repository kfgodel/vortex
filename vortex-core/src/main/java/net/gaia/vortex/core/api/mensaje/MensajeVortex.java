/**
 * 12/06/2012 22:17:47 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;

/**
 * Esta interfaz representa un mensaje vortex que es información que circula dentro de la red y
 * puede ser procesada, filtrada, transformada, etc por componentes vortex.<br>
 * Puede pensarse un mensaje vortex como un mapa raíz de clave/valor, donde la clave es un texto, y
 * el valor puede ser una primitiva u otro mapa igual (a la JSON). <br>
 * <br>
 * Un mensaje puede ser alterado durante la circulación en la red para rutearlo, pero de manera
 * no-destructiva. Lo que significa que la información original no se pierde y por lo tanto puede
 * ser recibido en destino tal como se envió.
 * 
 * @author D. García
 */
public interface MensajeVortex {

	/**
	 * Devuelve el mapa de datos que contiene este mensaje y que representa un mensaje
	 * 
	 * @return El mapa que representa los datos de este objeto
	 */
	public Map<String, Object> getContenido();

	/**
	 * Devuelve el valor que este mensaje tiene definido como primitiva.<br>
	 * Si este mensaje no representa una primitiva se devuelve null
	 */
	public Object getValorComoPrimitiva();

	/**
	 * Establece en este mensaje el valor que representa como primitiva. Para lo cual en el mapa
	 * interno se guarda la primitiva pasada como un atributo del mapa
	 * 
	 * @param valor
	 *            Un objeto que representa una primitiva de java (números,String, o array de
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
	 * Indica si este mensaje ya tiene en su estado el registro del identificador pasado
	 * 
	 * @param identificador
	 *            El identificador a evaluar en este mensaje
	 * @return true si este mensaje tiene el identificador pasado en la lista de moleculas visitadas
	 */
	public boolean pasoPreviamentePor(IdentificadorVortex identificador);

	/**
	 * Registra en este mensaje el pasaje por un nodo con el ID indicado
	 * 
	 * @param identificador
	 *            El identificador del nodo
	 */
	public void registrarPasajePor(IdentificadorVortex identificador);

}
