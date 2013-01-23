/**
 * 23/01/2013 09:52:08 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.memoria;

import java.util.Map;
import java.util.concurrent.Callable;

import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.colecciones.maps.LRUCache;
import ar.com.dgarcia.lang.conc.ReadWriteCoordinator;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una memoria de mensajes que puede reconstruir los mensajes que recuerda
 * 
 * @author D. García
 */
public class MemoriaDeMetamensajes implements MemoriaDeMensajes {
	/**
	 * Mapa en el que registramos los ids como mapas
	 */
	private LRUCache<Map<String, Object>, MensajeVortex> mensajesPorId;
	public static final String mensajesPorId_FIELD = "mensajesPorId";

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
	public static MemoriaDeMetamensajes create(final int cantidadMaximaRegistrada) {
		final MemoriaDeMetamensajes memoria = new MemoriaDeMetamensajes();
		memoria.concurrentCoordinator = ReadWriteCoordinator.create();
		memoria.mensajesPorId = new LRUCache<Map<String, Object>, MensajeVortex>(cantidadMaximaRegistrada);
		return memoria;
	}

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#registrarNuevo(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean registrarNuevo(final MensajeVortex mensaje) {
		final IdDeMensaje idNuevo = mensaje.getIdDeMensaje();
		final boolean yaExistia = comprobarSiYaExiste(idNuevo);
		if (yaExistia) {
			// Ya sabemos que no es nuevo
			return false;
		}
		final boolean agregadoComoNuevo = intentarAgregarComoNuevo(mensaje);
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
	private boolean intentarAgregarComoNuevo(final MensajeVortex mensaje) {
		final boolean agregado = concurrentCoordinator.doWriteOperation(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return unsyncIntentarAgregarComoNuevo(mensaje);
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
	protected Boolean unsyncIntentarAgregarComoNuevo(final MensajeVortex mensaje) {
		final ContenidoVortex contenido = mensaje.getContenido();
		final Object object = contenido.get(ContenidoVortex.ID_DE_MENSAJE_KEY);
		final IdDeMensaje idNuevo = mensaje.getIdDeMensaje();
		if (idNuevo == null) {
			throw new FaultyCodeException("Se recibió un mensaje[" + mensaje + "] sin ID");
		}

		// Tenemos que comprobar nuevamente porque ahora tenemos acceso exclusivo
		final Boolean yaExiste = unsyncComprobarSiYaExiste(idNuevo);
		if (yaExiste) {
			// Alguien nos ganó de mano entre que comprobamos antes y que pudimos acceder
			// exclusivamente
			return false;
		}
		final MensajeVortex mensajePrevio = mensajesPorId.put(idNuevo, mensaje);
		if (mensajePrevio != null) {
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
			@Override
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
		final MensajeVortex mensajeRegistrado = this.mensajesPorId.get(idNuevo);
		final boolean yaExistia = mensajeRegistrado != null;
		return yaExistia;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(mensajesPorId_FIELD, mensajesPorId.size()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes#tieneRegistroDe(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean tieneRegistroDe(final MensajeVortex mensaje) {
		final IdDeMensaje idDelMensaje = mensaje.getIdDeMensaje();
		final boolean tieneRegistroPrevio = comprobarSiYaExiste(idDelMensaje);
		return tieneRegistroPrevio;
	}

}
