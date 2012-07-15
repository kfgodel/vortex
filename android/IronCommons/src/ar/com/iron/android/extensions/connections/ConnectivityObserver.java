/**
 * 15/07/2012 13:10:38 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.connections;

import java.util.concurrent.atomic.AtomicReference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ar.com.iron.android.extensions.messages.IntentReceptor;
import ar.com.iron.android.helpers.ContextHelper;

/**
 * Esta clase permite obtener notificaciones y conocer el estado de conectividad de la aplicación,
 * abstrayendo el conocimiento de qué intents y managers están involucrados.<br>
 * Para poder utilizar esta clase es necesario el permiso ACCESS_NETWORK_STATE
 * 
 * @author D. García
 */
public class ConnectivityObserver extends BroadcastReceiver {

	/**
	 * Indica si este cliente tiene conectividad con el exterior
	 */
	private final AtomicReference<Boolean> hasConnectivity = new AtomicReference<Boolean>();

	/**
	 * La red utilizada actualmente. Null si no hay conectividad
	 */
	private final AtomicReference<NetworkInfo> activeNetwork = new AtomicReference<NetworkInfo>();

	private ConnectivityChangeListener connectivityListener;

	private ConnectivityManager connectivityManager;

	public ConnectivityChangeListener getConnectivityListener() {
		return connectivityListener;
	}

	public void setConnectivityListener(ConnectivityChangeListener connectivityListener) {
		this.connectivityListener = connectivityListener;
	}

	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String receivedAction = intent.getAction();
		String expectedAction = ConnectivityManager.CONNECTIVITY_ACTION;
		if (!expectedAction.equals(receivedAction)) {
			// Nos registraron mal no es el intent que esperábamos
			return;
		}
		onConnectivityChanged(intent);
	}

	/**
	 * Invocado cuando hay cambios de conectividad en la plataforma
	 * 
	 * @param intent
	 *            Intent con los datos del cambio
	 */
	protected void onConnectivityChanged(Intent intent) {
		// Establecemos la conectividad actual
		boolean currentConnectivity = getIsConnectedFrom(intent);
		Boolean previousConnectivity = this.hasConnectivity.get();
		this.hasConnectivity.set(currentConnectivity);

		// Establecemos la red actual
		NetworkInfo previousNetwork = activeNetwork.get();
		NetworkInfo currentNetwork = connectivityManager.getActiveNetworkInfo();
		this.activeNetwork.set(currentNetwork);

		if (connectivityListener == null) {
			// Nada más que hacer
			return;
		}
		boolean cambioLaConectividad = cambioLaConectividad(previousConnectivity, currentConnectivity);
		boolean cambioLaRed = cambioLaRedActiva(previousNetwork, currentNetwork);
		if (cambioLaConectividad || cambioLaRed) {
			// Es un cambio de estado, tenemos que notificarlo
			NetworkInfo network = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			connectivityListener.onConnectivityChanged(currentConnectivity, network, cambioLaRed);
		}
	}

	/**
	 * Indica si la red utilizada para brindar conectividad cambio
	 * 
	 * @param previousNetwork
	 *            La red utilizada anteriormente (puede ser null)
	 * @param currentNetwork
	 *            La red actual (puede ser null)
	 * @return true si se produjo un cambio de red. False si ambas redes son iguales (null o misma
	 *         red)
	 */
	private boolean cambioLaRedActiva(NetworkInfo previousNetwork, NetworkInfo currentNetwork) {
		if (previousNetwork == null) {
			boolean laNuevaRedYaNoEsNull = currentNetwork != null;
			return laNuevaRedYaNoEsNull;
		}
		if (currentNetwork == null) {
			boolean laViejaRedNoEraNull = previousNetwork != null;
			return laViejaRedNoEraNull;
		}
		// Si ambas son distitnas de null, comparamos por red
		int currentType = currentNetwork.getType();
		int previousType = previousNetwork.getType();
		if (previousType != currentType) {
			return true;
		}
		int currentSubtype = currentNetwork.getSubtype();
		int previousSubtype = previousNetwork.getSubtype();
		if (previousSubtype != currentSubtype) {
			return true;
		}
		return false;
	}

	/**
	 * Indica si se considera que cambio el estado de conectividad con el nuevo estado pasado
	 * 
	 * @param previousConnectivity
	 *            Estado de la conectividad previo (puede ser null)
	 * @param currentConnectivity
	 *            El estado de conectividad actual
	 * @return true si cambio el valor actual respecto del previo. Si dejo de ser null se considera
	 *         cambio
	 */
	private boolean cambioLaConectividad(Boolean previousConnectivity, boolean currentConnectivity) {
		boolean seMantuvoIgual = Boolean.valueOf(currentConnectivity).equals(previousConnectivity);
		return !seMantuvoIgual;
	}

	/**
	 * Devuelve el extra que indica si se tiene conectividad o no
	 * 
	 * @param intent
	 *            El intent escuchado
	 * @return true si el intent indica que se perdió conectividad
	 */
	public static boolean getIsConnectedFrom(Intent intent) {
		boolean isDisconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		return !isDisconnected;
	}

	public static ConnectivityObserver createObserving(Context contextoAndroid, ConnectivityChangeListener listener,
			IntentReceptor intentReceptor) {
		ConnectivityManager connectivityManager = ContextHelper.getConnectivityManager(contextoAndroid);
		ConnectivityObserver observer = new ConnectivityObserver();
		observer.connectivityListener = listener;
		observer.connectivityManager = connectivityManager;
		// La primera vez se llama siempre para indicar el estado actual
		intentReceptor.registerMessageReceiver(ConnectivityManager.CONNECTIVITY_ACTION, observer);
		return observer;
	}

}
