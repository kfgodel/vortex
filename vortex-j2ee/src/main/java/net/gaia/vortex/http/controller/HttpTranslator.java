/**
 * 28/01/2012 16:13:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.controller;

import java.util.List;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;
import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.http.protocol.VortexWrapper;
import net.gaia.vortex.prog.Decision;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.tenpines.commons.exceptions.UnhandledConditionException;

/**
 * Esta clase representa un traductor entre operaciones de http y operaciones sin sesión en vortex,
 * de manera que la ida y vuelta de mensajes se transporten correctamente sobre http
 * 
 * @author D. García
 */
@Component
public class HttpTranslator {
	private static final Logger LOG = LoggerFactory.getLogger(HttpTranslator.class);

	@Autowired
	private RemoteSessionController controller;

	@Autowired
	private InterpreteJson interprete;

	/**
	 * Procesa el pedido http devolviendo la respuesta en el mismo pedido
	 * 
	 * @param pedido
	 */
	public void translate(final OperacionHttp pedido) {
		final VortexWrapper wrapper = translateFromHttp(pedido);
		if (wrapper == null) {
			LOG.warn("Se recibió un request sin wrapper. Devolviendo vacío");
			return;
		}
		final VortexWrapper output = controller.process(wrapper);
		translateToHttp(output, pedido);
	}

	/**
	 * Genera un respuesta para ser devuelta en el pedido http recibido
	 * 
	 * @param output
	 *            El contenido a enviar en el pedido
	 * @param pedido
	 *            El pedido en el cual se puede escribir la respuesta
	 */
	@HasDependencyOn(Decision.SI_FALLA_LA_RESPUESTA_NO_HACEMOS_NADA)
	private void translateToHttp(final VortexWrapper output, final OperacionHttp pedido) {
		final List<MensajeVortex> mensajes = output.getMensajes();
		for (final MensajeVortex mensajeVortex : mensajes) {
			translateContentToJson(mensajeVortex);
		}

		final String wrapperComoTexto = interprete.toJson(output);
		pedido.responder(wrapperComoTexto);
	}

	/**
	 * Convierte el contenido del mensaje en json si es un metamensaje para el cliente
	 * 
	 * @param mensajeVortex
	 *            El mensaje a procesar
	 */
	private void translateContentToJson(final MensajeVortex mensajeVortex) {
		if (!mensajeVortex.esMetaMensaje()) {
			// No es necesario convertirlo ya que fue string en todo momento
			return;
		}
		final ContenidoVortex contenido = mensajeVortex.getContenido();
		final Object valorComoObjeto = contenido.getValor();
		final String valorComoJson = interprete.toJson(valorComoObjeto);
		contenido.setValor(valorComoJson);
		final String tipoDeContenido = getTipoDeContenidoFromTipoDeValor(valorComoObjeto);
		contenido.setTipoContenido(tipoDeContenido);
	}

	/**
	 * Devuelve la cadena que corresponde al tipo de contenido del objeto pasado
	 * 
	 * @param valorComoObjeto
	 *            El objeto a evaluar
	 * @return
	 */
	private String getTipoDeContenidoFromTipoDeValor(final Object valorComoObjeto) {
		if (valorComoObjeto == null) {
			throw new UnhandledConditionException("Se indico como valor de un metamensaje el valor null");
		}
		final String tipoDeContenido = valorComoObjeto.getClass().getName();
		return tipoDeContenido;
	}

	/**
	 * Crea una versión objeto con la interpretación del parámetro del request que corresponde al
	 * wrapper http
	 * 
	 * @param pedido
	 *            El request recibido
	 * @return El wrapper interpretado
	 */
	@HasDependencyOn(Decision.EL_WRAPPER_NO_ES_VALIDADO_AL_INTERPRETARLO)
	private VortexWrapper translateFromHttp(final OperacionHttp pedido) {
		final String textoDelWrapper = pedido.getWrapperJson();
		if (Strings.isNullOrEmpty(textoDelWrapper)) {
			return null;
		}
		final VortexWrapper wrapper;
		try {
			wrapper = interprete.fromJson(textoDelWrapper, VortexWrapper.class);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("Se produjo un error al interpretar el wrapper recibido: ["
					+ textoDelWrapper + "]", e);
		}

		final List<MensajeVortex> mensajes = wrapper.getMensajes();
		for (final MensajeVortex mensajeVortex : mensajes) {
			translateContentFromJson(mensajeVortex);
		}

		return wrapper;
	}

	/**
	 * Recupera el contenido del mensaje que es convertido a JSON para poder mandarlo por HTTP
	 * 
	 * @param mensajeVortex
	 *            El mensaje cuyo contenido se convertirá en objeto nuevamente
	 */
	private void translateContentFromJson(final MensajeVortex mensajeVortex) {
		if (!mensajeVortex.esMetaMensaje()) {
			// No necesita conversion, debería ser un String, lo confirmamos por las dudas
			final Object valor = mensajeVortex.getContenido().getValor();
			if (valor == null) {
				// Es un valor valido
				return;
			}
			if (!(valor instanceof String)) {
				throw new UnhandledConditionException("Se recibio un mensaje cuyo contenido no es un String: " + valor);
			}
			return;
		}
		final ContenidoVortex contenido = mensajeVortex.getContenido();
		final String tipoContenidoDeclarado = contenido.getTipoContenido();
		final Class<?> tipoEsperado = getTipoEsperadoFromTipoDeContenido(tipoContenidoDeclarado);
		String valorJson;
		try {
			valorJson = (String) contenido.getValor();
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("Se recibio un metamensaje cuyo contenido no es un String: "
					+ contenido.getValor(), e);
		}
		try {
			final Object valorObjeto = interprete.fromJson(valorJson, tipoEsperado);
			contenido.setValor(valorObjeto);
		} catch (final JsonConversionException e) {
			throw new UnhandledConditionException("No fue posible recrear el objeto[" + tipoEsperado
					+ "] de su version String: " + valorJson, e);
		}
	}

	/**
	 * devuelve la clase de objeto que se corresponde con el contenido en base al tipo declarado
	 * 
	 * @param tipoContenidoDeclarado
	 *            El tipo declarado del contenido
	 * @return La clase para recrear el objeto
	 */
	private Class<?> getTipoEsperadoFromTipoDeContenido(final String tipoContenidoDeclarado) {
		try {
			final Class<?> claseDemetamensaje = Class.forName(tipoContenidoDeclarado);
			return claseDemetamensaje;
		} catch (final Exception e) {
			throw new UnhandledConditionException("Se produjo un error al intentar obtener el metamensaje de tipo: "
					+ tipoContenidoDeclarado, e);
		}
	}
}
