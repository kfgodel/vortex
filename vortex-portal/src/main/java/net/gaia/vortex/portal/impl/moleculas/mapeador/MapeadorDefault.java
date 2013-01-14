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

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.mensaje.ContenidoVortexLazy;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el mapeador de mensajes vortex y objetos utilizando JSON como paso
 * intermedio. Para obtener un mensaje vortex se convierte a json y se desconvierte a map.<br>
 * Para obtener el objeto original desde uno vortex, se convierte a Json un mapa, y se desconvierte
 * al tipo esperado
 * 
 * @author D. García
 */
public class MapeadorDefault implements MapeadorVortex {

	private MapeadorDeObjetos mapeadorDeObjetos;

	/**
	 * @see net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorVortex#convertirAVortex(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MensajeVortex convertirAVortex(final Object objetoOriginal) throws ErrorDeMapeoVortexException {
		if (objetoOriginal == null) {
			// Al menos por ahora no soportamos null como fuente de la conversion
			throw new ErrorDeMapeoVortexException(
					"El objeto original no puede ser null para ser convertido en mensaje vortex");
		}

		ContenidoVortex contenidoDelMensaje;
		// Las primitivas viajan como tipo especial de mensaje
		if (ContenidoPrimitiva.esPrimitivaVortex(objetoOriginal)) {
			contenidoDelMensaje = ContenidoPrimitiva.create(objetoOriginal);
		} else {
			contenidoDelMensaje = ContenidoVortexLazy.create(objetoOriginal, mapeadorDeObjetos);
		}
		final MensajeConContenido mensajeVortex = MensajeConContenido.create(contenidoDelMensaje);
		return mensajeVortex;
	}

	/**
	 * @see net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorVortex#convertirDesdeVortex(net.gaia.vortex.core.api.mensaje.MensajeVortex,
	 *      java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T convertirDesdeVortex(final MensajeVortex mensajeVortex, final Class<T> tipoEsperado)
			throws ErrorDeMapeoVortexException {
		if (mensajeVortex == null) {
			throw new ErrorDeMapeoVortexException("El mensaje vortex no puede ser null para ser convertido en "
					+ tipoEsperado);
		}

		final ContenidoVortex contenidoVortex = mensajeVortex.getContenido();
		// Vemos si podemos optimizar el proceso usando el objeto original
		if (contenidoVortex instanceof ContenidoVortexLazy) {
			final ContenidoVortexLazy contenidoLazy = (ContenidoVortexLazy) contenidoVortex;
			final Object objetoOriginal = contenidoLazy.getObjetoOriginal();
			if (tipoEsperado.isInstance(objetoOriginal)) {
				// Estamos en memoria y justo nos piden lo que tenemos (no hace falta conversion)
				return (T) objetoOriginal;
			}
			// Nos piden otro tipo del que tenemos, pasamos por la conversión
		}

		// Verificamos si no es un mensaje con primitiva
		if (contenidoVortex.tieneValorComoPrimitiva()) {
			final Object valorPrimitivo = contenidoVortex.getValorComoPrimitiva();
			if (!tipoEsperado.isInstance(valorPrimitivo)) {
				throw new UnhandledConditionException("Me piden un tipo[" + tipoEsperado
						+ "] distinto de la primitiva[" + valorPrimitivo + "] que tengo. Conversión no implementada");
			}
			return (T) valorPrimitivo;
		}

		final T objeto = mapeadorDeObjetos.convertirDesdeEstado(contenidoVortex, tipoEsperado);
		return objeto;
	}

	public static MapeadorDefault create() {
		final MapeadorDefault mapeador = new MapeadorDefault();
		mapeador.mapeadorDeObjetos = MapeadorJackson.create();
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
