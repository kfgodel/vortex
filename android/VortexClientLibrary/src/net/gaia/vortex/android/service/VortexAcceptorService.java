/**
 * 23/03/2013 19:41:39 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.android.messages.api.ServidorMessageVortex;
import net.gaia.vortex.android.messages.impl.ServidorDeNexoMessager;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.server.impl.RealizarConexiones;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Esta clase representa el servicio accesible desde otras aplicaciones para recibir mensajes que
 * son enviados al {@link VortexRoot} para ser tratados por esta aplicación
 * 
 * @author D. García
 */
public class VortexAcceptorService extends Service {

	private ServidorMessageVortex acceptor;

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		// Las conexiones entrantes las vinculamos al nodo raiz
		TaskProcessor procesor = VortexRoot.getProcessor();
		EstrategiaDeConexionDeNexos estrategia = RealizarConexiones.con(VortexRoot.getNode());
		acceptor = ServidorDeNexoMessager.create(procesor, estrategia);
	}

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return acceptor.getServiceBinder();
	}

	/**
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		acceptor.allConnectionsClosed();
		return false;
	}
}
