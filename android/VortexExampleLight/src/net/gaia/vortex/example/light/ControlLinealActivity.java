/**
 * 16/03/2013 16:04:59 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.android.client.VortexRoot;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.impl.LuzRemota;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import ar.com.iron.helpers.ViewHelper;

/**
 * 
 * @author D. García
 */
public class ControlLinealActivity extends LuzActivitySupport {

	private Nodo nodoGlobal;
	private PortalBidi portal;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.control_lineal;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		super.setUpComponents();

		SeekBar barraControl = ViewHelper.findSeekBar(R.id.barraControl, getContentView());
		barraControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Nada que hacer
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// nada que hacer
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				onBarraCambiada(progress);
			}
		});
	}

	/**
	 * Invocado al cambiar la barra de progreso
	 * 
	 * @param nuevoValor
	 */
	protected void onBarraCambiada(int nuevoValor) {
		getLuz().cambiarA(nuevoValor);
	}

	/**
	 * @see net.gaia.vortex.example.light.LuzActivitySupport#crearLuz()
	 */
	@Override
	protected Luz crearLuz() {
		nodoGlobal = VortexRoot.getNode();
		portal = PortalBidi.create(VortexRoot.getProcessor());
		portal.conectarCon(nodoGlobal);
		nodoGlobal.conectarCon(portal);
		return LuzRemota.create(portal);
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		portal.desconectarDe(nodoGlobal);
		nodoGlobal.desconectarDe(portal);
		super.onDestroy();
	}
}
