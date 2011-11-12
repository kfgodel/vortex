/**
 * 20/08/2011 17:25:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.persistibles;

import javax.persistence.Entity;

import net.gaia.vortex.model.messages.protocolo.IdVortex;

import com.tenpines.integration.hibernate.repositories.persistibles.PersistibleSupport;

/**
 * 
 * @author D. García
 */
@Entity
public class IdVortexPersistible extends PersistibleSupport {

	private String hashDelContenido;
	public static final String hashDelContenido_FIELD = "hashDelContenido";

	private String idDelEmisor;
	public static final String idDelEmisor_FIELD = "idDelEmisor";

	private Long timestamp;
	public static final String timestamp_FIELD = "timestamp";

	private Long numeroDeSecuencia;
	public static final String numeroDeSecuencia_FIELD = "numeroDeSecuencia";

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

	public static IdVortexPersistible create(final IdVortex idVortex) {
		final IdVortexPersistible identification = new IdVortexPersistible();
		identification.hashDelContenido = idVortex.getHashDelContenido();
		identification.idDelEmisor = idVortex.getIdDelEmisor();
		identification.numeroDeSecuencia = idVortex.getNumeroDeSecuencia();
		identification.timestamp = idVortex.getTimestamp();
		return identification;
	}

	/**
	 * @return
	 */
	public IdVortex toIdentificacionVortex() {
		final IdVortex nuevoId = new IdVortex();
		nuevoId.setHashDelContenido(hashDelContenido);
		nuevoId.setIdDelEmisor(idDelEmisor);
		nuevoId.setNumeroDeSecuencia(numeroDeSecuencia);
		nuevoId.setTimestamp(timestamp);
		return nuevoId;
	}

}
