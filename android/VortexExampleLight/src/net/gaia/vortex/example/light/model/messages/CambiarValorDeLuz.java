/**
 * 18/03/2013 23:10:14 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.example.light.model.messages;

/**
 * Esta clase representa el mensaje enviado para cambiar el estado de la luz
 * 
 * @author D. García
 */
public class CambiarValorDeLuz extends MensajeSupport {

	public static final String TIPO = "CambiarValorDeLuz";

	public CambiarValorDeLuz() {
		super(TIPO);
	}

	private Integer nuevoValor;
	public static final String nuevoValor_FIELD = "nuevoValor";

	public Integer getNuevoValor() {
		return nuevoValor;
	}

	public void setNuevoValor(Integer nuevoValor) {
		this.nuevoValor = nuevoValor;
	}

	public static CambiarValorDeLuz create(int nuevoValor) {
		CambiarValorDeLuz cambio = new CambiarValorDeLuz();
		cambio.nuevoValor = nuevoValor;
		return cambio;
	}
}
