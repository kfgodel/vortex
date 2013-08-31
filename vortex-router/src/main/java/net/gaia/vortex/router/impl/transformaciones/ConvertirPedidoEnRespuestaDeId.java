/**
 * 22/01/2013 19:31:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.transformaciones;

import java.util.Map;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.mensajes.ContenidoVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.router.impl.messages.bidi.RespuestaDeIdRemoto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que al recibir un pedido de ID lo convierte en una
 * respuesta de ID para ser enviada
 * 
 * @author D. García
 */
@Paralelizable
public class ConvertirPedidoEnRespuestaDeId implements Transformacion {
	private static final Logger LOG = LoggerFactory.getLogger(ConvertirPedidoEnRespuestaDeId.class);

	private Long idLocalDePata;
	public static final String idLocalDePata_FIELD = "idLocalDePata";

	private ConversorDeMensajesVortex mapeador;

	/**
	 * @see net.gaia.vortex.api.transformaciones.Transformacion#transformar(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public MensajeVortex transformar(final MensajeVortex pedido) {
		final ContenidoVortex contenidoDelPedido = pedido.getContenido();
		final Object valorDelId = contenidoDelPedido.get(ContenidoVortex.ID_DE_MENSAJE_KEY);
		if (!(valorDelId instanceof Map)) {
			throw new UnhandledConditionException("El id[" + valorDelId + "] en el contenido del pedido recibido["
					+ pedido + "] no es un mapa?");
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> idDePedidoOriginal = (Map<String, Object>) valorDelId;
		final RespuestaDeIdRemoto respuesta = RespuestaDeIdRemoto.create(idDePedidoOriginal, idLocalDePata);
		final MensajeVortex respuestaEnviable = mapeador.convertirAVortex(respuesta);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug("Enviando respuesta[{}] al pedido[{}] recibido", respuesta, pedido.toShortString());
		}
		return respuestaEnviable;
	}

	public static ConvertirPedidoEnRespuestaDeId create(final Long idDePata, final ConversorDeMensajesVortex mapeador) {
		final ConvertirPedidoEnRespuestaDeId responder = new ConvertirPedidoEnRespuestaDeId();
		responder.idLocalDePata = idDePata;
		responder.mapeador = mapeador;
		return responder;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(idLocalDePata_FIELD, idLocalDePata).toString();
	}

}
