/**
 * 13/10/2012 10:57:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

import net.gaia.vortex.tests.router.impl.mensajes.ConfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router.impl.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router.impl.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router.impl.mensajes.RespuestaDeIdRemoto;

/**
 * Esta interfaz representa los elementos comunes a portales y routers
 * 
 * @author D. García
 */
public interface Nodo {

	public String getNombre();

	/**
	 * Crea un enlace unidireccional desde este nodo al otro. Sin la contraparte.<br>
	 * Al conectar con el otro se produce una publicacion de filtros al nodo pasado, de manera que
	 * conozca los filtros que se requieren
	 * 
	 * @param otro
	 *            El otro nodo
	 */
	void conectarCon(Nodo otro);

	/**
	 * Agrega el nodo pasado a los destinos sin requerir un paso de simulador
	 * 
	 * @param nodoDestino
	 */
	public void agregarDestino(Nodo nodoDestino);

	/**
	 * Quita el nodo pasado de los destinos sin requerir un paso del simulador
	 * 
	 * @param nodoDestino
	 *            El nodo a quitar
	 */
	void quitarDestino(Nodo nodoDestino);

	/**
	 * Indica si este nodo tiene al pasado como destino
	 * 
	 * @param otro
	 *            El nodo a comprobar
	 * @return true si es parte de los receptores de este nodo
	 */
	boolean tieneComoDestinoA(Nodo otro);

	/**
	 * Crea una conexión bidireccional entre este nodo y el pasado en un solo paso.<br>
	 * Normalmente el otro nodo iniciará la publicación de filtros
	 * 
	 * @param otro
	 *            El otro nodo al que se conectara
	 */
	public void conectarBidi(final Nodo otro);

	/**
	 * Invocado cuando este nodo recibe una publicación de filtros de otro nodo
	 * 
	 * @param publicacion
	 *            La publicación recibida
	 */
	public void recibirPublicacion(PublicacionDeFiltros publicacion);

	/**
	 * Invocado al recibir un pedido de ID desde un nodo
	 * 
	 * @param pedido
	 *            El pedido recibido
	 */
	public void recibirPedidoDeId(PedidoDeIdRemoto pedido);

	/**
	 * Invocado al recibir la respuesta con el ID remoto para una conexión local
	 * 
	 * @param respuesta
	 *            La respuesta que recibimos
	 */
	public void recibirRespuestaDeIdRemoto(RespuestaDeIdRemoto respuesta);

	/**
	 * Fuerza la publicación de los filtros utilizados por este nodo a los nodos vecinos.<br>
	 * Los filtros de este nodo pueden deberse a que son propios (portal) o que son heredados de los
	 * nodos vecinos
	 */
	public void publicarFiltros();

	/**
	 * Invocado cuando este nodo recibe el mensaje final del handshake
	 * 
	 * @param confirmacion
	 *            la confirmacion recibida
	 */
	public void recibirConfirmacionDeIdRemoto(ConfirmacionDeIdRemoto confirmacion);

	/**
	 * Desconecta ambos nodos entre sí
	 * 
	 * @param otroConectado
	 *            el nodo del cual se desconectará
	 */
	void desconectarBidiDe(Nodo otroConectado);

	/**
	 * Desconecta sólo este nodo del otro, si el otro está conectado a este, esa conexión permanece
	 * 
	 * @param destino
	 *            El nodo del cual nos deconectaremos
	 */
	void desconectarUniDe(Nodo destino);

	/**
	 * Recibe el mensaje en este nodo realizando la acción correspondiente segun el tipo de nodo
	 * 
	 * @param mensaje
	 *            El mensaje a recibir
	 */
	public void recibirMensaje(MensajeNormal mensaje);

	/**
	 * Indica si este nodo ya envio o recibio el mensaje pasado, por lo tanto no se le debe pasar de
	 * nuevo
	 * 
	 * @param mensaje
	 *            El mensaje a verificar
	 * @return true si ya se envio o recibio este mensaje
	 */
	public boolean yaProceso(MensajeNormal mensaje);
}
