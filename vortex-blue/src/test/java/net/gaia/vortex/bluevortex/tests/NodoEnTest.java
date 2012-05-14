/**
 * 13/05/2012 22:39:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.tests;

/**
 * Esta interfaz es una abstracción de la implementación real de los nodos, para poder expresar los
 * tests sin considerar la API real
 * 
 * @author D. García
 */
public interface NodoEnTest {

	/**
	 * Conecta este nodo con el indicado
	 * 
	 * @param otroNodo
	 *            El nodo a conectar para habilitar las comunicaciones entre ambos
	 */
	void conectarA(NodoEnTest otroNodo);

	/**
	 * Genera el envio a la red del mensaje pasado originándolo desde este nodo
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	void enviar(Object mensaje);

	/**
	 * Verifica que este nodo reciba el mensaje indicado esperando un tiempo prestablecido si aún no
	 * lo recibió
	 * 
	 * @param mensaje
	 *            El mensaje esperado
	 */
	void verificarQueRecibio(Object mensaje);

	/**
	 * Desconecta este nodo, del nodo pasado
	 * 
	 * @param otroNodo
	 *            El nodo a desconectar
	 */
	void desconectarDe(NodoEnTest otroNodo);

	/**
	 * Verifica que este nodo no reciba el mensaje indicado después de esperar un tiempo
	 * considerable
	 * 
	 * @param mensaje
	 *            El mensaje a controlar
	 */
	void verificarQueNoLlego(Object mensaje);

	/**
	 * Declara en este nodo que se enviarán objetos de la clase indicada
	 * 
	 * @param class1
	 *            Clase que indica el tipo de mensajes a enviar
	 */
	void declararEnvioDeInstanciasDe(Class<?> class1);

	/**
	 * Declara en este nodo que solo se aceptan instancias de la clase indicada
	 * 
	 * @param class1
	 *            La clase que indica el tipo de mensajes aceptado
	 */
	void declararRecepcionDeInstanciasDe(Class<?> class1);

	/**
	 * Quita la capacidad de este nodo de discriminar mensajes por filtros de instancia
	 */
	void quitarOperadorDeFiltroParaInstancias();

	/**
	 * Devuelve la cantidad de mensajes que fueron descartados en este nodo después de ser recibidos
	 * 
	 * @return La cantidad acumulada
	 */
	long getCantidadDeMensajesRecibidosYDescartados();

	/**
	 * Agrega en este nodo la posiblidad de discriminar mensajes por filtros de instancia
	 */
	void agregarOperadorDeFiltroParaInstancias();

	/**
	 * Devuelve la cantidad acumulada de mensajes que fueron ruteados por este nodo
	 * 
	 * @return La cantidad de mensajes ruteados
	 */
	long getCantidadDeMensajesRuteados();

	/**
	 * Indica si este nodo está conectado a otros interesados en sus mensajes
	 * 
	 * @return true si este nodo tiene otros que quiere recibir los mensajes que manda
	 */
	boolean tieneReceptores();

	/**
	 * Indica si este nodo está conectado a emisores de mensajes que quiere recibir
	 * 
	 * @return true si está conectado a emisores que mandan el tipo de mensajes que recibe
	 */
	boolean tieneEmisores();

}
