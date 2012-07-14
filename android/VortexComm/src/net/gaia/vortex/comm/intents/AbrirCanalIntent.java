/**
 * 14/07/2012 20:38:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.intents;

import net.gaia.vortex.comm.CanalActivity;
import android.content.Context;
import android.content.Intent;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase representa el intent usado para abrir un canal
 * 
 * @author D. García
 */
public class AbrirCanalIntent extends Intent {

	/**
	 * Key para almacenar los datos del canal
	 */
	public static final String NOMBRE_DE_CANAL_KEY = "NOMBRE_DE_CANAL_KEY";

	/**
	 * Constructor por copia para los intents recibidos
	 */
	public AbrirCanalIntent(Intent copia) {
		super(copia);
	}

	/**
	 * Crea el intent con el canal que se quiere abrir
	 */
	public AbrirCanalIntent(Context contextoAndroid, String nombreDeCanal) {
		super(contextoAndroid, CanalActivity.class);
		putExtra(NOMBRE_DE_CANAL_KEY, nombreDeCanal);
	}

	/**
	 * Devuelve el nombre del canal que se solicitó abrir
	 */
	public String getNombreDelCanal() {
		String nombreDelCanal = getStringExtra(NOMBRE_DE_CANAL_KEY);
		if (nombreDelCanal == null) {
			throw new FaultyCodeException("Pidieron abrir canal sin indicar nombre");
		}
		return nombreDelCanal;
	}

}
