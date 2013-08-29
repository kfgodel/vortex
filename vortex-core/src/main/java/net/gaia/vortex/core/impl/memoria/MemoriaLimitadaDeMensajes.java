/**
 * 01/09/2012 01:31:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.memoria;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase implementa la memoria de mensajes limitando la cantidad a un valor prefijado y
 * eliminado los más viejos al llegar al límite
 * 
 * @author D. García
 */
public class MemoriaLimitadaDeMensajes implements MemoriaDeMensajes {
	private static final Logger LOG = LoggerFactory.getLogger(MemoriaLimitadaDeMensajes.class);

	/**
	 * Tiempo máximo que se espera antes de considerar que no fue posible lockear.<br>
	 * Esta magnitud debiera ser suficientemente grande para emular una espera infinita, pero no
	 * tanto como para que un programa se bloquee infinitamente
	 */
	public static final TimeMagnitude MAX_WAIT_FOR_LOCKS = TimeMagnitude.of(10 * 60, TimeUnit.SECONDS);

	private Set<IdDeMensaje> idsPorHash;

	private LinkedList<IdDeMensaje> idsPorOrden;
	public static final String idsPorOrden_FIELD = "idsPorOrden";

	private int cantidadMaxima;
	public static final String cantidadMaxima_FIELD = "cantidadMaxima";

	private ReentrantLock writeLock;

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#registrarNuevo(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public boolean registrarNuevo(final MensajeVortex mensaje) {
		final boolean yaExistia = tieneRegistroDe(mensaje);
		if (yaExistia) {
			return false;
		}
		boolean locked;
		try {
			locked = writeLock.tryLock(MAX_WAIT_FOR_LOCKS.getQuantity(), MAX_WAIT_FOR_LOCKS.getTimeUnit());
		}
		catch (final InterruptedException e) {
			LOG.debug("Mensaje no comprobado para registrarlo por ser interrumpida la espera", e);
			return false;
		}
		if (!locked) {
			throw new UnhandledConditionException(
					"Se acabó el tiempo de espera y no se pudo lockear la memoria para registrar mensaje: " + mensaje);
		}
		try {
			return intentarRegistrarNuevo(mensaje);
		}
		finally {
			writeLock.unlock();
		}
	}

	/**
	 * Intenta agregar un nuevo id a los registrados en esta memoria
	 * 
	 * @param mensaje
	 * @return false si no pudo agregarse o ya existía
	 */
	private boolean intentarRegistrarNuevo(final MensajeVortex mensaje) {
		final IdDeMensaje idAgregado = mensaje.getIdDeMensaje();
		final boolean agregado = idsPorHash.add(idAgregado);
		if (!agregado) {
			// Otro thread se adelantó y puso el mismo ID
			return false;
		}
		idsPorOrden.addLast(idAgregado);
		if (idsPorOrden.size() > cantidadMaxima) {
			// Si superamos la cantidad maxima quitamos el mas viejo
			final IdDeMensaje idQuitado = idsPorOrden.removeFirst();
			idsPorHash.remove(idQuitado);
		}
		return true;
	}

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#tieneRegistroDe(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public boolean tieneRegistroDe(final MensajeVortex mensaje) {
		final IdDeMensaje idDeMensaje = mensaje.getIdDeMensaje();
		final boolean yaRegistrado = idsPorHash.contains(idDeMensaje);
		return yaRegistrado;
	}

	public static MemoriaLimitadaDeMensajes create(final int cantidadMaximaDeRegistros) {
		final MemoriaLimitadaDeMensajes memoria = new MemoriaLimitadaDeMensajes();
		memoria.cantidadMaxima = cantidadMaximaDeRegistros;
		memoria.writeLock = new ReentrantLock();
		memoria.idsPorHash = new HashSet<IdDeMensaje>();
		memoria.idsPorOrden = new LinkedList<IdDeMensaje>();
		return memoria;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idsPorOrden_FIELD, idsPorOrden.size()).con(cantidadMaxima_FIELD, cantidadMaxima)
				.toString();
	}

}
