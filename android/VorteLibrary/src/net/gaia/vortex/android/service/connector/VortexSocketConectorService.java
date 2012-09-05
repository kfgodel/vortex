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
package net.gaia.vortex.android.service.connector;

import java.net.InetSocketAddress;

import net.gaia.vortex.android.service.connector.impl.VortexConnectionImpl;
import net.gaia.vortex.android.service.connector.intents.CambioDeConectividadVortex;
import net.gaia.vortex.android.service.connector.intents.ConectarConServidorVortexIntent;
import net.gaia.vortex.android.service.provider.VortexProviderAccess;
import net.gaia.vortex.android.service.provider.VortexProviderService;
import net.gaia.vortex.core.api.Nodo;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import ar.com.iron.android.extensions.connections.ConnectivityChangeListener;
import ar.com.iron.android.extensions.connections.ConnectivityObserver;
import ar.com.iron.android.extensions.messages.IntentReceptor;
import ar.com.iron.android.extensions.services.BackgroundService;
import ar.com.iron.android.extensions.services.BackgroundTask;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;

/**
 * Esta clase representa el servicio android que conecta el nodo central al servidor online de
 * vortex.<br>
 * Para ello es necesario que se indique host y puerto de conexion
 * 
 * @author D. García
 */
public class VortexSocketConectorService extends BackgroundService {

	private IntentReceptor intentReceptor;
	private VortexConnection connection;
	private LocalServiceConnector<VortexProviderAccess> vortexConnector;
	private ConnectivityObserver connectivityObserver;

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStart()
	 */
	@Override
	protected void beforeProcessStart() {
		intentReceptor = new IntentReceptor(this);
		connection = VortexConnectionImpl.create();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStart()
	 */
	@Override
	protected void afterProcessStart() {
		// Intentamos conectarnos con el servicio vortex
		vortexConnector = LocalServiceConnector.create(VortexProviderService.class);
		vortexConnector.setConnectionListener(new LocalServiceConnectionListener<VortexProviderAccess>() {
			public void onServiceDisconnection(final VortexProviderAccess vortexAccess) {
				onVortexNoDisponible(vortexAccess);
			}

			public void onServiceConnection(final VortexProviderAccess vortexAccess) {
				onVortexDisponible(vortexAccess);
			}
		});
		vortexConnector.bindToService(this);
		registrarListenerDeEventosEnLaConectividad();
	}

	/**
	 * Registra un receiver para ser notificado de cambios en al conectividad
	 */
	private void registrarListenerDeEventosEnLaConectividad() {
		connectivityObserver = ConnectivityObserver.createObserving(this, new ConnectivityChangeListener() {
			public void onConnectivityChanged(boolean connected, NetworkInfo network, boolean networkChanged) {
				if (!connected) {
					// Perdimos conectividad
					desconectarDelServidor();
				} else {
					// Es la primera vez o cambio la red, nos reconectamos
					conectarAlServidor();
				}
				sendBroadcast(new CambioDeConectividadVortex(connected));
			}
		}, intentReceptor);
	}

	/**
	 * Invocado al desconectarse del nodo central de la app
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexNoDisponible(VortexProviderAccess vortexForAndroid) {
		connection.desconectarDeNodoCentral();
	}

	/**
	 * Invocado cuando este servicio logra conectarse con el proveedor central de vortex para la
	 * aplicación
	 * 
	 * @param vortexAccess
	 */
	protected void onVortexDisponible(VortexProviderAccess vortexAccess) {
		Nodo nodoCentral = vortexAccess.getNodoCentral();
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
		intentReceptor.unregisterMessageReceivers();
	}

	/**
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(final Intent intent, int startId) {
		ConectarConServidorVortexIntent pedidoDeConexion = new ConectarConServidorVortexIntent(intent);
		cambiarDireccionDeServidor(pedidoDeConexion);
		super.onStart(intent, startId);
	}

	/**
	 * CAmbia la dirección del servidor realizando reconexiones necesarias en un thread propio
	 * 
	 * @param pedidoDeConexion
	 *            El intent con la data de la nueva dir
	 */
	private void cambiarDireccionDeServidor(final ConectarConServidorVortexIntent pedidoDeConexion) {
		// No podemos hacer IO en el thread principal
		this.getBackgroundProcess().addTask(new BackgroundTask<Void>() {
			@Override
			public Void execute(Context backgroundContext) {
				InetSocketAddress serverAddress = pedidoDeConexion.getServerAddress();
				connection.usarLaDireccion(serverAddress);
				return null;
			}
		});
	}

	/**
	 * Desconecta el nodo socket del servidor en un thread propio
	 */
	private void desconectarDelServidor() {
		// No podemos usar el thread principal para las operaciones de IO
		this.getBackgroundProcess().addTask(new BackgroundTask<Void>() {
			@Override
			public Void execute(Context backgroundContext) {
				connection.desconectarDelServidor();
				return null;
			}
		});
	}

	/**
	 * Re Conecta al servidor en un thread propio
	 */
	private void conectarAlServidor() {
		// No podemos hacer oepracioens de IO en el thread principal
		this.getBackgroundProcess().addTask(new BackgroundTask<Void>() {
			@Override
			public Void execute(Context backgroundContext) {
				connection.reconectarAlServidor();
				return null;
			}
		});

	}

}
