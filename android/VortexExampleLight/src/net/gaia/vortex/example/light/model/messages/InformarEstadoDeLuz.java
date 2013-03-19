/**
 * 18/03/2013 23:10:26 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el mensaje que la luz envía para informar su estado actual
 * 
 * @author D. García
 */
public class InformarEstadoDeLuz extends MensajeSupport {

	public static final String TIPO = "InformarEstadoDeLuz";

	public InformarEstadoDeLuz() {
		super(TIPO);
	}

	private Integer estadoActual;
	public static final String estadoActual_FIELD = "estadoActual";

	public Integer getEstadoActual() {
		return estadoActual;
	}

	public void setEstadoActual(Integer nuevoValor) {
		this.estadoActual = nuevoValor;
	}

	public static InformarEstadoDeLuz create(Integer valorActual) {
		InformarEstadoDeLuz name = new InformarEstadoDeLuz();
		name.estadoActual = valorActual;
		return name;
	}
}
