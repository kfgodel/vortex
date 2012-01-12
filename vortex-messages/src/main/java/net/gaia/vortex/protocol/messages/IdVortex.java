/**
 * 16/08/2011 23:12:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages;

import com.google.common.base.Objects;

/**
 * Esta clase representa un identificador de un mensaje que permite compararlo por igualdad con otro
 * mensaje
 * 
 * @author D. García
 */
public class IdVortex {

	private Long numeroDeSecuencia;
	public static final String numeroDeSecuencia_FIELD = "numeroDeSecuencia";

	private String idDelEmisor;
	public static final String idDelEmisor_FIELD = "idDelEmisor";

	private String hashDelContenido;
	public static final String hashDelContenido_FIELD = "hashDelContenido";

	private Long timestamp;
	public static final String timestamp_FIELD = "timestamp";

	public String getHashDelContenido() {
		return hashDelContenido;
	}

	public void setHashDelContenido(final String hashDelContenido) {
		this.hashDelContenido = hashDelContenido;
	}

	public String getIdDelEmisor() {
		return idDelEmisor;
	}

	public void setIdDelEmisor(final String idDelEmisor) {
		this.idDelEmisor = idDelEmisor;
	}

	public Long getNumeroDeSecuencia() {
		return numeroDeSecuencia;
	}

	public void setNumeroDeSecuencia(final Long numeroDeSecuencia) {
		this.numeroDeSecuencia = numeroDeSecuencia;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Long timestamp) {
		this.timestamp = timestamp;
	}

	public static IdVortex create(final String hashDelContenido, final String idDelEmisor,
			final Long numeroDeSecuencia, final Long currentTimestamp) {
		final IdVortex identification = new IdVortex();
		identification.hashDelContenido = hashDelContenido;
		identification.idDelEmisor = idDelEmisor;
		identification.numeroDeSecuencia = numeroDeSecuencia;
		identification.timestamp = currentTimestamp;
		return identification;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.hashDelContenido, this.idDelEmisor, this.numeroDeSecuencia, this.timestamp);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof IdVortex)) {
			return false;
		}
		final IdVortex that = (IdVortex) obj;
		return Objects.equal(this.timestamp, that.timestamp)
				&& Objects.equal(this.hashDelContenido, that.hashDelContenido)
				&& Objects.equal(this.numeroDeSecuencia, that.numeroDeSecuencia)
				&& Objects.equal(this.idDelEmisor, that.idDelEmisor);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this) //
				.add(numeroDeSecuencia_FIELD, numeroDeSecuencia)//
				.add(idDelEmisor_FIELD, idDelEmisor)//
				.add(hashDelContenido_FIELD, hashDelContenido)//
				.add(timestamp_FIELD, timestamp)//
				.toString();
	}
}
