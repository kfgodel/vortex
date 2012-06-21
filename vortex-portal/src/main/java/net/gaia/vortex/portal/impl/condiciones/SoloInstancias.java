/**
 * 17/06/2012 18:09:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.condiciones;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion que sólo admite instancias de una clase.<br>
 * Debido a que no todos los mensajes tienen el tipo de la clase asociada, y como aunque lo tengan
 * es posible que no esté en el classpath, o que este classpath sea distinto del original, se
 * recomienda NO usar esta condición
 * 
 * @author D. García
 */
public class SoloInstancias implements Condicion {
	private static final Logger LOG = LoggerFactory.getLogger(SoloInstancias.class);

	private Class<?> tipoEsperado;
	public static final String tipoEsperado_FIELD = "tipoEsperado";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		if (mensaje.tieneValorComoPrimitiva()) {
			final Object valorPrimitivo = mensaje.getValorComoPrimitiva();
			final boolean esInstanciaDelTipoEsperado = tipoEsperado.isInstance(valorPrimitivo);
			return esInstanciaDelTipoEsperado;
		}
		// Como optimización verificamos si justo es del tipo esperado
		final String nombreDelTipoOriginal = mensaje.getNombreDelTipoOriginal();
		if (tipoEsperado.getName().equals(nombreDelTipoOriginal)) {
			// Es del mismo tipo que esperabamos
			return true;
		}
		if (nombreDelTipoOriginal == null) {
			// No sabemos de que tipo era originalmente
			if (Object.class.equals(tipoEsperado)) {
				// Solo lo aceptamos si el tipo esperado es object (todo es casteable a Object)
				return true;
			}
			return false;
		}
		// Si indica un tipo, vemos si en una de esas esta en el classpath
		Class<?> tipoDelMensajeOriginal;
		try {
			tipoDelMensajeOriginal = Class.forName(nombreDelTipoOriginal);
		} catch (final ClassNotFoundException e) {
			LOG.debug("La clase[{}] no existe en el classpath. Asumiendo que no cumple con esta condicion[{}]",
					nombreDelTipoOriginal, this);
			return false;
		}
		final boolean esInstanciaDelTipoEsperado = tipoEsperado.isAssignableFrom(tipoDelMensajeOriginal);
		return esInstanciaDelTipoEsperado;
	}

	/**
	 * Crea una condición que verifica si el mensaje corresponde a una clase conocida. Usar esta
	 * condición sólo para mensajes dentro de la misma VM
	 * 
	 * @param tipoEsperado
	 *            El tipo esperado del mensaje
	 * @return La condición creada
	 */
	public static SoloInstancias de(final Class<?> tipoEsperado) {
		final SoloInstancias condicion = new SoloInstancias();
		condicion.tipoEsperado = tipoEsperado;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(tipoEsperado_FIELD, tipoEsperado).toString();
	}
}
