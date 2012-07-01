/**
 * 30/06/2012 21:09:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests.perf;

import java.util.Arrays;
import java.util.List;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un mensaje modelo para los tests que tiene un estado esperable como
 * promedio de tipos de datos y cantidad de bytes en los mensajes tratados en la red
 * 
 * @author D. García
 */
public class MensajeModeloParaTests {

	public static long instanciasCreadas = 0;

	private String posibleDominio;
	public static final String posibleDominio_FIELD = "posibleDominio";

	private String posibleEmisor;
	public static final String posibleEmisor_FIELD = "posibleEmisor";

	private Long numeroDeSecuencia;
	public static final String numeroDeSecuencia_FIELD = "numeroDeSecuencia";

	private ObjetoAdicionalParaTests estadoAdicionalAlMensaje;
	public static final String estadoAdicionalAlMensaje_FIELD = "estadoAdicionalAlMensaje";

	private List<String> variosStrings;
	public static final String variosStrings_FIELD = "variosStrings";

	public String getPosibleDominio() {
		return posibleDominio;
	}

	public void setPosibleDominio(final String posibleDominio) {
		this.posibleDominio = posibleDominio;
	}

	public String getPosibleEmisor() {
		return posibleEmisor;
	}

	public void setPosibleEmisor(final String posibleEmisor) {
		this.posibleEmisor = posibleEmisor;
	}

	public Long getNumeroDeSecuencia() {
		return numeroDeSecuencia;
	}

	public void setNumeroDeSecuencia(final Long numeroDeSecuencia) {
		this.numeroDeSecuencia = numeroDeSecuencia;
	}

	public ObjetoAdicionalParaTests getEstadoAdicionalAlMensaje() {
		return estadoAdicionalAlMensaje;
	}

	public void setEstadoAdicionalAlMensaje(final ObjetoAdicionalParaTests estadoAdicionalAlMensaje) {
		this.estadoAdicionalAlMensaje = estadoAdicionalAlMensaje;
	}

	public List<String> getVariosStrings() {
		return variosStrings;
	}

	public void setVariosStrings(final List<String> variosStrings) {
		this.variosStrings = variosStrings;
	}

	public static MensajeModeloParaTests create() {
		final MensajeModeloParaTests mensaje = new MensajeModeloParaTests();
		mensaje.setEstadoAdicionalAlMensaje(ObjetoAdicionalParaTests.create());
		mensaje.setNumeroDeSecuencia(instanciasCreadas++);
		mensaje.setPosibleDominio("net.gaia.vortex.tests");
		mensaje.setPosibleEmisor("kfgodel-saraza");
		mensaje.setVariosStrings(Arrays.asList("s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10"));
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeSecuencia_FIELD, numeroDeSecuencia)
				.con(posibleDominio_FIELD, posibleDominio).con(posibleEmisor_FIELD, posibleEmisor)
				.con(variosStrings_FIELD, variosStrings).con(estadoAdicionalAlMensaje_FIELD, estadoAdicionalAlMensaje)
				.toString();
	}

}
