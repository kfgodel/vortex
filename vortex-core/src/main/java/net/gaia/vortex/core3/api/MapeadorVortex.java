/**
 * 14/06/2012 20:33:20 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api;

import net.gaia.vortex.core3.api.atomos.MensajeVortex;

/**
 * Esta interfaz representa un elemento utilizado por los portales para la conversión de obejtos de
 * clientes en mensajes de vortex y viceversa.<br>
 * Normalmente las implementaciones de este mapeador deben asegurar que las operaciones son
 * reversibles (o sea {@link Object} <-> {@link MensajeVortex}) y que son thread-safe, por lo que
 * varios threads pueden convertir en paralelo con la misma instancia de mapeador
 * 
 * @author D. García
 */
public interface MapeadorVortex {

	/**
	 * Convierte el objeto pasado interpretado como mensaje, en un mensaje vortex.<br>
	 * Si el objeto pasado posee estado que no es representable como mensaje vortex, normalmente es
	 * descartado
	 * 
	 * @return El mensaje vortex que representa el objeto pasado
	 */
	public MensajeVortex convertirAVortex(Object mensaje);

	/**
	 * Convierte el mensaje vortex pasado al tipo esperado.<br>
	 * Si el mensaje vortex tiene estado que no es representable en el tipo indicado, normalmente es
	 * descartado
	 * 
	 * @param mensaje
	 *            El mensaje vortex a convertir
	 * @param tipoEsperado
	 *            El tipo esperado de la conversion
	 * @return El objeto que representa el mensaje indicado pero con el tipo esperado
	 */
	public <T> T convertirDesdeVortex(MensajeVortex mensaje, Class<T> tipoEsperado);

}
