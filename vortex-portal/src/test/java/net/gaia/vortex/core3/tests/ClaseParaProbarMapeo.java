/**
 * 25/06/2012 00:25:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.tests;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase es para probar la performance del mapeo de obejtos
 * 
 * @author D. García
 */
public class ClaseParaProbarMapeo {

	private ClaseParaProbarMapeo subInstancia;

	private List<ClaseParaProbarMapeo> subInstancias;

	private int entero;

	private String texto;

	public int getEntero() {
		return entero;
	}

	public void setEntero(final int entero) {
		this.entero = entero;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(final String texto) {
		this.texto = texto;
	}

	public ClaseParaProbarMapeo getSubInstancia() {
		return subInstancia;
	}

	public void setSubInstancia(final ClaseParaProbarMapeo subInstancia) {
		this.subInstancia = subInstancia;
	}

	public List<ClaseParaProbarMapeo> getSubInstancias() {
		if (subInstancias == null) {
			subInstancias = new ArrayList<ClaseParaProbarMapeo>();
		}
		return subInstancias;
	}

	public void setSubInstancias(final List<ClaseParaProbarMapeo> subInstancias) {
		this.subInstancias = subInstancias;
	}

}
