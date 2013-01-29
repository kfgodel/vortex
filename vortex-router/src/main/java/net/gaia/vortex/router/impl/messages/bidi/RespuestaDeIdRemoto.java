/**
 * 13/10/2012 21:33:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.messages.bidi;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.impl.messages.meta.MetamensajeSupport;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la respuesta que genera un nodo para indicar el id que le asignó a otro.<br>
 * El id de pata corresponde al ID local del nodo que responde el pedido
 * 
 * @author D. García
 */
public class RespuestaDeIdRemoto extends MetamensajeSupport {

	public static final String NOMBRE_DE_TIPO = PREFIJO_METAMENSAJE + "IdRemoto.Respuesta";

	private Long idLocalAlEmisor;
	public static final String idLocalAlEmisor_FIELD = "idLocalAlEmisor";

	private Map<String, Object> idPedidoOriginal;
	public static final String idPedidoOriginal_FIELD = "idPedidoOriginal";

	public RespuestaDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getIdPedidoOriginal() {
		return idPedidoOriginal;
	}

	public void setIdPedidoOriginal(final Map<String, Object> idDePedido) {
		this.idPedidoOriginal = idDePedido;
	}

	public static RespuestaDeIdRemoto create(final Map<String, Object> idPedidoOriginal, final Long idLocal) {
		final RespuestaDeIdRemoto name = new RespuestaDeIdRemoto();
		name.setIdLocalAlEmisor(idLocal);
		name.idPedidoOriginal = idPedidoOriginal;
		return name;
	}

	public Long getIdLocalAlEmisor() {
		return idLocalAlEmisor;
	}

	public void setIdLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idLocalAlEmisor = idDePataLocalAlEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocalAlEmisor_FIELD, idLocalAlEmisor)
				.con(idPedidoOriginal_FIELD, idPedidoOriginal).toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDeRespuestas = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(tipoDeMensaje_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idLocalAlEmisor_FIELD),//
				AtributoPresente.conNombre(idPedidoOriginal_FIELD)//
				);
		return filtroDeRespuestas;
	}

}
