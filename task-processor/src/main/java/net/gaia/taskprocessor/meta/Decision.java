/**
 * 19/11/2011 16:45:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.meta;

/**
 * Esta clase reune algunas decisiones para explicitarlas aunque no se pueda en código
 * 
 * @author D. García
 */
public class Decision {

	public static final String SE_USA_UN_SOLO_THREAD_POR_DEFAULT = "Al crear el taskprocessor solo tiene un thread si no se especifica distinto en la config";
	public static final String AL_CREAR_LA_TAREA_SE_DEFINE_LISTENER_Y_HANDLER = "Aunque se modifique el handler de errores o el listener de tareas para un procesador, las tareas creadas previamente usaran el previo";

}
