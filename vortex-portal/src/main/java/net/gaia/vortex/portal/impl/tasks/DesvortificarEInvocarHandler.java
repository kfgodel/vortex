/**
 * 17/06/2012 20:53:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.tasks;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.portal.api.mensaje.HandlerDeMensaje;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada por un componente vortex para convertir el mensaje
 * vortex en un objeto antes de pasárselo al handler
 * 
 * @author D. García
 */
public class DesvortificarEInvocarHandler<T> implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DesvortificarEInvocarHandler.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private ConversorDeMensajesVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	private Class<? extends T> tipoEsperado;
	public static final String tipoEsperado_FIELD = "tipoEsperado";

	private HandlerDeMensaje<? super T> handlerDeMensajes;
	public static final String handlerDeMensajes_FIELD = "handlerDeMensajes";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		T desvortificado;
		try {
			desvortificado = mapeador.convertirDesdeVortex(mensaje, tipoEsperado);
		} catch (final ErrorDeMapeoVortexException e) {
			LOG.error("Se produjo un error al desvortificar un mensaje[" + mensaje + "] con el mapeador[" + mapeador
					+ "]. El mensaje no llegará al handler[" + handlerDeMensajes + "]", e);
			return;
		}
		final InvocarHandler<T> invocacion = InvocarHandler.create(desvortificado, handlerDeMensajes);
		parallelizer.submitAndForget(invocacion);
	}

	public static <T> DesvortificarEInvocarHandler<T> create(final MensajeVortex mensaje,
			final ConversorDeMensajesVortex mapeador, final Class<? extends T> tipoEsperado,
			final HandlerDeMensaje<? super T> handlerDeMensaje) {
		final DesvortificarEInvocarHandler<T> conversor = new DesvortificarEInvocarHandler<T>();
		conversor.mensaje = mensaje;
		conversor.mapeador = mapeador;
		conversor.handlerDeMensajes = handlerDeMensaje;
		conversor.tipoEsperado = tipoEsperado;
		return conversor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(tipoEsperado_FIELD, tipoEsperado).add(mensaje_FIELD, mensaje)
				.add(mapeador_FIELD, mapeador).add(handlerDeMensajes_FIELD, handlerDeMensajes).toString();
	}

}
