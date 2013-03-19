/**
 * fredwr * 16/03/2013 16:42:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.example.light.model.impl;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.LuzChangeListener;

/**
 * Esta clase implementa la luz en memoria sin necesidad de sincronización
 * 
 * @author D. García
 */
public class LuzEnMemoria implements Luz {

	private int estadoActual;

	private List<LuzChangeListener> listeners;

	public List<LuzChangeListener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<LuzChangeListener>(2);
		}
		return listeners;
	}

	public void setListeners(List<LuzChangeListener> listeners) {
		this.listeners = listeners;
	}

	public static LuzEnMemoria create() {
		LuzEnMemoria luz = new LuzEnMemoria();
		luz.estadoActual = 0;
		return luz;
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#cambiarA(int)
	 */
	@Override
	public void cambiarA(int nuevoValor) {
		if (nuevoValor == estadoActual) {
			// No hay cmabio
			return;
		}
		estadoActual = nuevoValor;
		notificarListeners();
	}

	/**
	 * Notifica a todos los listeners del nuevo valor
	 */
	private void notificarListeners() {
		List<LuzChangeListener> allListeners = getListeners();
		for (LuzChangeListener listener : allListeners) {
			listener.onLuzChanged(estadoActual);
		}
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#addChangeListener(net.gaia.vortex.example.light.model.LuzChangeListener)
	 */
	@Override
	public void addChangeListener(LuzChangeListener luzChangeListener) {
		getListeners().add(luzChangeListener);
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#getValorActual()
	 */
	@Override
	public int getValorActual() {
		return estadoActual;
	}

}
