/**
 * 20/08/2012 19:39:24 Copyright (C) 2011 Darío L. García
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

import java.util.Collection;
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
 * Esta clase representa la condición que evalúa si una propiedad en el mapa de tipo colección o
 * iterable contiene al valor indicado
 * 
 * @author D. García
 */
public class ColeccionContiene implements Condicion {

	private ValueAccessor valueAccessor;
	public static final String valueAccessor_FIELD = "valueAccessor";

	private Object valorEsperado;
	public static final String valorEsperado_FIELD = "valorEsperado";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		if (!valueAccessor.hasValueIn(contenidoDelMensaje)) {
			// Consideramos que si no tiene la propiedad no puede contener el valor
			return ResultadoDeCondicion.FALSE;
		}
		final Object valorEnElMensaje = valueAccessor.getValueFrom(contenidoDelMensaje);
		if (valorEnElMensaje == null) {
			// Si el valor es nulo tampoco contiene el valor esperado
			return ResultadoDeCondicion.FALSE;
		}
		// Probamos con la api de collection primero
		if (valorEnElMensaje instanceof Collection) {
			final Collection<?> coleccion = (Collection<?>) valorEnElMensaje;
			final boolean contieneElValor = coleccion.contains(valorEsperado);
			final ResultadoDeCondicion resultado = ResultadoDeCondicion.paraBooleano(contieneElValor);
			return resultado;
		}
		if (!(valorEnElMensaje instanceof Iterable)) {
			// Al no ser collecion ni iterable, asumimos que no contiene al valor
			return ResultadoDeCondicion.FALSE;
		}
		final Iterable<?> iterable = (Iterable<?>) valorEnElMensaje;
		for (final Object elementoEnMensaje : iterable) {
			final boolean sonIguales = ValorEsperadoEn.compararPorEquals(valorEsperado, elementoEnMensaje);
			if (sonIguales) {
				return ResultadoDeCondicion.TRUE;
			}
		}
		return ResultadoDeCondicion.FALSE;
	}

	/**
	 * Crea una nueva condición que evalua si el valor esperado se encuentra en la colección de la
	 * propiedad de cada mensaje evaluadao
	 * 
	 * @param valorEsperado
	 *            El valor que se espera
	 * @param propertyPath
	 *            La cadena de propiedades que representa una colección o iterable
	 * @return La condición creada
	 */
	public static ColeccionContiene alValor(final Object valorEsperado, final String propertyPath) {
		final ValueAccessor valueAccessor = PropertyChainAccessor.createAccessor(propertyPath);
		return create(valorEsperado, valueAccessor);
	}

	public static ColeccionContiene create(final Object valorEsperado, final ValueAccessor valueAccessor) {
		final ColeccionContiene condicion = new ColeccionContiene();
		condicion.valorEsperado = valorEsperado;
		condicion.valueAccessor = valueAccessor;
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

	public Object getValorEsperado() {
		return valorEsperado;
	}

	public void setValorEsperado(final Object valorEsperado) {
		this.valorEsperado = valorEsperado;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ColeccionContiene)) {
			return false;
		}
		final ColeccionContiene that = (ColeccionContiene) obj;
		if (!this.valueAccessor.equals(that.valueAccessor)) {
			// Son para propiedades distintas
			return false;
		}
		final boolean mismoValor = this.valorEsperado.equals(that.valorEsperado);
		return mismoValor;
	}

	/**
	 * Tomado de {@link TreeMap.Entry#equals}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ReflectionUtils.hashDeDosValores(valueAccessor, valorEsperado);
	}

}
