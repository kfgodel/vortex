/**
 * 24/12/2012 14:23:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.filtros;

import java.util.concurrent.Callable;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import ar.com.dgarcia.lang.conc.ReadWriteCoordinator;

/**
 * Esta clase es la implementación de la parte de un {@link ConjuntoDeCondiciones} que sincroniza
 * sus modificaciones
 * 
 * @author D. García
 */
public class ParteSincronizada implements ParteDeCondiciones {

	private Condicion condicionActual;

	private ListenerDeParteDeCondicion listener;

	private ReadWriteCoordinator coordinator;

	public static ParteSincronizada create(final ListenerDeParteDeCondicion listener,
			final ReadWriteCoordinator coordinator) {
		final ParteSincronizada parte = new ParteSincronizada();
		parte.listener = listener;
		parte.coordinator = coordinator;
		parte.condicionActual = SiempreFalse.getInstancia();
		return parte;
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ParteDeCondiciones#cambiarA(net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public void cambiarA(final Condicion nuevaCondicion) {
		if (nuevaCondicion.equals(condicionActual)) {
			// Si no hubo cambio evitamos el resto de las consecuencias
			return;
		}
		coordinator.doWriteOperation(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				condicionActual = nuevaCondicion;
				listener.onCambioDeCondicion(nuevaCondicion, ParteSincronizada.this);
				return null;
			}
		});
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ParteDeCondiciones#getCondicion()
	 */
	@Override
	public Condicion getCondicion() {
		return coordinator.doReadOperation(new Callable<Condicion>() {
			@Override
			public Condicion call() throws Exception {
				return condicionActual;
			}
		});
	}
}
