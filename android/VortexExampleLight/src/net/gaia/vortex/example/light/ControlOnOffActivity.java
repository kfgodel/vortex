package net.gaia.vortex.example.light;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import ar.com.iron.helpers.ViewHelper;

public class ControlOnOffActivity extends LuzActivitySupport {

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.control_on_off;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();

		ToggleButton onOffBoton = ViewHelper.findToggleButton(R.id.botonDeEncendido, getContentView());
		onOffBoton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				onOnOffChanged(isChecked);
			}
		});
	}

	/**
	 * Invocado al cambiar el estado del boton
	 * 
	 * @param isChecked
	 */
	protected void onOnOffChanged(boolean isChecked) {
		int nuevoValor = (isChecked) ? 100 : 0;
		getLuz().cambiarA(nuevoValor);
	}
}
