/**
 * 26/01/2013 12:46:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.tests;

/**
 * Esta clase se usa para los tests
 * 
 * @author D. García
 */
public class MensajeParaTestDeRuteo {
	public String atributo;
	public static final String atributo_FIELD = "atributo";

	public MensajeParaTestDeRuteo() {
	}

	public MensajeParaTestDeRuteo(final String valor) {
		this.atributo = valor;
	}

	
	public boolean equals(final Object obj) {
		if (!(obj instanceof MensajeParaTestDeRuteo)) {
			return false;
		}
		final MensajeParaTestDeRuteo that = (MensajeParaTestDeRuteo) obj;
		final boolean mismoAtributo = this.atributo.equals(that.atributo);
		return mismoAtributo;
	}

	
	public int hashCode() {
		return this.atributo.hashCode();
	}
}