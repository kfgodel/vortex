/**
 * 24/09/2012 13:02:11 Copyright (C) 2011 Darío L. García
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

import android.webkit.WebView;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.helpers.ViewHelper;
import ar.dgarcia.econamics.android.intents.MostrarEscaneadoIntent;
import ar.dgarcia.econamics.android.portales.PortalAndroid;

/**
 * Esta clase se encarga de mostrar los datos de un archivo escaneado
 * 
 * @author D. García
 */
public class MostrarEscaneadoActivity extends CustomActivity {

	private PortalAndroid portal;
	private WebView webView;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	public int getLayoutIdForActivity() {
		return R.layout.activity_mostrar_escaneado;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		portal = PortalAndroid.create();

		webView = ViewHelper.findInnerViewAs(WebView.class, R.id.vista_imagen, getContentView());
		webView.getSettings().setBuiltInZoomControls(true);

		MostrarEscaneadoIntent mensaje = new MostrarEscaneadoIntent(getIntent());
		Long idDelArchivo = mensaje.getIdDelArchivo();
		mostrarArchivoConId(idDelArchivo);
	}

	/**
	 * Muestra el contenido del archivo indicado por ID consultandolo via web
	 */
	private void mostrarArchivoConId(Long idDelArchivo) {
		String urlDelArchivo = EconamicsServer.PREFIJO_URL_ARCHIVOS + idDelArchivo;
		webView.loadUrl(urlDelArchivo);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		portal.closeAndDispose();
		super.onDestroy();
	}

}
