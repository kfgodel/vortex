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
package net.gaia.vortex.tests.router2.mensajes;

import net.gaia.vortex.tests.router2.impl.filtros.FiltroPorStrings;

/**
 * Esta clase representa la respuesta que genera un nodo para indicar el id que le asignó a otro.<br>
 * El id de pata corresponde al ID local del nodo que responde el pedido
 * 
 * @author D. García
 */
public class RespuestaDeIdRemoto extends MensajeSupport {

	private Long idDePataLocalAlEmisor;
	public static final String idDePataLocalAlEmisor_FIELD = "idDePataLocalAlEmisor";

	private PedidoDeIdRemoto pedido;

	public PedidoDeIdRemoto getPedido() {
		return pedido;
	}

	public void setPedido(final PedidoDeIdRemoto pedido) {
		this.pedido = pedido;
	}

	public static RespuestaDeIdRemoto create(final PedidoDeIdRemoto pedido, final Long idLocal) {
		final RespuestaDeIdRemoto name = new RespuestaDeIdRemoto();
		name.pedido = pedido;
		name.setIdDePataLocalAlEmisor(idLocal);
		return name;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

	public Long getIdDePataLocalAlEmisor() {
		return idDePataLocalAlEmisor;
	}

	public void setIdDePataLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idDePataLocalAlEmisor = idDePataLocalAlEmisor;
	}

}
