/**
 * 21/08/2011 20:07:24 Copyright (C) 2011 Darío L. García
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
import javax.persistence.ManyToOne;

import com.tenpines.integration.hibernate.repositories.persistibles.PersistibleSupport;

/**
 * Esta clase representa uno de los tags en los que un receptor puede estar interesado. Necesito
 * esta clase para mapear correctamente con Hibernate
 * 
 * @author D. García
 */
@Entity
public class TagPersistible extends PersistibleSupport {

	private String valor;
	public static final String valor_FIELD = "valor";

	@ManyToOne
	private ReceptorHttp receptor;
	public static final String receptor_FIELD = "receptor";

	public String getValor() {
		return valor;
	}

	public void setValor(final String valor) {
		this.valor = valor;
	}

	public ReceptorHttp getReceptor() {
		return receptor;
	}

	public void setReceptor(final ReceptorHttp receptor) {
		this.receptor = receptor;
	}

	public static TagPersistible create(final String valorTag, final ReceptorHttp receptor) {
		final TagPersistible tag = new TagPersistible();
		tag.valor = valorTag;
		tag.receptor = receptor;
		return tag;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.valor;
	}
}
