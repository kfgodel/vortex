/**
 * 23/08/2012 08:54:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sets.impl;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
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
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		if (!valueAccessor.hasValueIn(contenidoDelMensaje)) {
			// Consideramos que si no tiene la propiedad, no empieza con un valor
			return ResultadoDeCondicion.FALSE;
		}
		final Object valorEnElMensaje = valueAccessor.getValueFrom(contenidoDelMensaje);
		if (valorEnElMensaje == null) {
			// Consideramos que null tampoco empieza con el prefijo
			return ResultadoDeCondicion.FALSE;
		}
		if (!(valorEnElMensaje instanceof String)) {
			// Si no es un string asumimos que no empieza con el prefijo
			return ResultadoDeCondicion.FALSE;
		}
		final String cadenaEnMensaje = (String) valorEnElMensaje;
		final boolean empiezaConElPrefijo = cadenaEnMensaje.startsWith(prefijoEsperado);
		ResultadoDeCondicion resultado = ResultadoDeCondicion.paraBooleano(empiezaConElPrefijo);
		return resultado;
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
