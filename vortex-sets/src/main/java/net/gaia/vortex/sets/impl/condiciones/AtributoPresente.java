/**
 * 20/01/2013 19:09:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.condiciones;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que evalua si el mapa de contenidos tiene un atributo, o
 * secuencia de atributos especificados
 * 
 * @author D. García
 */
@Paralelizable
public class AtributoPresente implements Condicion {

	private ValueAccessor valueAccessor;
	public static final String valueAccessor_FIELD = "valueAccessor";

	public static AtributoPresente conNombre(final String propertyPath) {
		final AtributoPresente condicion = new AtributoPresente();
		condicion.valueAccessor = PropertyChainAccessor.createAccessor(propertyPath);
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		final boolean tieneValor = valueAccessor.hasValueIn(contenidoDelMensaje);
		return ResultadoDeCondicion.paraBooleano(tieneValor);
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	public ValueAccessor getValueAccessor() {
		return valueAccessor;
	}

	public void setValueAccessor(final ValueAccessor valueAccessor) {
		this.valueAccessor = valueAccessor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(valueAccessor_FIELD, valueAccessor).toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(final Object obj) {
		if (!(obj instanceof AtributoPresente)) {
			return false;
		}
		final AtributoPresente that = (AtributoPresente) obj;
		final boolean mismoAtributo = this.valueAccessor.equals(that.valueAccessor);
		return mismoAtributo;
	}

	/**
	 * Tomado de {@link TreeMap.Entry#equals}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		return valueAccessor.hashCode();
	}
}
