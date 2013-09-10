/**
 * 24/08/2012 15:53:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.tests;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es una condición para tests que permite saber si fue evaluada y devuelve el valor que
 * indique otra condición
 * 
 * @author D. García
 */
public class CondicionTestWrapper implements Condicion {

	private Condicion condicionReal;
	public static final String condicionReal_FIELD = "condicionReal";

	private boolean evaluada;
	public static final String evaluada_FIELD = "evaluada";

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		evaluada = true;
		return condicionReal.esCumplidaPor(mensaje);
	}

	public static CondicionTestWrapper create(final Condicion otra) {
		final CondicionTestWrapper condicion = new CondicionTestWrapper();
		condicion.condicionReal = otra;
		condicion.evaluada = false;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(evaluada_FIELD, evaluada).con(condicionReal_FIELD, condicionReal).toString();
	}

	public Condicion getCondicionReal() {
		return condicionReal;
	}

	public void setCondicionReal(final Condicion condicionReal) {
		this.condicionReal = condicionReal;
	}

	public boolean isEvaluada() {
		return evaluada;
	}

	public void setEvaluada(final boolean evaluada) {
		this.evaluada = evaluada;
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

}
