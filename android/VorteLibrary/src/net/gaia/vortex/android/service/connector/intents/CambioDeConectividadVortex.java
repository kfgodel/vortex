/**
 * 15/07/2012 17:09:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service.connector.intents;

import android.content.Intent;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase representa un intent enviado para notificar el cambio de conectividad de vortex.<br>
 * Normalmente es lanzado cuando el conector se desconecta de la dirección indicada
 * 
 * @author D. García
 */
public class CambioDeConectividadVortex extends Intent {

	private static final String NUEVO_ESTADO_DE_CONECTIVIDAD_KEY = "NUEVO_ESTADO_DE_CONECTIVIDAD_KEY";

	public static final String ACTION = "net.gaia.vortex.android.service.intents.CambioDeConectividadVortex";

	public CambioDeConectividadVortex(Intent copia) {
		super(copia);
	}

	public CambioDeConectividadVortex(boolean conectado) {
		super(ACTION);
		putExtra(NUEVO_ESTADO_DE_CONECTIVIDAD_KEY, conectado);
	}

	public boolean estaConectado() {
		if (!hasExtra(NUEVO_ESTADO_DE_CONECTIVIDAD_KEY)) {
			throw new FaultyCodeException("No existe info de conectividad en este intent");
		}
		boolean conectado = getBooleanExtra(NUEVO_ESTADO_DE_CONECTIVIDAD_KEY, false);
		return conectado;
	}
}
