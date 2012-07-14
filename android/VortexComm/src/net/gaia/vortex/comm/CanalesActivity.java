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

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.android.service.VortexAndroidAccess;
import net.gaia.vortex.android.service.VortexProviderService;
import net.gaia.vortex.comm.config.ConfiguracionVortexComm;
import net.gaia.vortex.comm.config.RepositorioDeConfiguracion;
import net.gaia.vortex.comm.intents.AbrirCanalIntent;
import net.gaia.vortex.comm.model.Canal;
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
public class CanalesActivity extends CustomListActivity<Canal> {
	private final List<Canal> canales = new ArrayList<Canal>();
	private EditText canalTxt;
	private ImageView conectadoImg;
	private LocalServiceConnector<VortexAndroidAccess> vortexConnector;

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
		vortexConnector.setConnectionListener(new LocalServiceConnectionListener<VortexAndroidAccess>() {
			public void onServiceDisconnection(final VortexAndroidAccess disconnectedIntercomm) {
			}

			public void onServiceConnection(final VortexAndroidAccess intercommObject) {
				onVortexDisponible(intercommObject);
			}
		});
		vortexConnector.bindToService(this);

		cargarCanalesDesdeConfig();
	}

	/**
	 * Carga los canales definidos en la configuración
	 */
	private void cargarCanalesDesdeConfig() {
		RepositorioDeConfiguracion repo = new RepositorioDeConfiguracion(getContext());
		ConfiguracionVortexComm configuracionActual = repo.getConfiguracion();
		List<String> canalesDelUsuario = configuracionActual.getCanalesDelUsuario();
		for (String nombreDelCanal : canalesDelUsuario) {
			Canal canal = Canal.create(nombreDelCanal);
			canales.add(canal);
		}
	}

	/**
	 * Invocado al disponer de la conexión con vortex
	 * 
	 * @param vortexForAndroid
	 */
	protected void onVortexDisponible(VortexAndroidAccess vortexForAndroid) {
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
		agregarCanal(nombreDelCanal);
	}

	/**
	 * Agrega un nuevo canal en los disponibles sólo si no existe antes
	 * 
	 * @param nombreDelCanal
	 *            El nombre del canal a agregar
	 */
	private void agregarCanal(String nombreDelCanal) {
		for (Canal canalExistente : canales) {
			if (canalExistente.getNombreDelCanal().equals(nombreDelCanal)) {
				ToastHelper.create(getContext()).showLong("Ya existe un canal con el nombre: " + nombreDelCanal);
				return;
			}
		}
		Canal nuevoCanal = Canal.create(nombreDelCanal);
		canales.add(nuevoCanal);
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
	public ContextMenuItem<? extends CustomableListActivity<Canal>, Canal>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	public List<Canal> getElementList() {
		return canales;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	public RenderBlock<Canal> getElementRenderBlock() {
		return new RenderBlock<Canal>() {
			public void render(View itemView, final Canal item, LayoutInflater inflater) {
				ImageView statusImg = ViewHelper.findImageView(R.id.statusImg, itemView);
				if (item.getConectado()) {
					statusImg.setImageResource(R.drawable.ic_status_connected);
				} else {
					statusImg.setImageResource(R.drawable.ic_status_disconnected);
				}
				TextView canalTxt = ViewHelper.findTextView(R.id.canalTxt, itemView);
				canalTxt.setText(item.getNombreDelCanal());
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
	 * @param canal
	 *            El canal clickeado
	 */
	protected void onCanalClickeado(Canal canal) {
		abrirCanal(canal);
	}

	/**
	 * Abre una nueva ventana en la que el usuario puede chatear con el canal pasado
	 * 
	 * @param canalAbierto
	 *            El canal a abrir
	 */
	private void abrirCanal(Canal canalAbierto) {
		String nombreDelCanal = canalAbierto.getNombreDelCanal();
		startActivity(new AbrirCanalIntent(getContext(), nombreDelCanal));
	}

	/**
	 * Invocado al apretar remove en un item
	 * 
	 * @param canal
	 *            El canal a quitar
	 */
	protected void onRemoveClickeado(Canal canal) {
		quitarCanal(canal);
	}

	/**
	 * Elimina el canal pasado de la lista de canales
	 * 
	 * @param canalRemovido
	 *            El canal a quitar
	 */
	private void quitarCanal(Canal canalRemovido) {
		canales.remove(canalRemovido);
		notificarCambioEnLosDatos();
		String nombreDelCanal = canalRemovido.getNombreDelCanal();
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

}
