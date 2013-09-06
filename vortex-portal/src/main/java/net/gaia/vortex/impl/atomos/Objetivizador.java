/**
 * 04/09/2013 23:07:13 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;
import net.gaia.vortex.portal.api.mensaje.HandlerDeObjetos;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el componente vortex que convierte los mensajes recibidos en objetos para
 * ser entregados a un {@link HandlerDeObjetos}
 * 
 * @author D. García
 */
public class Objetivizador extends ReceptorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(Objetivizador.class);

	private ConversorDeMensajesVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	private Class<?> tipoEsperado;
	public static final String tipoEsperado_FIELD = "tipoEsperado";

	private HandlerDeObjetos<Object> handlerDeObjetos;
	public static final String handlerDeMensajes_FIELD = "handlerDeMensajes";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		Object desvortificado;
		try {
			desvortificado = mapeador.convertirDesdeVortex(mensaje, tipoEsperado);
		}
		catch (final ErrorDeMapeoVortexException e) {
			LOG.error("Se produjo un error al desvortificar un mensaje[" + mensaje + "] con el mapeador[" + mapeador
					+ "]. El mensaje no llegará al handler[" + handlerDeObjetos + "]", e);
			return;
		}
		try {
			handlerDeObjetos.onObjetoRecibido(desvortificado);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en el handler[" + handlerDeObjetos + "] al recibir el objeto["
					+ desvortificado + "]", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Objetivizador create(final ConversorDeMensajesVortex mapeador, final Class<?> tipoEsperado,
			final HandlerDeObjetos<?> handlerDeObjetos) {
		final Objetivizador objetivizador = new Objetivizador();
		objetivizador.handlerDeObjetos = (HandlerDeObjetos<Object>) handlerDeObjetos;
		objetivizador.mapeador = mapeador;
		objetivizador.tipoEsperado = tipoEsperado;
		return objetivizador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.add(tipoEsperado_FIELD, tipoEsperado).add(handlerDeMensajes_FIELD, handlerDeObjetos).toString();
	}
}
