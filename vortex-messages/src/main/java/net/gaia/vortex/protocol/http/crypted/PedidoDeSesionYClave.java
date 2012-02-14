/**
 * 13/02/2012 23:28:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.http.crypted;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import com.google.common.base.Objects;

/**
 * Esta clase representa el pedido que realiza el cliente al servidor solicitando una sesión
 * encriptada
 * 
 * @author D. García
 */
public class PedidoDeSesionYClave {

	@NotNull
	@NotEmpty
	private String clavePublicaCliente;
	public static final String clavePublicaCliente_FIELD = "clavePublicaCliente";

	public String getClavePublicaCliente() {
		return clavePublicaCliente;
	}

	public void setClavePublicaCliente(final String clavePublicaCliente) {
		this.clavePublicaCliente = clavePublicaCliente;
	}

	public static PedidoDeSesionYClave create(final String clave) {
		final PedidoDeSesionYClave pedido = new PedidoDeSesionYClave();
		pedido.clavePublicaCliente = clave;
		return pedido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(clavePublicaCliente_FIELD, clavePublicaCliente).toString();
	}
}
