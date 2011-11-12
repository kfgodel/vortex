/**
 * 11/03/2011 15:59:49 Copyright (C) 2006 Darío L. García
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
package com.tenpines.commons.exceptions;

/**
 * Esta clase representa una condición que el código actual no fue pensado para tratar, o que asumía
 * que no era posible (y por lo tanto no existía un caso con el cuál pensar cómo tratarlo).<br>
 * Esta excepción es más bien de tipo comunicativo para que cuando ocurra indique que al momento de
 * diseñar el código no se conocía la solución a esta posiblidad. A diferencia de un
 * RuntimeException plano, esta clase permite indicar un estado de conocimiento al momento de
 * desarrollo.
 * 
 * @author D. García
 */
public class UnhandledConditionException extends RuntimeException {
	private static final long serialVersionUID = -7448872503311371753L;

	public UnhandledConditionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public UnhandledConditionException(final String message) {
		super(message);
	}

	public UnhandledConditionException(final Throwable cause) {
		super(cause);
	}

}
