/**
 * 20/08/2012 01:19:33 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que compara el valor de un atributo del mensaje con el valor
 * esperado
 * 
 * @author D. García
 */
public class ValorEsperadoIgual implements Condicion {

	private ValueAccessor valueAccessor;
	public static final String valueAccessor_FIELD = "valueAccessor";

	private Object valorEsperado;
	public static final String valorEsperado_FIELD = "valorEsperado";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		if (!valueAccessor.hasValueIn(contenidoDelMensaje)) {
			// Consideramos que si no tiene la propiedad no es igual al esperado
			return false;
		}
		final Object valorEnElMensaje = valueAccessor.getValueFrom(contenidoDelMensaje);
		if (valorEsperado == null) {
			final boolean ambosSonNull = valorEnElMensaje == null;
			return ambosSonNull;
		}
		final boolean sonIguales = valorEsperado.equals(valorEnElMensaje);
		return sonIguales;
	}

	public static ValorEsperadoIgual a(final Object valorEsperado, final String propertyPath) {
		final ValorEsperadoIgual condicion = new ValorEsperadoIgual();
		condicion.valorEsperado = valorEsperado;
		ValueAccessor createdValueAccessor;
		if (propertyPath.contains(PropertyChainAccessor.PROPERTY_DELIMITER)) {
			createdValueAccessor = PropertyChainAccessor.create(propertyPath);
		} else {
			createdValueAccessor = PropertyAccessor.create(propertyPath);
		}
		condicion.valueAccessor = createdValueAccessor;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(valorEsperado_FIELD, valorEsperado).con(valueAccessor_FIELD, valueAccessor)
				.toString();
	}

}
