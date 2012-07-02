/**
 * 02/07/2012 00:39:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.moleculas.mapeador;

import java.util.Map;

import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Esta clase implementa el {@link MapeadorDeObjetos} utilizando la librería jackson
 * 
 * @author D. García
 */
public class MapeadorJackson implements MapeadorDeObjetos {

	private ObjectMapper jacksonMapper;

	/**
	 * @see net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorDeObjetos#convertirAEstado(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> convertirAEstado(final Object objetoOriginal) throws ErrorDeMapeoVortexException {
		Map<String, Object> mapaDeEstado;
		try {
			mapaDeEstado = jacksonMapper.convertValue(objetoOriginal, Map.class);
		} catch (final Exception e) {
			throw new ErrorDeMapeoVortexException("Se produjo un error al convertir el objeto[" + objetoOriginal
					+ "] a mapa", e);
		}
		return mapaDeEstado;
	}

	/**
	 * @see net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorDeObjetos#convertirDesdeEstado(java.util.Map,
	 *      java.lang.Class)
	 */
	@Override
	public <T> T convertirDesdeEstado(final Map<String, Object> estado, final Class<T> tipoEsperado)
			throws ErrorDeMapeoVortexException {
		final T objeto;
		try {
			objeto = jacksonMapper.convertValue(estado, tipoEsperado);
		} catch (final CannotTextSerializeException e) {
			throw new ErrorDeMapeoVortexException("Se produjo un error al obtener el objeto de tipo[" + tipoEsperado
					+ "] desde el map[" + estado + "]", e);
		}
		return objeto;
	}

	public static MapeadorJackson create() {
		final MapeadorJackson mapeador = new MapeadorJackson();
		mapeador.jacksonMapper = new ObjectMapper();
		mapeador.jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapeador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

}
