/**
 * 22/08/2011 15:43:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.prog;

/**
 * Esta clase reune las decisiones del proyecto que crean dependencias entre partes del código, de
 * manera de poder vincularlas estáticamente
 * 
 * @author D. García
 */
public class Decision {

	public static final String EL_NOMBRE_DE_LA_CLASE_SE_USA_EN_XML_DE_TESTING = "Al hacer un refactor de nombre se debe actualizar el archivo: test-beans-context.xml";
	public static final String EL_WRAPPER_NO_ES_VALIDADO_AL_INTERPRETARLO = "Debería revisar que todos los campos sean válidos";
	public static final String SI_FALLA_LA_RESPUESTA_NO_HACEMOS_NADA = "Quizás habría que devolver algo como código de error estandar?";
	public static final String SE_REQUIERE_SESION_SI_SE_USA_ID_O_SI_SE_USAN_METAMENSAJES = "Si el cliente usa un ID que no existe, se crea una nueva sesión. Si no tiene id, pero usa metamensajes como publicar tags se crea también";
}
