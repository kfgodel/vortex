/**
 * 15/11/2011 23:52:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.api.exceptions;

/**
 * Esta clase representa una excepción en el código de procesamiento de tareas.<br>
 * Esta es la superclase de todas las excepciones de este proyecto
 * 
 * @author D. García
 */
public class TaskProcessingException extends RuntimeException {
	private static final long serialVersionUID = -521901232015021729L;

	public TaskProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TaskProcessingException(final String message) {
		super(message);
	}

	public TaskProcessingException(final Throwable cause) {
		super(cause);
	}
}
