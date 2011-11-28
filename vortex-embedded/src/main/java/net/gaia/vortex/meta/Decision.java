/**
 * 27/11/2011 21:29:57 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.meta;

/**
 * Esta clase junta las decisiones no explicitables en el código de este proyecto
 * 
 * @author D. García
 */
public class Decision {

	public static final String FALTAN_MAS_VALIDACIONES_DE_MENSAJE = "Hay que agregar más validaciones";
	public static final String TODAVIA_NO_IMPLEMENTE_EL_GENERADOR_DE_MENSAJES = "El codigo no va a andar hasta que lo haga";
	public static final String TODAVIA_NO_IMPLEMENTE_LA_MEMORIA_DE_MENSAJES = "El codigo no va a andar hasta que lo haga";
	public static final String NO_ESTA_IMPLEMENTADO_EL_INTERES_DEL_RECEPTOR = "Debería agregar la lista de tags que le interesan";
	public static final String LA_TAREA_DE_ENVIO_DE_CONFIRMACION_REQUIERE_EMISOR_REAL = "Asumimos que el emisor ya fue definido antes de pasar a la tarea";
	public static final String LA_TAREA_DE_ENVIO_DE_CONFIRMACION_ASIGNA_EL_ID_DEL_MENSAJE = "No es necesario definir id de mensaje ya que se obtiene de contexto";

}
