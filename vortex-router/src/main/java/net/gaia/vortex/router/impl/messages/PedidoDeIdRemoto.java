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
package net.gaia.vortex.router.impl.messages;

/**
 * Esta clase representa el mensaje enviado por un nodo para conocer el id con el que otro nodo lo
 * conoce.<br>
 * El id de pata del pedido corresponde al ID local del nodo que manda el pedido
 * 
 * @author D. García
 */
public class PedidoDeIdRemoto extends MensajeBidiSupport {

	public static final String NOMBRE_DE_TIPO = "vortex.id.pedido";

	public PedidoDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	private Long idDePataLocalAlEmisor;
	public static final String idDePataLocalAlEmisor_FIELD = "idDePataLocalAlEmisor";

	public Long getIdDePataLocalAlEmisor() {
		return idDePataLocalAlEmisor;
	}

	public void setIdDePataLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idDePataLocalAlEmisor = idDePataLocalAlEmisor;
	}

	public static PedidoDeIdRemoto create(final Long idDePataLocal) {
		final PedidoDeIdRemoto pedido = new PedidoDeIdRemoto();
		pedido.setIdDePataLocalAlEmisor(idDePataLocal);
		return pedido;
	}

}
