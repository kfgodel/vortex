/**
 * 16/03/2013 17:57:16 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.android.client.VortexRoot;
import net.gaia.vortex.android.service.VortexConnectorService;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.impl.ControladorRemoto;
import net.gaia.vortex.example.light.model.impl.LuzEnMemoria;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * @author D. García
 */
public class ShowLuzActivity extends LuzActivitySupport {

	private PortalBidi portal;
	private ControladorRemoto controlador;
	private Nodo nodoGlobal;
	private ServiceConnection connection;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.show_luz;
	}

	/**
	 * @see net.gaia.vortex.example.light.LuzActivitySupport#crearLuz()
	 */
	@Override
	protected Luz crearLuz() {
		return LuzEnMemoria.create();
	}

	/**
	 * @see net.gaia.vortex.example.light.LuzActivitySupport#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();

		// Creamos el portal para el controlador
		nodoGlobal = VortexRoot.getNode();
		portal = PortalBidi.create(VortexRoot.getProcessor());
		portal.conectarCon(nodoGlobal);
		nodoGlobal.conectarCon(portal);

		controlador = ControladorRemoto.create(getLuz(), portal);

		connection = new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
			}
		};
		boolean binded = bindService(new Intent(this, VortexConnectorService.class), connection,
				Context.BIND_AUTO_CREATE);
		if (!binded) {
			throw new RuntimeException("No pudimos conectarnos");
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		unbindService(connection);
		portal.desconectarDe(nodoGlobal);
		nodoGlobal.desconectarDe(portal);
		super.onDestroy();
	}

}
