/**
 * 09/07/2012 22:15:27 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.service.VortexSharedProcessor;
import net.gaia.vortex.android.service.connector.VortexSocketConectorService;
import net.gaia.vortex.android.service.connector.intents.CambioDeConectividadVortex;
import net.gaia.vortex.android.service.connector.intents.ConectarConServidorVortex;
import net.gaia.vortex.android.service.provider.VortexProviderAccess;
import net.gaia.vortex.android.service.provider.VortexProviderService;
import net.gaia.vortex.comm.api.CanalDeChat;
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal;
import net.gaia.vortex.comm.app.VortexCommApplication;
import net.gaia.vortex.comm.config.ConfiguracionVortexComm;
import net.gaia.vortex.comm.config.RepositorioDeConfiguracion;
import net.gaia.vortex.comm.impl.ClienteDeChatVortexImpl;
import net.gaia.vortex.comm.intents.AbrirCanalIntent;
import net.gaia.vortex.core.api.Nodo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
 * Esta clase permite administrar los canales de la app
 * 
 * @author D. García
 */
public class CanalesActivity extends CustomListActivity<CanalDeChat> {
	private EditText canalTxt;
	private ImageView conectadoImg;
	private LocalServiceConnector<VortexProviderAccess> vortexConnector;
	private ClienteDeChatVortex clienteVortex;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		canalTxt = ViewHelper.findEditText(R.id.canalTxt, getContentView());
		Button agregarBtn = ViewHelper.findButton(R.id.agregarCanalBtn, getContentView());
		agregarBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onAgregarClickeado();
			}
		});
		WidgetHelper.habilitarBotonEnPresenciaDeTexto(agregarBtn, canalTxt);

		conectadoImg = ViewHelper.findImageView(R.id.conectadoImg, getContentView());
		mostrarEstadoDeConexion(false);

		// Intentamos conectarnos con el servicio vortex
		vortexConnector = LocalServiceConnector.create(VortexProviderService.class);
		vortexConnector.setConnectionListener(new LocalServiceConnectionListener<VortexProviderAccess>() {
			public void onServiceDisconnection(final VortexProviderAccess disconnectedIntercomm) {
				onVortexNoDisponible(disconnectedIntercomm);
			}

			public void onServiceConnection(final VortexProviderAccess intercommObject) {
				onVortexDisponible(intercommObject);
			}
		});
		vortexConnector.bindToService(this);

		cargarDatosDesdeConfig();
	}

	/**
	 * Carga los canales definidos en la configuración e inicia el servicio de conexion
	 */
	private void cargarDatosDesdeConfig() {
		RepositorioDeConfiguracion repo = new RepositorioDeConfiguracion(getContext());
		ConfiguracionVortexComm configuracionActual = repo.getConfiguracion();

		String nombreDeUsuario = configuracionActual.getNombreDeUsuario();
		List<String> canalesDelUsuario = configuracionActual.getCanalesDelUsuario();

		TaskProcessor procesador = VortexSharedProcessor.getProcessor();
		clienteVortex = ClienteDeChatVortexImpl.create(procesador, nombreDeUsuario);
		for (String nombreDelCanal : canalesDelUsuario) {
			clienteVortex.agregarCanal(nombreDelCanal);
		}
		clienteVortex.setListenerDeEstado(new ListenerDeEstadoDeCanal() {
			public void onCanalVacio(CanalDeChat canal) {
				onCambioDeEstadoEnCanales();
			}

			public void onCanalHabitado(CanalDeChat canal) {
				onCambioDeEstadoEnCanales();
			}
		});
		VortexCommApplication.I.reemplazarClienteActual(clienteVortex);

		iniciarServicioDeConexion(configuracionActual);
	}

	/**
	 * Invocado al cambiar el estado de los canales
	 */
	protected void onCambioDeEstadoEnCanales() {
		runOnUiThread(new Runnable() {
			public void run() {
				notificarCambioEnLosDatos();
			}
		});
	}

	/**
	 * Inicia el servisio de conexión al servidor
	 * 
	 * @param configuracionActual
	 *            La configuración desde la cual tomar los datos
	 */
	private void iniciarServicioDeConexion(ConfiguracionVortexComm configuracionActual) {
		String hostDelServidor = configuracionActual.getHostDelServidor();
		Integer numeroDePuerto = configuracionActual.getNumeroDePuerto();
		startService(new ConectarConServidorVortex(getContext(), hostDelServidor, numeroDePuerto));
	}

	/**
	 * Invocado al disponer de la conexión con vortex
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexDisponible(VortexProviderAccess vortexForAndroid) {
		Nodo nodoCentral = vortexForAndroid.getNodoCentral();
		clienteVortex.conectarA(nodoCentral);
	}

	/**
	 * Invocado al desconectarse de android
	 * 
	 * @param disconnectedIntercomm
	 */
	protected void onVortexNoDisponible(VortexProviderAccess disconnectedIntercomm) {
		clienteVortex.desconectar();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		detenerServicioDeConexion();
		clienteVortex.closeAndDispose();
		vortexConnector.unbindFromService(this);
		super.onDestroy();
	}

	/**
	 * Detiene el servicio de conexion a vortex
	 */
	private void detenerServicioDeConexion() {
		stopService(new Intent(getContext(), VortexSocketConectorService.class));
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
	 * Invocado al clickear en agregar en agregar canal
	 */
	protected void onAgregarClickeado() {
		String nombreDelCanal = canalTxt.getText().toString();
		canalTxt.setText("");
		agregarCanal(nombreDelCanal);
	}

	/**
	 * Agrega un nuevo canal en los disponibles sólo si no existe antes
	 * 
	 * @param nombreDelCanal
	 *            El nombre del canal a agregar
	 */
	private void agregarCanal(String nombreDelCanal) {
		List<CanalDeChat> allCanales = clienteVortex.getCanales();
		for (CanalDeChat canalExistente : allCanales) {
			if (canalExistente.getNombre().equals(nombreDelCanal)) {
				ToastHelper.create(getContext()).showLong("Ya existe un canal con el nombre: " + nombreDelCanal);
				return;
			}
		}
		clienteVortex.agregarCanal(nombreDelCanal);
		notificarCambioEnLosDatos();
		agregarCanalEnConfig(nombreDelCanal);
	}

	/**
	 * Agrega el canal en al configuración de la app
	 * 
	 * @param nombreDelCanal
	 *            El nombre del canal a agregar
	 */
	private void agregarCanalEnConfig(String nombreDelCanal) {
		RepositorioDeConfiguracion repo = new RepositorioDeConfiguracion(getContext());
		ConfiguracionVortexComm configuracionActual = repo.getConfiguracion();
		configuracionActual.agregarCanal(nombreDelCanal);
		repo.setConfiguracion(configuracionActual);
		repo.saveChanges();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	public ContextMenuItem<? extends CustomableListActivity<CanalDeChat>, CanalDeChat>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<CanalDeChat> getElementList() {
		return clienteVortex.getCanales();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<CanalDeChat> getElementRenderBlock() {
		return new RenderBlock<CanalDeChat>() {
			public void render(View itemView, final CanalDeChat item, LayoutInflater inflater) {
				ImageView statusImg = ViewHelper.findImageView(R.id.statusImg, itemView);
				if (item.getOtrosPresentes().isEmpty()) {
					statusImg.setImageResource(R.drawable.ic_status_disconnected);
				} else {
					statusImg.setImageResource(R.drawable.ic_status_connected);
				}
				TextView canalTxt = ViewHelper.findTextView(R.id.canalTxt, itemView);
				canalTxt.setText(item.getNombre());
				canalTxt.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						onCanalClickeado(item);
					}

				});

				ImageView removeImg = ViewHelper.findImageView(R.id.removeImg, itemView);
				removeImg.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						onRemoveClickeado(item);
					}
				});
			}
		};
	}

	/**
	 * Invocado al clickear el usuario sobre un canal
	 * 
	 * @param CanalDeChat
	 *            El canal clickeado
	 */
	protected void onCanalClickeado(CanalDeChat CanalDeChat) {
		abrirCanal(CanalDeChat);
	}

	/**
	 * Abre una nueva ventana en la que el usuario puede chatear con el canal pasado
	 * 
	 * @param canalAbierto
	 *            El canal a abrir
	 */
	private void abrirCanal(CanalDeChat canalAbierto) {
		String nombreDelCanal = canalAbierto.getNombre();
		startActivity(new AbrirCanalIntent(getContext(), nombreDelCanal));
	}

	/**
	 * Invocado al apretar remove en un item
	 * 
	 * @param CanalDeChat
	 *            El canal a quitar
	 */
	protected void onRemoveClickeado(CanalDeChat CanalDeChat) {
		quitarCanal(CanalDeChat);
	}

	/**
	 * Elimina el canal pasado de la lista de canales
	 * 
	 * @param canalRemovido
	 *            El canal a quitar
	 */
	private void quitarCanal(CanalDeChat canalRemovido) {
		String nombreDelCanal = canalRemovido.getNombre();
		clienteVortex.quitarCanal(nombreDelCanal);
		notificarCambioEnLosDatos();
		quitarCanalDeLaConfig(nombreDelCanal);
		ToastHelper.create(getContext()).showShort("Quitado canal: " + nombreDelCanal);
	}

	/**
	 * Quita el canal de la configuración de la app
	 * 
	 * @param nombreDelCanal
	 *            El canal a quitar
	 */
	private void quitarCanalDeLaConfig(String nombreDelCanal) {
		RepositorioDeConfiguracion repo = new RepositorioDeConfiguracion(getContext());
		ConfiguracionVortexComm configuracionActual = repo.getConfiguracion();
		configuracionActual.quitarCanal(nombreDelCanal);
		repo.setConfiguracion(configuracionActual);
		repo.saveChanges();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	public int getLayoutIdForElements() {
		return R.layout.canal_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.canales_section;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#initMessageReceivers()
	 */
	@Override
	public void initMessageReceivers() {
		registerMessageReceiver(CambioDeConectividadVortex.ACTION, new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				CambioDeConectividadVortex mensaje = new CambioDeConectividadVortex(intent);
				onCambioDeConectividadVortex(mensaje.estaConectado());
			}
		});
	}

	/**
	 * Invocado cuando se produce un cambio de conectividad con el servidor
	 * 
	 * @param estaConectado
	 */
	protected void onCambioDeConectividadVortex(boolean estaConectado) {
		mostrarEstadoDeConexion(estaConectado);
		if (estaConectado) {
			clienteVortex.actualizarPresentismo();
		}
	}
}
