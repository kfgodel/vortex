/**
 * 17/06/2012 19:02:19 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.ConcurrentMap;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Esta clase implementa el mapeador de mensajes vortex y objetos utilizando JSON como paso
 * intermedio. Para obtener un mensaje vortex se convierte a json y se desconvierte a map.<br>
 * Para obtener el objeto original desde uno vortex, se convierte a Json un mapa, y se desconvierte
 * al tipo esperado
 * 
 * @author D. García
 */
public class MapeadorJson implements MapeadorVortex {

	private ObjectMapper jacksonMapper;

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.MapeadorVortex#convertirAVortex(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MensajeVortex convertirAVortex(final Object mensajeOriginal) throws ErrorDeMapeoVortexException {
		if (mensajeOriginal == null) {
			// Al menos por ahora no soportamos null como fuente de la conversion
			throw new ErrorDeMapeoVortexException(
					"El objeto original no puede ser null para ser convertido en mensaje vortex");
		}
		// Las primitivas viajan como tipo especial de mensaje
		if (MensajeMapa.esPrimitivaVortex(mensajeOriginal)) {
			// No podemos jsonizarlo, pero si podemos crear el mensaje de una
			final MensajeMapa mensajeConPrimitiva = MensajeMapa.create();
			mensajeConPrimitiva.setValorComoPrimitiva(mensajeOriginal);
			return mensajeConPrimitiva;
		}
		Map<String, Object> mapaDeEstado;
		try {
			mapaDeEstado = jacksonMapper.convertValue(mensajeOriginal, Map.class);
		} catch (final Exception e) {
			throw new ErrorDeMapeoVortexException("Se produjo un error al convertir el objeto[" + mensajeOriginal
					+ "] a mapa", e);
		}

		final MensajeVortex mensajeVortex = MensajeMapa.create(mapaDeEstado);
		final Class<? extends Object> tipoDelMensaje = mensajeOriginal.getClass();
		final String nombreDelTipoOriginal = tipoDelMensaje.getName();
		mensajeVortex.setNombreDelTipoOriginal(nombreDelTipoOriginal);
		return mensajeVortex;
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.MapeadorVortex#convertirDesdeVortex(net.gaia.vortex.core.api.mensaje.MensajeVortex,
	 *      java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T convertirDesdeVortex(final MensajeVortex mensajeOriginal, final Class<T> tipoEsperado)
			throws ErrorDeMapeoVortexException {
		if (mensajeOriginal == null) {
			throw new ErrorDeMapeoVortexException("El mensaje vortex no puede ser null para ser convertido en "
					+ tipoEsperado);
		}
		// Verificamos si no es un mensaje con primitiva
		if (mensajeOriginal.tieneValorComoPrimitiva()) {
			final Object valorPrimitivo = mensajeOriginal.getValorComoPrimitiva();
			return (T) valorPrimitivo;
		}

		final ConcurrentMap<String, Object> mapaDeContenido = mensajeOriginal.getContenido();
		final T objeto;
		try {
			objeto = jacksonMapper.convertValue(mapaDeContenido, tipoEsperado);
		} catch (final CannotTextSerializeException e) {
			throw new ErrorDeMapeoVortexException("Se produjo un error al obtener el objeto de tipo[" + tipoEsperado
					+ "] desde el map[" + mensajeOriginal + "]", e);
		}
		return objeto;
	}

	public static MapeadorJson create() {
		final MapeadorJson mapeador = new MapeadorJson();
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
