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
package net.gaia.vortex.router.impl.messages;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.AtributoPresente;
import net.gaia.vortex.sets.impl.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la respuesta que genera un nodo para indicar el id que le asignó a otro.<br>
 * El id de pata corresponde al ID local del nodo que responde el pedido
 * 
 * @author D. García
 */
public class RespuestaDeIdRemoto extends MensajeBidiSupport {

	public static final String NOMBRE_DE_TIPO = "vortex.id.respuesta";

	private Long idDePataLocalAlEmisor;
	public static final String idDePataLocalAlEmisor_FIELD = "idDePataLocalAlEmisor";

	private Map<String, Object> idDePedido;
	public static final String idDePedido_FIELD = "idDePedido";

	public RespuestaDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getIdDePedido() {
		return idDePedido;
	}

	public void setIdDePedido(final Map<String, Object> idDePedido) {
		this.idDePedido = idDePedido;
	}

	public static RespuestaDeIdRemoto create(final MensajeVortex pedidoOriginal, final Long idLocal) {
		final RespuestaDeIdRemoto name = new RespuestaDeIdRemoto();
		name.setIdDePataLocalAlEmisor(idLocal);
		name.idDePedido = pedidoOriginal.getIdDeMensaje().getAsMap();
		return name;
	}

	public Long getIdDePataLocalAlEmisor() {
		return idDePataLocalAlEmisor;
	}

	public void setIdDePataLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idDePataLocalAlEmisor = idDePataLocalAlEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDePataLocalAlReceptor_FIELD, getIdDePataLocalAlReceptor())
				.con(idDePataLocalAlEmisor_FIELD, idDePataLocalAlEmisor).con(idDePedido_FIELD, idDePedido).toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDeRespuestas = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(nombreDeTipo_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idDePataLocalAlReceptor_FIELD),//
				AtributoPresente.conNombre(idDePataLocalAlEmisor_FIELD),//
				AtributoPresente.conNombre(idDePedido_FIELD)//
				);
		return filtroDeRespuestas;
	}

}
