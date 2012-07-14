/**
 * 11/07/2012 19:44:24 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service;

import net.gaia.vortex.android.service.impl.VortexAndroidAccessImpl;
import android.content.Intent;
import ar.com.iron.android.extensions.services.BackgroundProcess;
import ar.com.iron.android.extensions.services.BackgroundService;
import ar.com.iron.android.extensions.services.local.LocalServiceBinder;
import ar.com.iron.android.extensions.services.local.LocallyBindableService;

/**
 * Esta clase implementa el servicio de android que permite acceder a vortex como un pedazo de la
 * red, y extenderla con nuevos nodos
 * 
 * @author D. García
 */
public class VortexProviderService extends BackgroundService implements LocallyBindableService<VortexAndroidAccess> {

	/**
	 * Permite el enlace con los otros componentes de android
	 */
	private LocalServiceBinder<VortexAndroidAccess> serviceBinder;

	private VortexAndroidAccessImpl vortex;

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStart()
	 */
	@Override
	protected void beforeProcessStart() {
		vortex = VortexAndroidAccessImpl.create();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStart()
	 */
	@Override
	protected void afterProcessStart() {
		serviceBinder = LocalServiceBinder.<VortexAndroidAccess> create(vortex);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#createBackgroundThread()
	 */
	@Override
	protected BackgroundProcess createBackgroundThread() {
		return new BackgroundProcess(this, "VortexProviderService");
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#onBind(android.content.Intent)
	 */
	@Override
	public LocalServiceBinder<VortexAndroidAccess> onBind(Intent intent) {
		return serviceBinder;
	}

	/**
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		vortex.closeAndDispose();

		// Queremos que vuelvan a invocar onBind
		return false;
	}
}
