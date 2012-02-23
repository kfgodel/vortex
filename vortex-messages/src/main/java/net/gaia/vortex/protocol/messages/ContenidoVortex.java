/**
 * 18/10/2011 22:22:49 Copyright (C) 2011 Darío L. García
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

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Objects;

/**
 * Esta clase representa el contenido de un mensaje vortex con un tipo que sirve para interpretarlo
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContenidoVortex {

	@NotNull
	private String tipoContenido;
	public static final String tipoContenido_FIELD = "tipoContenido";

	@AssertValid
	private Object valor;
	public static final String valor_FIELD = "valor";

	public String getTipoContenido() {
		return tipoContenido;
	}

	public void setTipoContenido(final String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(final Object valor) {
		this.valor = valor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this) //
				.add(tipoContenido_FIELD, tipoContenido)//
				.add(valor_FIELD, valor)//
				.toString();
	}

	public static ContenidoVortex create(final String tipo, final Object valor) {
		final ContenidoVortex contenido = new ContenidoVortex();
		contenido.tipoContenido = tipo;
		contenido.valor = valor;
		return contenido;
	}

}
