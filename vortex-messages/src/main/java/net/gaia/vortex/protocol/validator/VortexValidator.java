/**
 * 11/02/2012 15:13:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.validator;

import java.lang.reflect.Field;
import java.util.List;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AnnotationsConfigurer;
import net.sf.oval.context.FieldContext;
import net.sf.oval.context.OValContext;
import net.sf.oval.logging.LoggerFactorySLF4JImpl;

/**
 * Esta clase representa un validador de mensajes vortex que permite obtener un código de error a
 * partir del mensaje invalido
 * 
 * @author D. García
 */
public class VortexValidator {

	private static Validator internalValidator;

	public static Validator getInternalValidator() {
		if (internalValidator == null) {
			// Definimos que el logger de Oval sea SLF4j
			Validator.setLoggerFactory(new LoggerFactorySLF4JImpl());
			internalValidator = new Validator(new AnnotationsConfigurer());
		}
		return internalValidator;
	}

	/**
	 * Devuelve el primer código de error correspondiente a los errores encontrados en el mensaje
	 * pasado.
	 * 
	 * @param mensaje
	 *            El mensaje a analizar
	 * @return null si no encuentra ningún error
	 */
	public static String getFirstErrorCodeFrom(final Object mensaje) {
		final List<ConstraintViolation> violations = getInternalValidator().validate(mensaje);
		if (violations.isEmpty()) {
			return null;
		}
		final ConstraintViolation firstViolation = violations.get(0);
		final StringBuilder builder = new StringBuilder();
		translateViolation(firstViolation, builder);
		return builder.toString();
	}

	/**
	 * Devuelve la versión String como código de error de la violación pasada
	 * 
	 * @param violation
	 *            La violación de protocolo
	 * @param builder
	 * @return El código de error para la violación
	 */
	private static void translateViolation(final ConstraintViolation violation, final StringBuilder builder) {
		final OValContext declaringContext = violation.getCheckDeclaringContext();
		if (declaringContext instanceof FieldContext) {
			final FieldContext fieldContext = (FieldContext) declaringContext;
			final Field invalidField = fieldContext.getField();
			builder.append(invalidField.getName());
		}
		builder.append(".");
		final String violationErrorCode = violation.getErrorCode();
		if (violationErrorCode.equals("net.sf.oval.constraint.NotNull")) {
			builder.append("isNull");
		} else if (violationErrorCode.equals("net.sf.oval.constraint.MinSize")) {
			builder.append("isEmpty");
		} else if (violationErrorCode.equals("net.sf.oval.constraint.NotNegative")) {
			builder.append("isNegative");
		} else if (violationErrorCode.equals("net.sf.oval.constraint.NotEmpty")) {
			builder.append("isEmpty");
		} else if (violationErrorCode.equals("net.sf.oval.constraint.AssertValid")) {
			// Tiene sub errores
			final ConstraintViolation[] causes = violation.getCauses();
			if (causes.length > 0) {
				// Solo nos quedamos con la primera
				final ConstraintViolation firstSubViolation = causes[0];
				translateViolation(firstSubViolation, builder);
			} else {
				builder.append("internalError!");
			}
		} else {
			builder.append(violationErrorCode);
		}
	}

}
