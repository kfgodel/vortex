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
	public static final String LA_TAREA_DE_ENVIO_DE_ACUSE_REQUIERE_EMISOR_REAL = "Asumimos que el emisor ya fue definido antes de pasar a la tarea";
	public static final String LA_TAREA_DE_ENVIO_DE_ACUSE_ASIGNA_EL_ID_DEL_MENSAJE = "No es necesario definir id de mensaje ya que se obtiene de contexto";
	public static final String EL_TIMEOUT_DE_RECEPCION_QUITA_EL_MENSAJE_DE_ESPERA = "El mensaje se saca de espera para evitar que se registre la confirmación si llega justo, y permite darlo por perdido";
	public static final String PARA_CONTINUAR_EL_RUTEO_DESPUES_DEL_ENVIO_SE_UTILIZAN_CONTADORES = "Quizás no es la mejor estrategia porque los contadores pueden no actualizarse. Quizás debería haber un timeout también?";
	public static final String EL_TIMEOUT_DE_CONSUMO_QUITA_EL_MENSAJE_DE_ESPERA = "El código asume que esto se mantiene";
	public static final String TODAVIA_NO_IMPLEMENTE_PRORROGA = "Tengo que agregar el calculo de espera para poder usar las prorrogas";
	public static final String TODAVIA_NO_IMPLEMENTE_REENVIO_DE_MENSAJE_PERDIDO = "Si el receptor nos devuelve que se perdio lo consideramos rechazo";
	public static final String CONFIO_EN_QUE_NO_HAY_ACUSES_REPETIDOS = "Si un nodo mandara acuses de más, habría que cambiar las listas por sets";
	public static final String LA_DIFERENCIA_DE_SETS_DE_TAGS_REQUIERE_LA_MISMA_IMPL_DE_SETS = "Los sets usados en la diferencia tienen que tener la misma relacion de equivalencia para que la differencia de bien";
	public static final String TODAVIA_NO_IMPLEMENTE_EL_AJUSTE_DE_PESOS = "No hay cambios en la elección de los receptores para el ruteo basado en los acuses";
	public static final String AL_QUITAR_TAG_SE_ELIMINA_SU_ENTRADA_DEL_MAPA = "Si no hay más interesados en un tag, se elimina del nodo";
	public static final String TODAVIA_NO_IMPLEMENTE_LA_LIMPIEZA_DE_TAREAS = "Las tareas para receptores cerrados se siguen ejecutando";
	public static final String EL_NODO_PROCESA_LOS_MENSAJES_DE_UN_RECEPTOR_EN_EL_ORDEN_QUE_LOS_RECIBE = "Un cliente puede confiar que un mensaje enviado posteriormente actuará con la base del enviado anteriormente";
}
