/**
 * 13/06/2012 00:54:35 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.coding.caching;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase representa un referencia weak a un singleton que es regenerable.<br>
 * Cada vez que se solicita el singleton, si no existe, se crea una nueva instancia única para todos
 * los threads que permanecerá viva meintras se referencie fuertemente
 * 
 * @author D. García
 */
public class WeakSingleton<T> {

	private final Instantiator<Object, T> valueCreator;
	private WeakReference<T> referencia;
	private final ReentrantLock lockParaCreacion;

	/**
	 * Crea una nueva referencia para la cual se define creador
	 */
	public WeakSingleton(final Instantiator<Object, T> valueCreator) {
		this.valueCreator = valueCreator;
		lockParaCreacion = new ReentrantLock();
	}

	/**
	 * Devuelve la instancia actual del singleton, creando una si no existe.<br>
	 * Esta operación es bloqueante mientras se crea la instancia para todos los threads
	 * concurrentes
	 * 
	 * @return El objeto que representa el singleton
	 */
	public T get() {
		if (referencia == null) {
			return crearNuevaInstancia();
		}
		final T instancia = referencia.get();
		if (instancia == null) {
			return crearNuevaInstancia();
		}
		return instancia;
	}

	/**
	 * Crea una nueva instancia en forma atómica asegurando que ningún otro thread cree la instancia
	 * 
	 * @return La instancia creada como nuevo singleton
	 */
	private T crearNuevaInstancia() {
		boolean locked;
		try {
			locked = lockParaCreacion.tryLock(60, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			throw new UnhandledConditionException(
					"El thread actual fue interrumpido al esperar para crear un singleton weak");
		}
		if (!locked) {
			throw new UnhandledConditionException(
					"Paso un minuto y no fue posible adquirir el lock para crear el singleton. Hay otro thread con el lock colgado?");
		}
		try {
			// Aunque tengamos el lock es posible que otro thread acabe de liberarlo con una
			// instancia ya creada
			if (referencia != null) {
				final T instanciaPrevia = referencia.get();
				if (instanciaPrevia != null) {
					// Otro thread acaba de crear el singleton, no necesitamos hacerlo nosotros
					return instanciaPrevia;
				}
			}
			// Si a este punto no hay instancia entonces nadie más puede crearla. Lo hacemos
			// nosotros
			final T instancia = valueCreator.instantiate(null);
			referencia = new WeakReference<T>(instancia);
			return instancia;
		} finally {
			lockParaCreacion.unlock();
		}
	}

}
