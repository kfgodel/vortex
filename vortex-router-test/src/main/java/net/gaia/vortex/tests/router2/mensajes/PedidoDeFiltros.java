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
package net.gaia.vortex.tests.router2.mensajes;

import net.gaia.vortex.tests.router2.impl.filtros.FiltroPorStrings;

/**
 * Esta clase representa el mensaje enviado por un nodo para que otro le indique qué filtros debe
 * aplicar para mandarle mensajes
 * 
 * @author D. García
 */
public class PedidoDeFiltros extends MensajeSupport {

	public static PedidoDeFiltros create(final Long idDePataRemota) {
		final PedidoDeFiltros solicitud = new PedidoDeFiltros();
		solicitud.setIdDePataLocalAlReceptor(idDePataRemota);
		return solicitud;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

}
