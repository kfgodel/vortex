/**
 * 24/03/2013 12:50:28 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.client.VortexRoot;
import net.gaia.vortex.android.messages.impl.ClienteDeNexoMessager;
import net.gaia.vortex.server.impl.RealizarConexiones;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import ar.com.iron.android.extensions.services.remote.api.support.StringRemoteAddress;

/**
 * Esta clase representa un servicio local que permite extender la mensajería vortex conectandose a
 * una aplicación remota para que los mensajes lleguen a servidores externos.<br>
 * <br>
 * Este servicio se conectará a la aplicación remota con el primer componente bindeado y se
 * desconectará con el último. Todos los componentes que usen este servicio deben binderase aunque
 * los mensajes deben enviarlos por el {@link VortexRoot}
 * 
 * @author D. García
 */
public class VortexConnectorService extends Service {

	private Binder dummyBinder;

	private ClienteDeNexoMessager cliente;

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		TaskProcessor processor = VortexRoot.getProcessor();
		StringRemoteAddress remoteAddress = StringRemoteAddress.create(VortexAcceptorService.class.getName());
		RealizarConexiones estrategia = RealizarConexiones.con(VortexRoot.getNode());
		cliente = ClienteDeNexoMessager.create(this, processor, remoteAddress, estrategia);
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		if (dummyBinder == null) {
			dummyBinder = new Binder();
			cliente.conectarAServicioRemoto();
		}
		return dummyBinder;
	}

	/**
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		dummyBinder = null;
		cliente.desconectar();
		return false;
	}
}
