/**
 * 23/03/2013 18:54:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.atomos;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.android.messages.conversor.CannotDesmessagificarException;
import net.gaia.vortex.android.messages.conversor.ConversorVortexMessage;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.NexoSupport;
import net.gaia.vortex.core.impl.tasks.forward.DelegarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler;

/**
 * Esta clase representa un componente vortex que se encarga de
 * 
 * @author D. García
 */
public class Desmessageficador extends NexoSupport implements RemoteSessionReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Desmessageficador.class);

	private ConversorVortexMessage converter;

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler#onMessageReceived(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      android.os.Message)
	 */
	@Override
	public void onMessageReceived(RemoteSession sesion, Message message) {
		MensajeVortex mensajeRecibido;
		try {
			mensajeRecibido = converter.convertFromMessage(message);
		} catch (CannotDesmessagificarException e) {
			LOG.error("No pudimos convertir el mensaje desde android. Ignorando", e);
			return;
		}
		// El mensaje del socket lo introducimos en la red desde nosotros como punto inicial
		this.recibir(mensajeRecibido);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final DelegarMensaje delegacion = DelegarMensaje.create(mensaje, getDestino());
		return delegacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(destino_FIELD, getDestino())
				.toString();
	}

	public static Desmessageficador create(final TaskProcessor processor, final Receptor destino,
			ConversorVortexMessage converter) {
		final Desmessageficador desocketizador = new Desmessageficador();
		desocketizador.initializeWith(processor, destino);
		desocketizador.converter = converter;
		return desocketizador;
	}

}
