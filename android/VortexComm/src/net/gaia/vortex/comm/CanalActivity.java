/**
 * 09/07/2012 22:49:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm;

import java.util.List;

import net.gaia.vortex.comm.api.CanalDeChat;
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal;
import net.gaia.vortex.comm.api.ListenerDeMensajesDeChat;
import net.gaia.vortex.comm.api.messages.MensajeDeChat;
import net.gaia.vortex.comm.app.VortexCommApplication;
import net.gaia.vortex.comm.impl.ListenerNuloDeCanal;
import net.gaia.vortex.comm.intents.AbrirCanalIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.helpers.WidgetHelper;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;

/**
 * Esta clase representa la pantalla de comunicación dentro de un canal
 * 
 * @author D. García
 */
public class CanalActivity extends CustomListActivity<MensajeDeChat> {

	private EditText textoTxt;
	private ImageView conectadoImg;
	private CanalDeChat canalActual;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		AbrirCanalIntent intentRecibido = new AbrirCanalIntent(getIntent());
		String nombreDelCanalAbierto = intentRecibido.getNombreDelCanal();
		ClienteDeChatVortex clienteActual = VortexCommApplication.I.getClienteActual();
		if (clienteActual != null) {
			canalActual = clienteActual.getCanal(nombreDelCanalAbierto);
		}
		if (canalActual == null) {
			// Tamos en problema
			ToastHelper.create(getContext()).showLong(
					"Se produjo un error interno, no fue posible acceder al canal: " + nombreDelCanalAbierto);
			finish();
			return;
		}

		TextView nombreCanalTxt = ViewHelper.findTextView(R.id.nombreCanalTxt, getContentView());
		nombreCanalTxt.setText(nombreDelCanalAbierto);

		textoTxt = ViewHelper.findEditText(R.id.textoTxt, getContentView());
		Button agregarBtn = ViewHelper.findButton(R.id.enviarBtn, getContentView());
		agregarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onEnviarClickeado();
			}
		});
		WidgetHelper.habilitarBotonEnPresenciaDeTexto(agregarBtn, textoTxt);

		conectadoImg = ViewHelper.findImageView(R.id.conectadoImg, getContentView());
		mostrarEstadoDeConexion();

		canalActual.setListenerDeEstado(new ListenerDeEstadoDeCanal() {
			public void onCanalVacio(CanalDeChat canal) {
				onCambioDeEstadoDelCanal();
			}

			public void onCanalHabitado(CanalDeChat canal) {
				onCambioDeEstadoDelCanal();
			}
		});
		canalActual.setListenerDeMensajes(new ListenerDeMensajesDeChat() {
			public void onMensajeNuevo(MensajeDeChat mensaje) {
				onMensajeRecibidoDesdeVortex(mensaje);
			}
		});
	}

	/**
	 * Invocado cuando se produce un cambio en la cantidad de presentes en el canal
	 */
	protected void onCambioDeEstadoDelCanal() {
		runOnUiThread(new Runnable() {
			public void run() {
				mostrarEstadoDeConexion();
			}
		});
	}

	/**
	 * Cambia el estado del icono de conexión
	 */
	private void mostrarEstadoDeConexion() {
		boolean noHayNadieMas = canalActual.getOtrosPresentes().isEmpty();
		int resourceId = R.drawable.ic_status_connected;
		if (noHayNadieMas) {
			resourceId = R.drawable.ic_status_disconnected;
		}
		conectadoImg.setImageResource(resourceId);
	}

	/**
	 * Invocado en un thread de vortex al recibir un mensaje
	 * 
	 * @param mensaje
	 *            El mensaje recibido en vortex
	 */
	protected void onMensajeRecibidoDesdeVortex(final MensajeDeChat mensaje) {
		runOnUiThread(new Runnable() {
			public void run() {
				onMensajeRecibido(mensaje);
			}
		});
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		canalActual.setListenerDeEstado(ListenerNuloDeCanal.getInstancia());
		canalActual.setListenerDeMensajes(ListenerNuloDeCanal.getInstancia());
		super.onDestroy();
	}

	/**
	 * Invocado al clickear el boton para enviar
	 */
	protected void onEnviarClickeado() {
		String textoAEnviar = textoTxt.getText().toString();
		textoTxt.setText("");
		enviarMensaje(textoAEnviar);
	}

	/**
	 * Envía el mensaje indicado como texto
	 * 
	 * @param textoAEnviar
	 *            El texto que se enviará en el canal
	 */
	private void enviarMensaje(String textoAEnviar) {
		canalActual.enviar(textoAEnviar);
	}

	/**
	 * Invocado al recibir un mensaje desde vortex
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	public void onMensajeRecibido(MensajeDeChat mensaje) {
		notificarCambioEnLosDatos();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	public ContextMenuItem<? extends CustomableListActivity<MensajeDeChat>, MensajeDeChat>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<MensajeDeChat> getElementList() {
		return canalActual.getMensajes();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<MensajeDeChat> getElementRenderBlock() {
		return new RenderBlock<MensajeDeChat>() {
			public void render(View itemView, MensajeDeChat item, LayoutInflater inflater) {
				TextView autorTxt = ViewHelper.findTextView(R.id.authorTxt, itemView);
				autorTxt.setText(item.getUsuario());
				TextView contenidoTxt = ViewHelper.findTextView(R.id.contenidoTxt, itemView);
				contenidoTxt.setText(item.getTexto());
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	public int getLayoutIdForElements() {
		return R.layout.message_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.canal_section;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

}
