package net.gaia.vortex.example.light;

import net.gaia.vortex.android.client.VortexRoot;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.impl.LuzRemota;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import ar.com.iron.helpers.ViewHelper;

public class ControlOnOffActivity extends LuzActivitySupport {

	private PortalBidi portal;
	private Nodo nodoRaiz;

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

	/**
	 * @see net.gaia.vortex.example.light.LuzActivitySupport#crearLuz()
	 */
	@Override
	protected Luz crearLuz() {
		nodoRaiz = VortexRoot.getNode();
		portal = PortalBidi.create(VortexRoot.getProcessor());
		portal.conectarCon(nodoRaiz);
		nodoRaiz.conectarCon(portal);

		return LuzRemota.create(portal);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		nodoRaiz.desconectarDe(portal);
		portal.desconectarDe(nodoRaiz);
		super.onDestroy();
	}
}
