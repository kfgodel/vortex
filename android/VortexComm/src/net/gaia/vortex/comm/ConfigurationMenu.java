/**
 * 09/07/2012 22:25:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm;

import android.content.Context;
import android.content.Intent;
import ar.com.iron.menues.ActivityMenuItem;

/**
 * Este enum representa las opciones del menu de configuración
 * 
 * @author D. García
 */
public enum ConfigurationMenu implements ActivityMenuItem<ConfigurationActivity> {
	VER_CANALES {
		@Override
		public Object getItemTitleOrResId() {
			return "Ver canales";
		}

		@Override
		public Intent getFiredActivityIntent(Context contexto) {
			return new Intent(contexto, CanalesActivity.class);
		}
	};

	/**
	 * @see ar.com.iron.menues.CustomMenuItem#getItemTitleOrResId()
	 */
	public Object getItemTitleOrResId() {
		return null;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getFiredActivityIntent(android.content.Context)
	 */
	public Intent getFiredActivityIntent(Context contexto) {
		return null;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#onSelection(android.app.Activity)
	 */
	public boolean onSelection(ConfigurationActivity activity) {
		return false;
	}

	/**
	 * @see ar.com.iron.menues.ActivityMenuItem#getIconId()
	 */
	public Integer getIconId() {
		return null;
	}

}
