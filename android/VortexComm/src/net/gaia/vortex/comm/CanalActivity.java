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

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.comm.model.MensajeDeChat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.helpers.WidgetHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;

/**
 * Esta clase representa la pantalla de comunicación dentro de un canal
 * 
 * @author D. García
 */
public class CanalActivity extends CustomListActivity<MensajeDeChat> {

	private final List<MensajeDeChat> mensajes = new ArrayList<MensajeDeChat>();
	private EditText textoTxt;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		textoTxt = ViewHelper.findEditText(R.id.textoTxt, getContentView());
		Button agregarBtn = ViewHelper.findButton(R.id.enviarBtn, getContentView());
		agregarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onEnviarClickeado();
			}
		});
		WidgetHelper.habilitarBotonEnPresenciaDeTexto(agregarBtn, textoTxt);
	}

	/**
	 * Invocado al clickear el boton para enviar
	 */
	protected void onEnviarClickeado() {
		String textoAEnviar = textoTxt.getText().toString();
		enviarMensaje(textoAEnviar);
	}

	/**
	 * Envía el mensaje indicado como texto
	 * 
	 * @param textoAEnviar
	 *            El texto que se enviará en el canal
	 */
	private void enviarMensaje(String textoAEnviar) {
		MensajeDeChat mensaje = MensajeDeChat.create(textoAEnviar, "usuario");
		onMensajeRecibido(mensaje);
	}

	/**
	 * Invocado al recibir un mensaje desde vortex
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	public void onMensajeRecibido(MensajeDeChat mensaje) {
		mensajes.add(0, mensaje);
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
		return mensajes;
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
