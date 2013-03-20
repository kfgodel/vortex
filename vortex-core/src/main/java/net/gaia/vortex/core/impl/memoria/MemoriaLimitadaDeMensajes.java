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

import java.util.concurrent.Callable;

import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.colecciones.maps.LRUCache;
import ar.com.dgarcia.lang.conc.ReadWriteCoordinator;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa la memoria de mensajes limitando la cantidad a un valor prefijado y
 * eliminado los más viejos al llegar al límite
 * 
 * @author D. García
 */
public class MemoriaLimitadaDeMensajes implements MemoriaDeMensajes {

	/**
	 * Mapa utilizado como registro limitado LRU
	 */
	private LRUCache<IdDeMensaje, IdDeMensaje> idsRegistrados;
	public static final String idsRegistrados_FIELD = "idsRegistrados";

	private ReadWriteCoordinator concurrentCoordinator;

	/**
	 * Crea una nueva memoria limitada a la cantidad de mensajes indicados.<br>
	 * Los mensajes más accedidos serán conservados y los menos descartados al superar el tamaño
	 * máximo
	 * 
	 * @param cantidadMaximaRegistrada
	 *            El tamaño que esta memoria puede tener como máximo de mensajes registrados
	 * @return La memoria creada
	 */
	public static MemoriaLimitadaDeMensajes create(final int cantidadMaximaRegistrada) {
		final MemoriaLimitadaDeMensajes memoria = new MemoriaLimitadaDeMensajes();
		memoria.concurrentCoordinator = ReadWriteCoordinator.create();
		memoria.idsRegistrados = new LRUCache<IdDeMensaje, IdDeMensaje>(cantidadMaximaRegistrada);
		return memoria;
	}

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#registrarNuevo(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public boolean registrarNuevo(final MensajeVortex mensaje) {
		final IdDeMensaje idNuevo = mensaje.getIdDeMensaje();
		final boolean yaExistia = comprobarSiYaExiste(idNuevo);
		if (yaExistia) {
			// Ya sabemos que no es nuevo
			return false;
		}
		final boolean agregadoComoNuevo = intentarAgregarComoNuevo(idNuevo);
		return agregadoComoNuevo;
	}

	/**
	 * Intenta modificar esta memoria para registrar el ID como nuevo. Falla si otro thread nos gana
	 * de mano
	 * 
	 * @param idNuevo
	 *            El ID para registrar como nuevo
	 * @return true si fue posible agregarlo, false si al intentar agregarlo el estado actual de la
	 *         memoria ya lo tiene registrado
	 */
	private boolean intentarAgregarComoNuevo(final IdDeMensaje idNuevo) {
		if (idNuevo == null) {
			throw new FaultyCodeException("Se recibió un mensaje sin ID");
		}
		final boolean agregado = concurrentCoordinator.doWriteOperation(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				return unsyncIntentarAgregarComoNuevo(idNuevo);
			}
		});
		return agregado;
	}

	/**
	 * Intenta agregar el ID como nuevo al mapa interno. Esta operación no está sincronizada
	 * 
	 * @param idNuevo
	 *            El Id a agregar
	 * @return false si al intentar agregarlo, ya existia. True si fue posible agregarlo
	 */
	protected Boolean unsyncIntentarAgregarComoNuevo(final IdDeMensaje idNuevo) {
		// Tenemos que comprobar nuevamente porque ahora tenemos acceso exclusivo
		final Boolean yaExiste = unsyncComprobarSiYaExiste(idNuevo);
		if (yaExiste) {
			// Alguien nos ganó de mano entre que comprobamos antes y que pudimos acceder
			// exclusivamente
			return false;
		}
		final IdDeMensaje idPrevio = idsRegistrados.put(idNuevo, idNuevo);
		if (idPrevio != null) {
			// Por alguna extraña razón la comprobación previa dio false pero algo había
			return false;
		}
		return true;
	}

	/**
	 * Accede a esta memoria para verificar si el ID pasado como nuevo existía previamente, en cuyo
	 * caso devuelve true
	 * 
	 * @param idNuevo
	 *            El id a verificar como nuevo
	 * @return true si el ID ya estaba registrado, false si es realmente nuevo
	 */
	private boolean comprobarSiYaExiste(final IdDeMensaje idNuevo) {
		if (idNuevo == null) {
			throw new FaultyCodeException("Se recibió un mensaje sin ID");
		}
		final Boolean yaExiste = concurrentCoordinator.doReadOperation(new Callable<Boolean>() {
			
			public Boolean call() throws Exception {
				return unsyncComprobarSiYaExiste(idNuevo);
			}
		});
		return yaExiste;
	}

	/**
	 * Verifica en el mapa interno si el id ya está registrado.<br>
	 * Esta operación no está sincronizada con otros threads
	 * 
	 * @param idNuevo
	 *            El id a comprobar
	 * @return true si ya existía en el mapa, false si es nuevo
	 */
	protected Boolean unsyncComprobarSiYaExiste(final IdDeMensaje idNuevo) {
		final IdDeMensaje idRegistrado = this.idsRegistrados.get(idNuevo);
		final boolean yaExistia = idRegistrado != null;
		return yaExistia;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(idsRegistrados_FIELD, idsRegistrados.size()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#tieneRegistroDe(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public boolean tieneRegistroDe(final MensajeVortex mensaje) {
		final IdDeMensaje idDelMensaje = mensaje.getIdDeMensaje();
		final boolean tieneRegistroPrevio = comprobarSiYaExiste(idDelMensaje);
		return tieneRegistroPrevio;
	}

}
