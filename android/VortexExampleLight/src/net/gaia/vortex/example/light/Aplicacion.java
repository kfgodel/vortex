/**
 * 16/03/2013 18:02:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.example.light;

import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.impl.LuzEnMemoria;
import ar.com.iron.android.extensions.applications.CustomApplication;

/**
 * 
 * @author D. García
 */
public class Aplicacion extends CustomApplication {

	public static Aplicacion app;

	private Luz luz;

	public Luz getLuz() {
		return luz;
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#getMainThreadName()
	 */
	@Override
	protected String getMainThreadName() {
		return "AplicacionLuz";
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#initializeGlobalComponents()
	 */
	@Override
	protected void initializeGlobalComponents() {
		app = this;
		luz = LuzEnMemoria.create();
	}
}
