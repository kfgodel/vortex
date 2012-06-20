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
 * Esta clase representa la condicion que solo admite instancias de una clase
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
		final String nombreDelTipoOriginal = mensaje.getNombreDelTipoOriginal();
		if (nombreDelTipoOriginal == null) {
			// No sabemos de que tipo era originalmente
			if (Object.class.equals(tipoEsperado)) {
				// Solo lo aceptamos si el tipo esperado es object (cualquier objeto serviría)
				return true;
			}
			return false;
		}
		Class<?> tipoDelMensajeOriginal;
		try {
			tipoDelMensajeOriginal = Class.forName(nombreDelTipoOriginal);
		} catch (final ClassNotFoundException e) {
			LOG.debug("La clase[{}] no existe en el classpath. Asumiendop que no cumple con esta condicion[{}]",
					nombreDelTipoOriginal, this);
			return false;
		}
		final boolean esInstanciaDelTipoEsperado = tipoEsperado.isAssignableFrom(tipoDelMensajeOriginal);
		return esInstanciaDelTipoEsperado;
	}

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
