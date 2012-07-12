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

import net.gaia.vortex.comm.api.VortexAndroid;
import android.content.Intent;
import ar.com.iron.android.extensions.services.BackgroundProcess;
import ar.com.iron.android.extensions.services.BackgroundService;
import ar.com.iron.android.extensions.services.local.LocalServiceBinder;
import ar.com.iron.android.extensions.services.local.LocallyBindableService;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceEngineVariable;

/**
 * Esta clase implementa un servicio android que permite acceder a vortex como recurso que opera en
 * background
 * 
 * @author D. García
 */
public class VortexService extends BackgroundService implements LocallyBindableService<VortexAndroid> {

	/**
	 * Permite el enlace con los otros componentes de android
	 */
	private LocalServiceBinder<VortexAndroid> serviceBinder;

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStart()
	 */
	@Override
	protected void beforeProcessStart() {
		// deberia conectar a vortex?
		// Si no hay conectividad?
		persistenceEngine = PersistenceEngineVariable.getInstance();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStart()
	 */
	@Override
	protected void afterProcessStart() {
		PersistenceDao dao = persistenceEngine.getDao();
		dao.setBackgroundProcess(getBackgroundProcess());
		serviceBinder = LocalServiceBinder.create(dao);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#createBackgroundThread()
	 */
	@Override
	protected BackgroundProcess createBackgroundThread() {
		return new BackgroundProcess(this, "VortexService");
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#onBind(android.content.Intent)
	 */
	@Override
	public LocalServiceBinder<VortexService> onBind(Intent intent) {
		return serviceBinder;
	}

	/**
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		persistenceEngine.releaseResources();

		// Queremos que vuelvan a invocar onBind
		return false;
	}
}
