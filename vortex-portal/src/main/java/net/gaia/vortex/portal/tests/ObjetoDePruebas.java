/**
 * 04/09/2013 23:37:34 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.portal.tests;

/**
 * Esta clase representa un obejto con algunos datos primitivos para pruebas de mensajes
 * 
 * @author D. García
 */
public class ObjetoDePruebas {

	private int numero;
	private String cadena;
	private ObjetoDePruebas objeto;

	public int getNumero() {
		return numero;
	}

	public void setNumero(final int numero) {
		this.numero = numero;
	}

	public String getCadena() {
		return cadena;
	}

	public void setCadena(final String cadena) {
		this.cadena = cadena;
	}

	public ObjetoDePruebas getObjeto() {
		return objeto;
	}

	public void setObjeto(final ObjetoDePruebas objeto) {
		this.objeto = objeto;
	}

	public static ObjetoDePruebas create(final int numero) {
		final ObjetoDePruebas objeto = new ObjetoDePruebas();
		objeto.numero = numero;
		objeto.cadena = "Cadena" + numero;
		return objeto;
	}

}
