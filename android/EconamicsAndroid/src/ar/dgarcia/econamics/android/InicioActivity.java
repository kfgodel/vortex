/**
 * 23/09/2012 12:13:40 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.econamics.android;

import net.gaia.vortex.android.service.connector.VortexSocketConectorService;
import net.gaia.vortex.android.service.connector.intents.CambioDeConectividadVortex;
import net.gaia.vortex.android.service.connector.intents.ConectarConServidorVortexIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;

/**
 * Esta clase representa el activity que permite navegar al resto de las secciones de la aplicación
 * 
 * @author D. García
 */
public class InicioActivity extends CustomActivity {

	private Button botonConectar;
	private Button botonDesconectar;
	private Button botonProbar;
	private Button botonNuevos;
	private Button botonCargados;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.activity_inicio;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();
		botonConectar = ViewHelper.findButton(R.id.boton_conectar, getContentView());
		botonConectar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onConectarClickeado();
			}
		});

		botonDesconectar = ViewHelper.findButton(R.id.boton_desconectar, getContentView());
		botonDesconectar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onDesconectarClickeado();
			}
		});

		botonProbar = ViewHelper.findButton(R.id.boton_probar_conexion, getContentView());
		botonProbar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onProbarConexionClickeado();
			}
		});

		botonNuevos = ViewHelper.findButton(R.id.boton_nuevos, getContentView());
		botonNuevos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onNuevoClickeado();
			}
		});

		botonCargados = ViewHelper.findButton(R.id.boton_comprobantes, getContentView());
		botonCargados.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onCargadosClickeado();
			}
		});

		mostrarBotonesParaDesconectado();

	}

	/**
	 * Invocado cuando el usuario quiere ir a la seccion de facturas ya cargadas
	 */
	protected void onCargadosClickeado() {
		ToastHelper.create(getContext()).showShort("No construido todavia");
	}

	/**
	 * Invocado cuando el usuario quiere ir a la seccion de pendientes
	 */
	protected void onNuevoClickeado() {
		ToastHelper.create(getContext()).showShort("No construido todavia");
	}

	/**
	 * Invocado por el usuario para probar que hay conexión
	 */
	protected void onProbarConexionClickeado() {
		ToastHelper.create(getContext()).showShort("Todavía no construido...");
	}

	/**
	 * Invocado al clickear en el boton para desconectar
	 */
	protected void onDesconectarClickeado() {
		pasarAEstadoDesconectado();
	}

	/**
	 * Cambia el estado mostrado a desconectado
	 */
	private void pasarAEstadoDesconectado() {
		mostrarBotonesParaDesconectado();
		stopService(new Intent(this, VortexSocketConectorService.class));
	}

	/**
	 * Oculta los botones disponibles cuando esta conectado
	 */
	private void mostrarBotonesParaDesconectado() {
		botonNuevos.setEnabled(false);
		botonCargados.setEnabled(false);
		botonProbar.setVisibility(View.GONE);
		botonDesconectar.setVisibility(View.GONE);
		botonConectar.setVisibility(View.VISIBLE);
	}

	/**
	 * Invocado al clickear el boton para conectarse a vortex
	 */
	protected void onConectarClickeado() {
		mostrarBotonesParaConectado();

		// Iniciamos la conexión al server
		String hostDelServidor = "192.168.1.130";
		// El 60221 es debug
		Integer numeroDePuerto = 60220;
		startService(new ConectarConServidorVortexIntent(getContext(), hostDelServidor, numeroDePuerto));
	}

	/**
	 * Oculta el boton conectar y permite realizar las acciones cuando esta conectado
	 */
	private void mostrarBotonesParaConectado() {
		botonNuevos.setEnabled(true);
		botonCargados.setEnabled(true);
		botonConectar.setVisibility(View.GONE);
		botonDesconectar.setVisibility(View.VISIBLE);
		botonProbar.setVisibility(View.VISIBLE);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#initMessageReceivers()
	 */
	@Override
	public void initMessageReceivers() {
		registerMessageReceiver(CambioDeConectividadVortex.ACTION, new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				CambioDeConectividadVortex mensaje = new CambioDeConectividadVortex(intent);
				boolean estaConectado = mensaje.estaConectado();
				if (!estaConectado) {
					onDesconexionDelServer(mensaje.getCausa());
				}
			}
		});
	}

	/**
	 * Invocado cuando no se puede conectar con el servidor o cuando se produce una desconexión
	 * estando conectado
	 * 
	 * @param descripcion
	 */
	protected void onDesconexionDelServer(String descripcion) {
		ToastHelper.create(getContext()).showLong(
				"Error en la conexión. Reconectar para continuar. Detalle: " + descripcion);
		pasarAEstadoDesconectado();
	}
}
