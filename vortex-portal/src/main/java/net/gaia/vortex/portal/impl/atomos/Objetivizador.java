/**
 * 17/06/2012 20:43:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.atomos;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;
import net.gaia.vortex.portal.api.mensaje.HandlerDeMensaje;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.tasks.DesvortificarEInvocarHandler;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que convierte los mensajes recibidos en objetos de un
 * tipo esperado.<br>
 * A través de instancias de esta clase el portal puede recrear los objetos del usuario a partir de
 * los mensajes recibidos.<br>
 * Este componente convierte los mensajes vortex en objetos utilizando su propio thread y luego
 * invoca el handler (también en su thread)
 * 
 * @author D. García
 */
@Atomo
public class Objetivizador<T> extends ReceptorConProcesador {

	private ConversorDeMensajesVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	private Class<? extends T> tipoEsperado;
	public static final String tipoEsperado_FIELD = "tipoEsperado";

	private HandlerDeMensaje<? super T> handlerDeMensajes;
	public static final String handlerDeMensajes_FIELD = "handlerDeMensajes";

	public static <T> Objetivizador<T> create(final TaskProcessor processor, final ConversorDeMensajesVortex mapeador,
			final Class<? extends T> tipoEsperado, final HandlerDeMensaje<? super T> handlerDeMensaje) {
		final Objetivizador<T> conversor = new Objetivizador<T>();
		conversor.initializeWith(processor);
		conversor.mapeador = mapeador;
		conversor.handlerDeMensajes = handlerDeMensaje;
		conversor.tipoEsperado = tipoEsperado;
		return conversor;
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final DesvortificarEInvocarHandler<T> desvortificacion = DesvortificarEInvocarHandler.create(mensaje, mapeador,
				tipoEsperado, handlerDeMensajes);
		return desvortificacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.add(tipoEsperado_FIELD, tipoEsperado).add(handlerDeMensajes_FIELD, handlerDeMensajes)
				.add(mapeador_FIELD, mapeador).toString();
	}
}
