/**
 * 13/10/2012 21:19:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.mensajes;

import net.gaia.vortex.tests.router.impl.patas.filtros.FiltroPorStrings;

/**
 * Esta clase representa el mensaje enviado por un nodo para conocer el id con el que otro nodo lo
 * conoce
 * 
 * @author D. García
 */
public class PedidoDeIdRemoto extends MensajeSupport {

	private Long idDePata;

	public Long getIdDePata() {
		return idDePata;
	}

	public void setIdDePata(final Long idDePata) {
		this.idDePata = idDePata;
	}

	public static PedidoDeIdRemoto create(final Long idDePataLocal) {
		final PedidoDeIdRemoto name = new PedidoDeIdRemoto();
		name.idDePata = idDePataLocal;
		return name;
	}

	/**
	 * @see net.gaia.vortex.tests.router.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}
}
