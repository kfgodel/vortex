/**
 * 14/06/2012 20:33:20 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.api.conversiones;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.portal.ErrorDeMapeoVortexException;

/**
 * Esta interfaz representa un elemento utilizado por los portales para la conversión de objetos de
 * clientes en mensajes de vortex y viceversa.<br>
 * <br>
 * Normalmente las implementaciones de este mapeador deben asegurar que las operaciones son
 * reversibles (o sea {@link Object} <-> {@link MensajeVortex}) y que son thread-safe (o sea varios
 * threads pueden convertir en paralelo con la misma instancia de mapeador).<br>
 * <br>
 * La implementaciones de esta interfaz normalmente usarán implementaciones de
 * {@link ConversorDeContenidoVortex}
 * 
 * @author D. García
 */
public interface ConversorDeMensajesVortex {

	/**
	 * Convierte el objeto pasado interpretado como mensaje, en un mensaje vortex.<br>
	 * Si el objeto pasado posee estado que no es representable como mensaje vortex, normalmente es
	 * descartado
	 * 
	 * @return El mensaje vortex que representa el objeto pasado
	 * @throws ErrorDeMapeoVortexException
	 *             Si se produce un error en la conversión
	 */
	public MensajeVortex convertirAVortex(Object mensaje) throws ErrorDeMapeoVortexException;

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
	 * @throws ErrorDeMapeoVortexException
	 *             Si se produce un error en al conversión
	 */
	public <T> T convertirDesdeVortex(MensajeVortex mensaje, Class<T> tipoEsperado) throws ErrorDeMapeoVortexException;

}
