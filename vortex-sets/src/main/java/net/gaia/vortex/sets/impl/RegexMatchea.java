/**
 * 24/08/2012 16:50:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que evalúa la expresión regular en formato similiar a Perl
 * (con algunas restricciones) sobre la propiedad indicada
 * 
 * @author D. García
 */
public class RegexMatchea implements Condicion {

	private ValueAccessor valueAccessor;
	public static final String valueAccessor_FIELD = "valueAccessor";

	private Pattern expresion;
	public static final String expresion_FIELD = "expresion";

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(expresion_FIELD, expresion).con(valueAccessor_FIELD, valueAccessor).toString();
	}

	/**
	 * Crea una nueva condición que evalúa la expresión indicada contra la propiedad de cada mensaje
	 * para descartarlo o no.<br>
	 * Ver {@link Pattern}
	 * 
	 * @param expresionRegular
	 *            La expresión a evaluar en cada mensaje
	 * @param propertyPath
	 *            La cadena de propiedades para navegar en el mensaje
	 * @return La condición creada
	 */
	public static RegexMatchea laExpresion(final String expresionRegular, final String propertyPath) {
		Pattern pattern;
		try {
			pattern = Pattern.compile(expresionRegular);
		} catch (final PatternSyntaxException e) {
			throw new IllegalArgumentException("La expresión regular de la condicion no es aceptable por el Pattern", e);
		}
		final ValueAccessor valueAccessor = PropertyChainAccessor.createAccessor(propertyPath);
		return create(pattern, valueAccessor);
	}

	public static RegexMatchea create(final Pattern pattern, final ValueAccessor valueAccessor) {
		final RegexMatchea condicion = new RegexMatchea();
		condicion.expresion = pattern;
		condicion.valueAccessor = valueAccessor;
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		if (!valueAccessor.hasValueIn(contenidoDelMensaje)) {
			// Consideramos que si no tiene la propiedad no matchea
			return false;
		}
		final Object valorEnElMensaje = valueAccessor.getValueFrom(contenidoDelMensaje);
		if (valorEnElMensaje == null) {
			// Consideramos que no matchea si es null
			return false;
		}
		if (!(valorEnElMensaje instanceof CharSequence)) {
			// Solo podemos evaluar cadenas de caracteres
			return false;
		}
		final CharSequence cadenaDelMensaje = (CharSequence) valorEnElMensaje;
		final Matcher matcher = expresion.matcher(cadenaDelMensaje);
		final boolean matchea = matcher.matches();
		return matchea;
	}

}