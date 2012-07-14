/**
 * 09/07/2012 18:38:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa la configuración de vortex comm
 * 
 * @author D. García
 */
public class ConfiguracionVortexComm {

	/**
	 * Canal inicial compartido
	 */
	public static final String CANAL_VORTEX_GLOBAL = "Global";

	private String nombreDeUsuario;
	private String hostDelServidor;
	private Integer numeroDePuerto;
	private List<String> canalesDelUsuario;

	public String getNombreDeUsuario() {
		return nombreDeUsuario;
	}

	public void setNombreDeUsuario(String nombreDeUsuario) {
		this.nombreDeUsuario = nombreDeUsuario;
	}

	public String getHostDelServidor() {
		return hostDelServidor;
	}

	public void setHostDelServidor(String hostDelServidor) {
		this.hostDelServidor = hostDelServidor;
	}

	public Integer getNumeroDePuerto() {
		return numeroDePuerto;
	}

	public void setNumeroDePuerto(Integer numeroDePuerto) {
		this.numeroDePuerto = numeroDePuerto;
	}

	/**
	 * Crea una configuración default inicial
	 * 
	 * @return La configuración con valores default
	 */
	public static ConfiguracionVortexComm create() {
		ConfiguracionVortexComm config = new ConfiguracionVortexComm();
		config.hostDelServidor = "kfgodel.info";
		config.numeroDePuerto = 61616;
		config.agregarCanal(CANAL_VORTEX_GLOBAL);
		return config;
	}

	/**
	 * Indica si esta configuración puede ser usada
	 * 
	 * @return false si hay algun campo por definir
	 */
	public boolean estaCompleta() {
		boolean tieneNombre = getNombreDeUsuario() != null && getNombreDeUsuario().trim().length() > 0;
		boolean tieneHost = getHostDelServidor() != null && getHostDelServidor().trim().length() > 0;
		boolean tienePuerto = getNumeroDePuerto() != null;
		return tieneNombre && tieneHost && tienePuerto;
	}

	public List<String> getCanalesDelUsuario() {
		if (canalesDelUsuario == null) {
			canalesDelUsuario = new ArrayList<String>();
		}
		return canalesDelUsuario;
	}

	public void setCanalesDelUsuario(List<String> canalesDelUsuario) {
		this.canalesDelUsuario = canalesDelUsuario;
	}

	/**
	 * Agrega el texto como canal de esta configuración sólo si no estaba antes
	 * 
	 * @param nombreDelCanal
	 *            El nombre a agregar
	 */
	public void agregarCanal(String nombreDelCanal) {
		if (nombreDelCanal == null || nombreDelCanal.trim().length() == 0) {
			// No agregamos cadenas vacias
			return;
		}
		if (getCanalesDelUsuario().contains(nombreDelCanal)) {
			// Ya está
			return;
		}
		getCanalesDelUsuario().add(nombreDelCanal);
	}

	public void quitarCanal(String nombreDelCanal) {
		getCanalesDelUsuario().remove(nombreDelCanal);
	}
}
