/**
 * 23/08/2012 08:54:26 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sets.impl.condiciones;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.lang.reflection.ReflectionUtils;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el filtro de mensajes que indica si una propiedad del mensaje empieza con
 * la cadena indicada
 * 
 * @author D. García
 */
public class AtributoEmpieza implements Condicion {

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
		final ResultadoDeCondicion resultado = ResultadoDeCondicion.paraBooleano(empiezaConElPrefijo);
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

	public static AtributoEmpieza conPrefijo(final String prefjioEsperado, final String propertyPath) {
		final AtributoEmpieza condicion = new AtributoEmpieza();
		condicion.prefijoEsperado = prefjioEsperado;
		condicion.valueAccessor = PropertyChainAccessor.createAccessor(propertyPath);
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	@Override
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	public ValueAccessor getValueAccessor() {
		return valueAccessor;
	}

	public void setValueAccessor(final ValueAccessor valueAccessor) {
		this.valueAccessor = valueAccessor;
	}

	public String getPrefijoEsperado() {
		return prefijoEsperado;
	}

	public void setPrefijoEsperado(final String prefijoEsperado) {
		this.prefijoEsperado = prefijoEsperado;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof AtributoEmpieza)) {
			return false;
		}
		final AtributoEmpieza that = (AtributoEmpieza) obj;
		if (!this.valueAccessor.equals(that.valueAccessor)) {
			// Son para propiedades distintas
			return false;
		}
		final boolean mismoPrefijo = this.prefijoEsperado.equals(that.prefijoEsperado);
		return mismoPrefijo;
	}

	/**
	 * Tomado de {@link TreeMap.Entry#equals}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ReflectionUtils.hashDeDosValores(valueAccessor, prefijoEsperado);
	}

}
