/**
 * 15/07/2012 10:56:32 Copyright (C) 2011 Darío L. García
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

import java.net.InetSocketAddress;

import net.gaia.vortex.android.service.impl.VortexConnectionImpl;
import net.gaia.vortex.android.service.intents.ConectarConServidorVortex;
import net.gaia.vortex.core.api.Nodo;
import android.content.Intent;
import ar.com.iron.android.extensions.services.BackgroundService;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;

/**
 * Esta clase representa el servicio android que conecta el nodo central al servidor online de
 * vortex.<br>
 * Para ello es necesario que se indique host y puerto de conexion
 * 
 * @author D. García
 */
public class VortexConectorService extends BackgroundService {

	private VortexConnection connection;
	private LocalServiceConnector<VortexAndroidAccess> vortexConnector;

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStart()
	 */
	@Override
	protected void beforeProcessStart() {
		connection = VortexConnectionImpl.create();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStart()
	 */
	@Override
	protected void afterProcessStart() {
		// Intentamos conectarnos con el servicio vortex
		vortexConnector = LocalServiceConnector.create(VortexProviderService.class);
		vortexConnector.setConnectionListener(new LocalServiceConnectionListener<VortexAndroidAccess>() {
			public void onServiceDisconnection(final VortexAndroidAccess disconnectedIntercomm) {
				onVortexNoDisponible(disconnectedIntercomm);
			}

			public void onServiceConnection(final VortexAndroidAccess intercommObject) {
				onVortexDisponible(intercommObject);
			}
		});
		vortexConnector.bindToService(this);
	}

	/**
	 * Invocado al desconectarse del nodo central de la app
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexNoDisponible(VortexAndroidAccess vortexForAndroid) {
		connection.desconectarDeNodoCentral();
	}

	/**
	 * Invocado cuando este servicio logra conectarse con el proveedor central de vortex para la
	 * aplicación
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexDisponible(VortexAndroidAccess vortexForAndroid) {
		Nodo nodoCentral = vortexForAndroid.getNodoCentral();
		connection.utilizarComoNodoCentralA(nodoCentral);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStop()
	 */
	@Override
	protected void beforeProcessStop() {
		vortexConnector.unbindFromService(this);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStop()
	 */
	@Override
	protected void afterProcessStop() {
		connection.closeAndDispose();
	}

	/**
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		ConectarConServidorVortex pedidoDeConexion = new ConectarConServidorVortex(intent);
		InetSocketAddress serverAddress = pedidoDeConexion.getServerAddress();
		connection.conectarCon(serverAddress);
		super.onStart(intent, startId);
	}

}
