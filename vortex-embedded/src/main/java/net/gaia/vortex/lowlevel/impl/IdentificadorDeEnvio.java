/**
 * 28/11/2011 12:59:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.IdVortex;

import com.google.common.base.Objects;

/**
 * Esta clase identifica un envío realizado a un receptor de cierto mensaje. <br>
 * El identificador del envío tiene el Id del mensaje y el receptor destino como discriminantes.<br>
 * Esta clase permite identificar el envío al recibir una confirmación por parte de un receptor
 * 
 * @author D. García
 */
public class IdentificadorDeEnvio {

	private IdVortex idDeMensajeEnviado;
	public static final String idDeMensajeEnviado_FIELD = "idDeMensajeEnviado";
	private ReceptorVortex receptorDestino;
	public static final String receptorDestino_FIELD = "receptorDestino";

	public IdVortex getIdDeMensajeEnviado() {
		return idDeMensajeEnviado;
	}

	public void setIdDeMensajeEnviado(final IdVortex idDeMensajeEnviado) {
		this.idDeMensajeEnviado = idDeMensajeEnviado;
	}

	public ReceptorVortex getReceptorDestino() {
		return receptorDestino;
	}

	public void setReceptorDestino(final ReceptorVortex receptorDestino) {
		this.receptorDestino = receptorDestino;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(idDeMensajeEnviado, receptorDestino);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof IdentificadorDeEnvio)) {
			return false;
		}
		final IdentificadorDeEnvio that = (IdentificadorDeEnvio) obj;
		return Objects.equal(this.receptorDestino, that.receptorDestino)
				&& Objects.equal(this.idDeMensajeEnviado, that.idDeMensajeEnviado);
	}

	public static IdentificadorDeEnvio create(final IdVortex idMensaje, final ReceptorVortex receptor) {
		final IdentificadorDeEnvio identificador = new IdentificadorDeEnvio();
		identificador.receptorDestino = receptor;
		identificador.idDeMensajeEnviado = idMensaje;
		return identificador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idDeMensajeEnviado_FIELD, idDeMensajeEnviado)
				.add(receptorDestino_FIELD, receptorDestino).toString();
	}

	/**
	 * Indica si este identificador de envío es para uno realizado al receptor indicado
	 * 
	 * @param receptorComprobado
	 *            El receptor a comprobar
	 * @return true si este identificador de envío esta compuesto por el receptor pasado
	 */
	public boolean esPara(final ReceptorVortex receptorComprobado) {
		final boolean esParaElReceptor = this.receptorDestino.equals(receptorComprobado);
		return esParaElReceptor;
	}
}
