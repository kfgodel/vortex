/**
 * 23/01/2013 10:21:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.condiciones;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.impl.messages.bidi.RespuestaDeIdRemoto;
import net.gaia.vortex.router.impl.moleculas.memoria.MemoriaDePedidosDeId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que evalua si un mensaje representa una respuesta para esta
 * pata
 * 
 * @author D. García
 */
public class EsRespuestaParaEstaPata implements Condicion {
	private static final Logger LOG = LoggerFactory.getLogger(EsRespuestaParaEstaPata.class);

	private MemoriaDePedidosDeId memoria;

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final Object idDePedido = mensaje.getContenido().get(RespuestaDeIdRemoto.idPedidoOriginal_FIELD);
		if (!(idDePedido instanceof Map)) {
			LOG.error("Recibimos una respuesta de id[" + mensaje + "] cuyo id de pedido no es un mapa?: " + idDePedido
					+ ". Asumiendo que no fue pedido por la pata");
			return ResultadoDeCondicion.FALSE;
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> idDePedidoComoMapa = (Map<String, Object>) idDePedido;
		final boolean pedidoHechoPorLaPata = memoria.tieneRegistroDelPedido(idDePedidoComoMapa);
		return ResultadoDeCondicion.paraBooleano(pedidoHechoPorLaPata);
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	@Override
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	public static EsRespuestaParaEstaPata create(final MemoriaDePedidosDeId memoriaDeLaPata) {
		final EsRespuestaParaEstaPata condicion = new EsRespuestaParaEstaPata();
		condicion.memoria = memoriaDeLaPata;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con("ultimoId", memoria.getIdDeultimoPedido()).toString();
	}

}
