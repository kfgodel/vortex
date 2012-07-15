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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.service.VortexAndroidAccess;
import net.gaia.vortex.android.service.VortexProviderService;
import net.gaia.vortex.comm.api.messages.MensajeDeChat;
import net.gaia.vortex.comm.config.ConfiguracionVortexComm;
import net.gaia.vortex.comm.config.RepositorioDeConfiguracion;
import net.gaia.vortex.comm.intents.AbrirCanalIntent;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
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
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
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
	private final List<MensajeDeChat> mensajes = new ArrayList<MensajeDeChat>();
	private EditText textoTxt;
	private LocalServiceConnector<VortexAndroidAccess> vortexConnector;
	private Portal portalEmisor;
	private Portal portalReceptor;
	private String nombreDeUsuario;
	private String canalActual;

	private ImageView conectadoImg;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		AbrirCanalIntent intentRecibido = new AbrirCanalIntent(getIntent());
		canalActual = intentRecibido.getNombreDelCanal();

		TextView nombreCanalTxt = ViewHelper.findTextView(R.id.nombreCanalTxt, getContentView());
		nombreCanalTxt.setText(canalActual);

		textoTxt = ViewHelper.findEditText(R.id.textoTxt, getContentView());
		Button agregarBtn = ViewHelper.findButton(R.id.enviarBtn, getContentView());
		agregarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onEnviarClickeado();
			}
		});
		WidgetHelper.habilitarBotonEnPresenciaDeTexto(agregarBtn, textoTxt);

		conectadoImg = ViewHelper.findImageView(R.id.conectadoImg, getContentView());
		mostrarEstadoDeConexion(false);

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

		cargarNombreDeUsuarioDesdeLaConfig();
	}

	/**
	 * Carga el nombre de usuario definido en la configuración
	 */
	private void cargarNombreDeUsuarioDesdeLaConfig() {
		RepositorioDeConfiguracion repo = new RepositorioDeConfiguracion(getContext());
		ConfiguracionVortexComm configuracion = repo.getConfiguracion();
		this.nombreDeUsuario = configuracion.getNombreDeUsuario();
	}

	/**
	 * Cambia el estado del icono de conexión
	 * 
	 * @param conectado
	 *            true si se debe mostrar conectado, false si desconectado
	 */
	private void mostrarEstadoDeConexion(boolean conectado) {
		int resourceId = R.drawable.ic_status_disconnected;
		if (conectado) {
			resourceId = R.drawable.ic_status_connected;
		}
		conectadoImg.setImageResource(resourceId);
	}

	/**
	 * Invocado al disponer de la conexión con vortex
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexDisponible(VortexAndroidAccess vortexForAndroid) {
		// Creamos un portal para comunicarnos con el nodo central
		TaskProcessor procesador = vortexForAndroid.getProcesadorCentral();
		Nodo nodoCentral = vortexForAndroid.getNodoCentral();
		this.portalReceptor = PortalMapeador.createForIOWith(procesador, nodoCentral);
		this.portalReceptor.recibirCon(new HandlerTipado<MensajeDeChat>(SiempreTrue.getInstancia()) {
			public void onMensajeRecibido(MensajeDeChat mensaje) {
				onMensajeRecibidoDesdeVortex(mensaje);
			}
		});

		this.portalEmisor = PortalMapeador.createForIOWith(procesador, nodoCentral);
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
	 * Invocado al desconectarse del servicio vortex
	 * 
	 * @param disconnectedIntercomm
	 */
	protected void onVortexNoDisponible(VortexAndroidAccess vortexForAndroid) {
		// Desconectamos el portal del nodo central
		Nodo nodoCentral = vortexForAndroid.getNodoCentral();
		this.portalEmisor.desconectarDe(nodoCentral);
		this.portalReceptor.desconectarDe(nodoCentral);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		vortexConnector.unbindFromService(this);
		super.onDestroy();
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
		if (portalEmisor == null) {
			ToastHelper.create(getContext()).showLong("Aún no conectado a vortex");
			return;
		}
		MensajeDeChat mensaje = MensajeDeChat.create(nombreDeUsuario, textoAEnviar, canalActual);
		portalEmisor.enviar(mensaje);
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
