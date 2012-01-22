/**
 * 21/01/2012 20:10:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteos;

import java.util.concurrent.LinkedBlockingQueue;

import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase es la implementación de la memoria de mensajes que permite reconocer duplicados
 * 
 * @author D. García
 */
public class MemoriaDeMensajesImpl implements MemoriaDeMensajes {

	private static final int MAX_MILLIS_TRYING_TO_ADD = 3000;

	private static final int MAX_MENSAJES_REGISTRADOS = 1000;

	private MensajesEnEspera esperandoAcuse;

	private LinkedBlockingQueue<MensajeVortex> registrados;

	/**
	 * @see net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes#registrarSiNoRecuerdaA(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public boolean registrarSiNoRecuerdaA(final MensajeVortex mensaje) {
		final boolean yaLoRecordaba = registrados.contains(mensaje);
		if (yaLoRecordaba) {
			return false;
		}
		// Registramos el momento de inicio de intentar agregar a la memoria
		final long startTime = getCurrentTime();
		boolean reallyAdded = registrados.offer(mensaje);
		while (!reallyAdded) {
			// Llegamos al límite de la capacidad de memoria, tenemos que limpiar
			// Sacamos el más viejo e intentamos de nuevo
			registrados.poll();
			reallyAdded = registrados.add(mensaje);
			if (!reallyAdded) {
				// Verificamos si ya llevamos demasiado tiempo intentándolo
				final long elapsedMillis = getCurrentTime() - startTime;
				if (elapsedMillis > MAX_MILLIS_TRYING_TO_ADD) {
					throw new UnhandledConditionException("Se excedió el límite de espera[" + MAX_MILLIS_TRYING_TO_ADD
							+ "ms] para registrar el mensaje sin poder hacerlo");
				}
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes#getEsperandoAcuseDeConsumo()
	 */
	@Override
	public MensajesEnEspera getEsperandoAcuseDeConsumo() {
		return esperandoAcuse;
	}

	public static MemoriaDeMensajesImpl create() {
		final MemoriaDeMensajesImpl memoria = new MemoriaDeMensajesImpl();
		memoria.esperandoAcuse = MensajesEnEspera.create();
		memoria.registrados = new LinkedBlockingQueue<MensajeVortex>(MAX_MENSAJES_REGISTRADOS);
		return memoria;
	}
}
