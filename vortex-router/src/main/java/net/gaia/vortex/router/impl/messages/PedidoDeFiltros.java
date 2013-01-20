/**
 * 09/12/2012 13:25:32 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.AtributoPresente;
import net.gaia.vortex.sets.impl.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el mensaje enviado por un nodo para que otro le indique qué filtros debe
 * aplicar para mandarle mensajes
 * 
 * @author D. García
 */
public class PedidoDeFiltros extends MensajeBidiSupport {

	public static final String NOMBRE_DE_TIPO = "vortex.filtro.pedido";

	public PedidoDeFiltros() {
		super(NOMBRE_DE_TIPO);
	}

	public static PedidoDeFiltros create(final Long idDePataRemota) {
		final PedidoDeFiltros solicitud = new PedidoDeFiltros();
		solicitud.setIdDePataLocalAlReceptor(idDePataRemota);
		return solicitud;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDePataLocalAlReceptor_FIELD, getIdDePataLocalAlReceptor()).toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDePedidos = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(nombreDeTipo_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idDePataLocalAlReceptor_FIELD)//
				);
		return filtroDePedidos;
	}
}
