/**
 * 24/12/2012 14:14:34 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.sets.impl.condiciones.OrCompuesto;
import ar.com.dgarcia.lang.conc.ReadWriteCoordinator;

/**
 * Esta clase es la implemetación del {@link ConjuntoDeCondiciones} que sincroniza las operaciones
 * realizadas obre él para tener siempre un estado consistente
 * 
 * @author D. García
 */
public class ConjuntoSincronizado implements ConjuntoDeCondiciones, ListenerDeParteDeCondicion {

	private ListenerDeConjuntoDeCondiciones listener;

	private ReadWriteCoordinator coordinator;

	private List<ParteDeCondiciones> partes;

	private Condicion condicionActual;

	public List<ParteDeCondiciones> getPartes() {
		return partes;
	}

	public static ConjuntoSincronizado create(final ListenerDeConjuntoDeCondiciones listener) {
		final ConjuntoSincronizado conjunto = new ConjuntoSincronizado();
		conjunto.coordinator = ReadWriteCoordinator.create();
		conjunto.partes = new ArrayList<ParteDeCondiciones>();
		conjunto.listener = listener;
		return conjunto;
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ConjuntoDeCondiciones#crearNuevaParte()
	 */
	@Override
	public ParteDeCondiciones crearNuevaParte() {
		final ParteSincronizada parte = ParteSincronizada.create(this, coordinator);
		coordinator.doWriteOperation(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				partes.add(parte);
				onCambioEnElConjunto();
				return null;
			}
		});
		return parte;
	}

	/**
	 * Invocado cuando se produce un cambio en el estado de este conjunto
	 */
	protected void onCambioEnElConjunto() {
		final Condicion condicionUnificada = unificarCondicionesExceptuandoA(null);
		if (condicionUnificada.equals(condicionActual)) {
			// No es necesario notificar porque no hubo cambio de estado en el filtro global
			return;
		}
		// Es un filtro distinto del que informamos la última vez
		condicionActual = condicionUnificada;
		this.listener.onCambioDeCondicionEn(this, condicionActual);
	}

	/**
	 * Genera una condición unificada de todas las partes
	 * 
	 * @param parteExcluida
	 *            La parte que será excluida de la condicion resultante
	 * @return La condicion que reune a todas las partes excepto la indicada
	 */
	private Condicion unificarCondicionesExceptuandoA(final ParteDeCondiciones parteExcluida) {
		final List<Condicion> condiciones = new ArrayList<Condicion>();
		final List<ParteDeCondiciones> allPartes = getPartes();
		for (final ParteDeCondiciones parte : allPartes) {
			if (parte.equals(parteExcluida)) {
				// Esta no la queremos incluir
				continue;
			}
			final Condicion condicionParcial = parte.getCondicion();
			condiciones.add(condicionParcial);
		}
		if (condiciones.isEmpty()) {
			// No hay condiciones, el mensaje no debería pasar?
			return SiempreFalse.getInstancia();
		}
		if (condiciones.size() == 1) {
			// Como optimización si es sólo una no creamos un operador innecesario
			final Condicion unicaCondicion = condiciones.get(0);
			return unicaCondicion;
		}

		final Condicion condicionUnificada = OrCompuesto.create(condiciones);
		return condicionUnificada;
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ConjuntoDeCondiciones#eliminarParte(net.gaia.vortex.router.impl.filtros.ParteDeCondiciones)
	 */
	@Override
	public void eliminarParte(final ParteDeCondiciones parteDeCondicion) {
		coordinator.doWriteOperation(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				partes.remove(parteDeCondicion);
				onCambioEnElConjunto();
				return null;
			}
		});
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ListenerDeParteDeCondicion#onCambioDeCondicion(net.gaia.vortex.core.api.condiciones.Condicion,
	 *      net.gaia.vortex.router.impl.filtros.ParteDeCondiciones)
	 */
	@Override
	public void onCambioDeCondicion(final Condicion nuevaCondicion, final ParteDeCondiciones parte) {
		onCambioEnElConjunto();
	}
}
