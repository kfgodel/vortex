/**
 * 09/07/2012 22:15:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.model;

/**
 * Esta clase representa un canal de mensajería
 * 
 * @author D. García
 */
public class Canal {

	private String nombreDelCanal;
	private Boolean conectado;

	public String getNombreDelCanal() {
		return nombreDelCanal;
	}

	public void setNombreDelCanal(String nombreDelCanal) {
		this.nombreDelCanal = nombreDelCanal;
	}

	public Boolean getConectado() {
		return conectado;
	}

	public void setConectado(Boolean conectado) {
		this.conectado = conectado;
	}

	public static Canal create(String nombre) {
		Canal canal = new Canal();
		canal.nombreDelCanal = nombre;
		canal.conectado = false;
		return canal;
	}
}
