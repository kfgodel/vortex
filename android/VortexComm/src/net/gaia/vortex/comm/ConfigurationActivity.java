/**
 * 09/07/2012 17:40:08 Copyright (C) 2011 Darío L. García
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

import java.net.InetSocketAddress;

import net.gaia.vortex.comm.config.ConfiguracionVortexComm;
import net.gaia.vortex.comm.config.RepositorioDeConfiguracion;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.android.helpers.WidgetHelper;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ActivityMenuItem;

/**
 * Esta pantalla permite definir la configuración de la aplicación
 * 
 * @author D. García
 */
public class ConfigurationActivity extends CustomActivity {

	private EditText userTxt;
	private EditText hostTxt;
	private EditText portTxt;
	private RepositorioDeConfiguracion repositorio;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.config_section;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		userTxt = ViewHelper.findEditText(R.id.userIdTxt, getContentView());
		hostTxt = ViewHelper.findEditText(R.id.hostUrlTxt, getContentView());
		portTxt = ViewHelper.findEditText(R.id.portNumberTxt, getContentView());
		Button guardarbutton = ViewHelper.findButton(R.id.guardarBtn, getContentView());
		guardarbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onGuardarClickeado();
			}
		});
		WidgetHelper.habilitarBotonEnPresenciaDeTexto(guardarbutton, userTxt, hostTxt, portTxt);
		repositorio = new RepositorioDeConfiguracion(getContext());
		cargarValoresDesdeLaConfiguracion();
	}

	/**
	 * Intenta cargar los valores actuales para cada uno de los campos desde la configuración
	 */
	private void cargarValoresDesdeLaConfiguracion() {
		ConfiguracionVortexComm configuracion = repositorio.getConfiguracion();
		String hostDelServidor = configuracion.getHostDelServidor();
		this.hostTxt.setText(hostDelServidor);

		String nombreDeUsuario = configuracion.getNombreDeUsuario();
		this.userTxt.setText(nombreDeUsuario);

		Integer puertoDelServidor = configuracion.getNumeroDePuerto();
		this.portTxt.setText(String.valueOf(puertoDelServidor));
	}

	/**
	 * Invocado al apretar guardar
	 */
	protected void onGuardarClickeado() {
		String numeroDePuertoComoTexto = portTxt.getText().toString();
		Integer numeroDePuerto = validarPuerto(numeroDePuertoComoTexto);
		if (numeroDePuerto == null) {
			ToastHelper.create(getContext()).showLong(
					"El valor \"" + numeroDePuertoComoTexto + "\" debe ser un numero entre 1-65535");
			return;
		}
		String hostDelServer = hostTxt.getText().toString();
		validarHost(hostDelServer, numeroDePuerto);

		String nombreDeUsuario = userTxt.getText().toString();
		ConfiguracionVortexComm configuracion = repositorio.getConfiguracion();
		configuracion.setNombreDeUsuario(nombreDeUsuario);
		configuracion.setHostDelServidor(hostDelServer);
		configuracion.setNumeroDePuerto(numeroDePuerto);
		repositorio.setConfiguracion(configuracion);
		repositorio.saveChanges();
		ToastHelper.create(getContext()).showShort("Configuración guardada!");
	}

	/**
	 * Valida que el server sea válido, para lo cual intenta conectarse o determinar el host.<br>
	 * Si no lo logra solo muestra un mensaje
	 * 
	 * @param hostDelServer
	 *            EL host de prueba
	 * @param numeroDePuerto
	 *            El numero de puerto a probar
	 */
	private void validarHost(final String hostDelServer, final Integer numeroDePuerto) {
		// A partir de la 3.0 no está permitido invocar red en el thread principal y no podemos
		// validar en el main
		AsyncTask<Void, Void, Boolean> validationTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				InetSocketAddress hostAddress = new InetSocketAddress(hostDelServer, numeroDePuerto);
				boolean unresolved = hostAddress.isUnresolved();
				return unresolved;
			}

			@Override
			protected void onPostExecute(Boolean unresolved) {
				if (unresolved) {
					ToastHelper.create(getContext()).showLong(
							"No fue posible conectar con el host[" + hostDelServer + ":" + numeroDePuerto
									+ "] al validar los datos");
				}
			}
		};
		validationTask.execute();
	}

	/**
	 * Valida que el numero de puerto esté bien
	 * 
	 * @param numeroDePuertoComoTexto
	 *            El número de puerto a validar como texto
	 * @return El puerto validado
	 */
	private Integer validarPuerto(String numeroDePuertoComoTexto) {
		int puertoNumerico;
		try {
			puertoNumerico = Integer.parseInt(numeroDePuertoComoTexto);
		} catch (NumberFormatException e) {
			return null;
		}
		if (puertoNumerico < 1 || puertoNumerico > 65535) {
			return null;
		}
		return puertoNumerico;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#getMenuItems()
	 */
	@Override
	public ActivityMenuItem<? extends CustomActivity>[] getMenuItems() {
		return ConfigurationMenu.values();
	}
}
