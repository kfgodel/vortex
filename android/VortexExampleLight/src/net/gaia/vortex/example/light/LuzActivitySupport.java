/**
 * 16/03/2013 16:37:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.example.light;

import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.LuzChangeListener;
import net.gaia.vortex.example.light.model.impl.LuzEnMemoria;
import android.widget.ImageView;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.helpers.ViewHelper;

/**
 * 
 * @author D. García
 */
public abstract class LuzActivitySupport extends CustomActivity {

	private Luz luz;
	private TextView textoValor;
	private ImageView imagenEncendida;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		luz = LuzEnMemoria.create();

		textoValor = ViewHelper.findTextView(R.id.textoValor, getContentView());
		imagenEncendida = ViewHelper.findImageView(R.id.imagen_encendida, getContentView());
		actualizarLuzRepresentada(luz.getValorActual());

		luz.addChangeListener(new LuzChangeListener() {
			@Override
			public void onLuzChanged(int nuevoValor) {
				onCambioDeLuz(nuevoValor);
			}
		});
	}

	/**
	 * Invocado al cambiar el estado actual de la luz
	 * 
	 * @param nuevoValor
	 *            El valor actual
	 */
	protected void onCambioDeLuz(int nuevoValor) {
		actualizarLuzRepresentada(nuevoValor);
	}

	/**
	 * Modifica la representación de la luz al valor pasado
	 * 
	 * @param nuevoValor
	 */
	private void actualizarLuzRepresentada(int nuevoValor) {
		textoValor.setText(String.valueOf(nuevoValor));
		int nuevoAlpha = (int) (((nuevoValor) / 100f) * 255);
		imagenEncendida.setAlpha(nuevoAlpha);
	}

	public Luz getLuz() {
		return luz;
	}
}
