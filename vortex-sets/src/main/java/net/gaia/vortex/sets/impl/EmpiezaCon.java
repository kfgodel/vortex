/**
 * 23/08/2012 08:54:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sets.impl;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el filtro de mensajes que indica si una propiedad del mensaje empieza con
 * la cadena indicada
 * 
 * @author D. García
 */
public class EmpiezaCon implements Condicion {

	private ValueAccessor valueAccessor;
	public static final String valueAccessor_FIELD = "valueAccessor";

	private String prefijoEsperado;
	public static final String prefijoEsperado_FIELD = "prefijoEsperado";

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
		if (valorEnElMensaje == null) {
			return false;
		}
		if (valorEnElMensaje instanceof String) {
			// No podemos saber si tiene el prefijo
			return false;
		}
		final String cadenaEnMensaje = (String) valorEnElMensaje;
		final boolean empiezaConElPrefijo = cadenaEnMensaje.startsWith(prefijoEsperado);
		return empiezaConElPrefijo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(prefijoEsperado_FIELD, prefijoEsperado).con(valueAccessor_FIELD, valueAccessor)
				.toString();
	}

	public static EmpiezaCon elPrefijo(final String prefjioEsperado, final String propertyPath) {
		final EmpiezaCon condicion = new EmpiezaCon();
		condicion.prefijoEsperado = prefjioEsperado;
		condicion.valueAccessor = PropertyChainAccessor.createAccessor(propertyPath);
		return condicion;
	}
}
