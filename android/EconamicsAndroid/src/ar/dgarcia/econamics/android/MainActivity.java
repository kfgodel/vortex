package ar.dgarcia.econamics.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.service.connector.VortexSocketConectorService;
import net.gaia.vortex.android.service.connector.intents.CambioDeConectividadVortex;
import net.gaia.vortex.android.service.connector.intents.ConectarConServidorVortexIntent;
import net.gaia.vortex.android.service.provider.VortexProviderAccess;
import net.gaia.vortex.android.service.provider.VortexProviderService;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.sets.impl.And;
import net.gaia.vortex.sets.impl.ValorEsperadoIgual;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.dgarcia.econamics.messages.ArchivoPendienteTo;
import ar.dgarcia.econamics.messages.PedidoDeArchivosPendientes;
import ar.dgarcia.econamics.messages.RespuestaDeArchivosPendientes;

/**
 * Esta clase representa el activity principal
 * 
 * @author D. García
 */
public class MainActivity extends CustomListActivity<ArchivoPendienteTo> {

	private final List<ArchivoPendienteTo> archivos = new ArrayList<ArchivoPendienteTo>();
	private Button botonRefresh;
	private LocalServiceConnector<VortexProviderAccess> vortexConnector;
	private PortalMapeador portalPropio;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		botonRefresh = ViewHelper.findButton(R.id.boton_refresh, getContentView());
		botonRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onRefreshClickeado();
			}
		});
		// Desactivamos hasta tener conexion con vortex
		botonRefresh.setEnabled(false);

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

		// Iniciamos la conexión al server
		String hostDelServidor = "192.168.1.130";
		// El 60221 es debug
		Integer numeroDePuerto = 60220;
		startService(new ConectarConServidorVortexIntent(getContext(), hostDelServidor, numeroDePuerto));
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		stopService(new Intent(getContext(), VortexSocketConectorService.class));
		vortexConnector.unbindFromService(this);
		super.onDestroy();
	}

	protected void onVortexDisponible(VortexProviderAccess intercommObject) {
		TaskProcessor procesadorCentral = intercommObject.getProcesadorCentral();
		Nodo nodoCentral = intercommObject.getNodoCentral();
		portalPropio = PortalMapeador.createForIOWith(procesadorCentral, nodoCentral);
		portalPropio.recibirCon(new HandlerTipado<RespuestaDeArchivosPendientes>(And.create(ValorEsperadoIgual.a(
				RespuestaDeArchivosPendientes.NOMBRE_APLICACION_ECONAMICS,
				RespuestaDeArchivosPendientes.aplicacion_FIELD), ValorEsperadoIgual.a(
				RespuestaDeArchivosPendientes.DISCRIMINADOR, RespuestaDeArchivosPendientes.discriminador_FIELD))) {
			public void onMensajeRecibido(RespuestaDeArchivosPendientes respuesta) {
				onRespuestaDesdeVortex(respuesta);
			}
		});
		botonRefresh.setEnabled(true);
	}

	/**
	 * Recibimos la respuesta en otro thread de vortex
	 * 
	 * @param respuesta
	 *            La respuesta con los archivos
	 */
	protected void onRespuestaDesdeVortex(final RespuestaDeArchivosPendientes respuesta) {
		// Actualizamos la vista en el thread principal
		runOnUiThread(new Runnable() {
			public void run() {
				List<ArchivoPendienteTo> nuevosArchivos = respuesta.getArchivosPendientes();
				getCustomAdapter().replaceAll(nuevosArchivos);
				ToastHelper.create(getContext())
						.showShort(nuevosArchivos.size() + " archivos encontrados en el server");
			}
		});
	}

	protected void onVortexNoDisponible(VortexProviderAccess disconnectedIntercomm) {
		botonRefresh.setEnabled(false);
		Nodo nodoCentral = disconnectedIntercomm.getNodoCentral();
		this.portalPropio.desconectarDe(nodoCentral);
		nodoCentral.desconectarDe(portalPropio);
		this.portalPropio = null;
	}

	/**
	 * Invocado cuando el usuario quiere actualizar la lista de archivos
	 */
	protected void onRefreshClickeado() {
		PedidoDeArchivosPendientes pedido = PedidoDeArchivosPendientes.create();
		portalPropio.enviar(pedido);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	public ContextMenuItem<? extends CustomableListActivity<ArchivoPendienteTo>, ArchivoPendienteTo>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<ArchivoPendienteTo> getElementList() {
		return archivos;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<ArchivoPendienteTo> getElementRenderBlock() {
		return new RenderBlock<ArchivoPendienteTo>() {
			public void render(View itemView, ArchivoPendienteTo item, LayoutInflater inflater) {
				TextView nombreTxt = ViewHelper.findTextView(R.id.nombre_txt, itemView);
				nombreTxt.setText(item.getNombreDeArchivo());

				TextView sizeTxt = ViewHelper.findTextView(R.id.tamanio_txt, itemView);
				double tamanioEnKilos = item.getTamanio() / 1024d;
				String tamanio = String.format("%,.3fKb", tamanioEnKilos);
				sizeTxt.setText(tamanio);

				TextView fechaTxt = ViewHelper.findTextView(R.id.fecha_txt, itemView);
				String fecha = String.format("%tF", new Date(item.getUltimaModificacion()));
				fechaTxt.setText(fecha);
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	public int getLayoutIdForElements() {
		return R.layout.archivo_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.activity_main;
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
		if (estaConectado) {
			ToastHelper.create(getContext()).showShort("Conectado al servidor");
		} else {
			ToastHelper.create(getContext()).showLong("Desconectado del servidor");
		}
	}
}
