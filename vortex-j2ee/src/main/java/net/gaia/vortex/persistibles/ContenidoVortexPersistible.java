/**
 * 18/10/2011 22:39:20 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.model.messages.protocolo.ContenidoVortex;

import com.tenpines.integration.hibernate.repositories.persistibles.PersistibleSupport;

/**
 * Esta clase es la versión persistible del contenido vortex de un mensaje
 * 
 * @author D. García
 */
@Entity
public class ContenidoVortexPersistible extends PersistibleSupport {

	private String tipoContenido;
	public static final String tipoContenido_FIELD = "tipoContenido";

	private String valor;
	public static final String valor_FIELD = "valor";

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(final String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(final String valor) {
		this.valor = valor;
	}

	public static ContenidoVortexPersistible create(final ContenidoVortex contenidoVortex) {
		final ContenidoVortexPersistible persistible = new ContenidoVortexPersistible();
		persistible.tipoContenido = contenidoVortex.getTipoContenido();
		persistible.valor = contenidoVortex.getValor();
		return persistible;
	}

	public ContenidoVortex toContenidoVortex() {
		final ContenidoVortex contenido = new ContenidoVortex();
		contenido.setTipoContenido(tipoContenido);
		contenido.setValor(valor);
		return contenido;
	}

}
