/**
 * 24/03/2013 13:12:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.server;

import java.util.List;

import net.gaia.vortex.android.client.VortexRoot;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.mensaje.HandlerTipado;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;

/**
 * Esta clase es un logger de los mensajes que permite ver la secuencia recibida
 * 
 * @author D. García
 */
public class LoggerActivity extends CustomListActivity<MensajeVortex> {

	private Portal portalDeMensajes;

	private List<MensajeVortex> mensajesRecibidos;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	public ContextMenuItem<? extends CustomableListActivity<MensajeVortex>, MensajeVortex>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<MensajeVortex> getElementList() {
		return mensajesRecibidos;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<MensajeVortex> getElementRenderBlock() {
		return new RenderBlock<MensajeVortex>() {
			public void render(View itemView, MensajeVortex item, LayoutInflater inflater) {
				TextView textView = ViewHelper.findTextView(R.id.texto_mensaje, itemView);
				textView.setText(item.toString());
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	public int getLayoutIdForElements() {
		return R.layout.mensaje_view;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.logger_view;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		portalDeMensajes = PortalBidi.create(VortexRoot.getProcessor());
		Nodo nodoGlobal = VortexRoot.getNode();
		portalDeMensajes.conectarCon(nodoGlobal);
		nodoGlobal.conectarCon(portalDeMensajes);

		portalDeMensajes.recibirCon(new HandlerTipado<MensajeVortex>(SiempreTrue.getInstancia()) {
			public void onMensajeRecibido(MensajeVortex mensaje) {
				onMensajeVortexRecibido(mensaje);
			}
		});
	}

	/**
	 * Invocado al recibir un mensaje desde vortex
	 */
	protected void onMensajeVortexRecibido(final MensajeVortex mensaje) {
		runOnUiThread(new Runnable() {
			public void run() {
				mensajesRecibidos.add(mensaje);
				notificarCambioEnLosDatos();
			}
		});
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Nodo nodoGlobal = VortexRoot.getNode();
		nodoGlobal.desconectarDe(portalDeMensajes);
		portalDeMensajes.desconectarDe(nodoGlobal);

		super.onDestroy();
	}
}
