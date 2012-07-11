package net.gaia.vortex.vorterix;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.comm.R;
import net.gaia.vortex.vorterix.messages.HandlerDeMensajesDeChat;
import net.gaia.vortex.vorterix.messages.MensajeDeChat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;

public class VorterixActivity extends CustomListActivity<MensajeDeChat> implements HandlerDeMensajesDeChat {

	private final List<MensajeDeChat> allMessages = new ArrayList<MensajeDeChat>();

	private VorterixClient vorterixClient;

	private EditText userTxt;
	private EditText messageTxt;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.main;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();

		// Primero preparamos el cliente para poder enviar los mensajes
		vorterixClient = VorterixClient.create();
		vorterixClient.setHandlerDeMensajes(this);
		vorterixClient.conectarAlaRed(new InetSocketAddress("kfgodel.info", 61616));

		userTxt = ViewHelper.findEditText(R.id.userIdTxt, getContentView());
		messageTxt = ViewHelper.findEditText(R.id.mesasageTxt, getContentView());

		Button sendButton = ViewHelper.findButton(R.id.sendBtn, getContentView());
		sendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onEnviarApretado();
			}
		});
	}

	/**
	 * @see net.gaia.vortex.vorterix.messages.HandlerDeMensajesDeChat#onMensajeDeChatRecibido(net.gaia.vortex.vorterix.messages.MensajeDeChat)
	 */
	public void onMensajeDeChatRecibido(final MensajeDeChat mensaje) {
		// Tenemos que mostrarlo en el thread principal
		runOnUiThread(new Runnable() {
			public void run() {
				agregarMensaje(mensaje);
			}
		});
	}

	/**
	 * Agrega el mensaje pasado a los motrados
	 * 
	 * @param mensaje
	 *            El mensaje a mostrar
	 */
	protected void agregarMensaje(MensajeDeChat mensaje) {
		allMessages.add(mensaje);
		notificarCambioEnLosDatos();
	}

	/**
	 * Invocado al apretar enviar
	 */
	protected void onEnviarApretado() {
		String autorActual = userTxt.getText().toString();
		String textoEnviado = messageTxt.getText().toString();
		MensajeDeChat mensajeDeChat = MensajeDeChat.create(autorActual, textoEnviado);
		vorterixClient.send(mensajeDeChat);
		messageTxt.setText("");
		agregarMensaje(mensajeDeChat);
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
		return allMessages;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<MensajeDeChat> getElementRenderBlock() {
		return new RenderBlock<MensajeDeChat>() {
			public void render(View itemView, MensajeDeChat item, LayoutInflater inflater) {
				TextView autorTxt = ViewHelper.findTextView(R.id.authorTxt, itemView);
				autorTxt.setText(item.getAutor());

				TextView contenidoTxt = ViewHelper.findTextView(R.id.contenidoTxt, itemView);
				contenidoTxt.setText(item.getCuerpo());
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
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

}