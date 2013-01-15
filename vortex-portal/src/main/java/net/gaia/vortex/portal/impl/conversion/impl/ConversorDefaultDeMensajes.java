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
package net.gaia.vortex.portal.impl.conversion.impl;

import java.util.Map;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeContenidoVortex;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
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
public class ConversorDefaultDeMensajes implements ConversorDeMensajesVortex {

	private ConversorDeContenidoVortex mapeadorDeObjetos;

	/**
	 * @see net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex#convertirAVortex(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public MensajeVortex convertirAVortex(final Object objetoOriginal) throws ErrorDeMapeoVortexException {
		if (objetoOriginal == null) {
			// Al menos por ahora no soportamos null como fuente de la conversion
			throw new ErrorDeMapeoVortexException(
					"El objeto original no puede ser null para ser convertido en mensaje vortex");
		}
		final ContenidoVortex contenidoDelMensaje = generarContenidoDesde(objetoOriginal);
		final MensajeConContenido mensajeVortex = MensajeConContenido.create(contenidoDelMensaje);
		return mensajeVortex;
	}

	/**
	 * Genera el contenido que se incluirá como estado del mensaje, representando el objeto pasado
	 * 
	 * @param objetoOriginal
	 *            El objeto a representar como contenido vortex
	 * @return El contenido generado
	 */
	private ContenidoVortex generarContenidoDesde(final Object objetoOriginal) {

		// El mapeador falla con las primitivas, por eso hacemos una excepción
		if (ContenidoPrimitiva.esPrimitivaVortex(objetoOriginal)) {
			return ContenidoPrimitiva.create(objetoOriginal);
		}

		// Si es un objeto común, lo convertimos con el mapeador
		final Map<String, Object> estadoGenerado = mapeadorDeObjetos.convertirAEstado(objetoOriginal);
		final ContenidoMapa contenido = ContenidoMapa.create(estadoGenerado);

		// Registramos el nombre de la clase original para optimizar algunas condiciones
		contenido.setNombreDelTipoOriginalDesde(objetoOriginal);
		return contenido;
	}

	/**
	 * @see net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex#convertirDesdeVortex(net.gaia.vortex.core.api.mensaje.MensajeVortex,
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
		final T objeto = (T) generarObjetoDesde(contenidoVortex, tipoEsperado);
		return objeto;
	}

	/**
	 * Genera el objeto esperado a partir del contenido pasado, intentando reconstruir la instancia
	 * del tipo
	 * 
	 * @param contenido
	 *            El contenido desde el cual se obtiene el estado para regenerar el objeto
	 * @return El objeto generado
	 */
	private Object generarObjetoDesde(final ContenidoVortex contenido, final Class<?> tipoEsperado) {
		// Si representa una primitiva tenemos que hacer una excepción porque el mapeador falla
		if (contenido.tieneValorComoPrimitiva()) {
			final Object valorPrimitivo = contenido.getValorComoPrimitiva();
			if (!tipoEsperado.isInstance(valorPrimitivo)) {
				throw new UnhandledConditionException("Me piden un tipo[" + tipoEsperado
						+ "] distinto de la primitiva[" + valorPrimitivo + "] que tengo. Conversión no implementada");
			}
			return valorPrimitivo;
		}

		// Para el resto de los casos el mapeador se la banca
		final Object objeto = mapeadorDeObjetos.convertirDesdeEstado(contenido, tipoEsperado);
		return objeto;
	}

	public static ConversorDefaultDeMensajes create() {
		final ConversorDefaultDeMensajes mapeador = new ConversorDefaultDeMensajes();
		mapeador.mapeadorDeObjetos = ConversorJacksonDeContenido.create();
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
