/**
 * 23/01/2013 09:52:08 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.memoria;

import java.util.Map;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una memoria de mensajes que puede reconstruir los mensajes que recuerda
 * 
 * @author D. García
 */
public class MemoriaDePedidosDeId {

	private Map<String, Object> ultimoIdPedido;
	public static final String ultimoIdPedido_FIELD = "ultimoIdPedido";

	/**
	 * Crea una nueva memoria limitada a la cantidad de mensajes indicados.<br>
	 * Los mensajes más accedidos serán conservados y los menos descartados al superar el tamaño
	 * máximo
	 * 
	 * @param cantidadMaximaRegistrada
	 *            El tamaño que esta memoria puede tener como máximo de mensajes registrados
	 * @return La memoria creada
	 */
	public static MemoriaDePedidosDeId create() {
		final MemoriaDePedidosDeId memoria = new MemoriaDePedidosDeId();
		return memoria;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(ultimoIdPedido_FIELD, ultimoIdPedido).toString();
	}

	/**
	 * Indica si esta memoria registra el id indicado
	 * 
	 * @param idDePedidoComoMapa
	 *            El id a verificar
	 * @return true si en esta memoria se registro el id indicado
	 */
	public boolean tieneRegistroDelPedido(final Map<String, Object> idDePedidoComoMapa) {
		final boolean esElUltimoPedido = idDePedidoComoMapa.equals(ultimoIdPedido);
		return esElUltimoPedido;
	}

	/**
	 * Registra en esta memoria el pedido realizado para posteriores respuestas
	 * 
	 * @param idDeMensajeComoMapa
	 *            El identificador del pedido
	 */
	public void registrarPedidoConId(final Map<String, Object> idDeMensajeComoMapa) {
		ultimoIdPedido = idDeMensajeComoMapa;
	}

	/**
	 * Devuelve el id del último pedido registrado
	 * 
	 * @return EL mapa del id o null si no se registró ninguno aún
	 */
	public Map<String, Object> getIdDeultimoPedido() {
		return ultimoIdPedido;
	}
}
