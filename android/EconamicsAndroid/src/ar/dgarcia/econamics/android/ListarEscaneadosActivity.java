package ar.dgarcia.econamics.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.sets.impl.And;
import net.gaia.vortex.sets.impl.ValorEsperadoIgual;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.dgarcia.econamics.android.intents.MostrarEscaneadoIntent;
import ar.dgarcia.econamics.android.portales.PortalAndroid;
import ar.dgarcia.econamics.messages.ArchivoPendienteTo;
import ar.dgarcia.econamics.messages.PedidoDeArchivosPendientes;
import ar.dgarcia.econamics.messages.RespuestaDeArchivosPendientes;

/**
 * Esta clase representa el activity principal
 * 
 * @author D. García
 */
public class ListarEscaneadosActivity extends CustomListActivity<ArchivoPendienteTo> {

	private final List<ArchivoPendienteTo> archivos = new ArrayList<ArchivoPendienteTo>();
	private Button botonRefresh;
	private PortalAndroid portal;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		// Creamos el portal para mensajearnos con vortex
		portal = PortalAndroid.create();
		portal.getPortalVortex().recibirCon(
				new HandlerTipado<RespuestaDeArchivosPendientes>(And.create( //
						ValorEsperadoIgual.a(RespuestaDeArchivosPendientes.NOMBRE_APLICACION_ECONAMICS,
								RespuestaDeArchivosPendientes.aplicacion_FIELD), //
						ValorEsperadoIgual.a(RespuestaDeArchivosPendientes.DISCRIMINADOR,
								RespuestaDeArchivosPendientes.discriminador_FIELD))) {
					public void onMensajeRecibido(RespuestaDeArchivosPendientes respuesta) {
						onRespuestaDesdeVortex(respuesta);
					}
				});

		// El referesh pide los archivos al portal que creamos
		botonRefresh = ViewHelper.findButton(R.id.boton_refresh, getContentView());
		botonRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onRefreshClickeado();
			}
		});
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// Nos desconectamos de vortex
		portal.closeAndDispose();
		super.onDestroy();
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
				ToastHelper.create(getContext()).showShort(
						nuevosArchivos.size() + " archivos encontrados en el servidor");
			}
		});
	}

	/**
	 * Invocado cuando el usuario quiere actualizar la lista de archivos
	 */
	protected void onRefreshClickeado() {
		PedidoDeArchivosPendientes pedido = PedidoDeArchivosPendientes.create();
		portal.getPortalVortex().enviar(pedido);
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
		return R.layout.item_escaneado;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.activity_listar_pendientes;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onItemSelected(java.lang.Object)
	 */
	@Override
	protected void onItemSelected(ArchivoPendienteTo selectedItem) {
		Long idDeArchivo = selectedItem.getIdDeArchivo();
		startActivity(new MostrarEscaneadoIntent(this, idDeArchivo));
	}
}
