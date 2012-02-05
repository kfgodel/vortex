/**
 * 02/02/2012 22:33:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.sessions.SesionConId;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la operación realizada por el nodo http para validar los mensajes que va a
 * enviar antes de enviarlos realmente
 * 
 * @author D. García
 */
public class ValidarMensajesPrevioEnvioWorkUnit implements WorkUnit {

	private MensajeVortex mensajeAEnviar;
	private ContextoDeOperacionHttp contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Verificamos errores en el mensaje
		final ErroresDelMensaje errores = validarMensajeAEnviarPorHttp(mensajeAEnviar);
		if (errores != null) {
			// Si hay errores no podemos continuar con este mensaje
			final SesionConId sesionInvolucrada = contexto.getSesionInvolucrada();
			sesionInvolucrada.onErrorDeMensaje(mensajeAEnviar, errores);

			// Finalizamos este mensaje
			final TerminarEnvioDeMensajeWorkUnit terminar = TerminarEnvioDeMensajeWorkUnit.create(contexto);
			contexto.getProcessor().process(terminar);
			return;
		}

		// Continuamos con el proceso convirtiendo a String metamensajes
		final ConvertirMetamensajesEnStringsWorkUnit conversion = ConvertirMetamensajesEnStringsWorkUnit.create(
				contexto, mensajeAEnviar);
		contexto.getProcessor().process(conversion);
	}

	/**
	 * Valida que el mensaje sea correcto para ser enviado por http. Verifica que esté completo y
	 * que si es un mensaje su valor sea un string
	 * 
	 * @param mensajeAEnviar
	 * @return
	 */
	private ErroresDelMensaje validarMensajeAEnviarPorHttp(final MensajeVortex mensajeAEnviar) {
		final ContenidoVortex contenidoVortex = mensajeAEnviar.getContenido();
		if (contenidoVortex == null) {
			return ErroresDelMensaje.create("El mensaje no tiene contenido vortex: " + mensajeAEnviar.toPrettyPrint());
		}
		final Object valorAValidar = contenidoVortex.getValor();
		final boolean valorEsNull = valorAValidar == null;
		final boolean valorEsString = valorAValidar instanceof String;
		final boolean esMetamensaje = mensajeAEnviar.esMetaMensaje();

		if (esMetamensaje && valorEsNull) {
			// Hacemos esta validación de onda, quizás en el futuro pueda ser null
			return ErroresDelMensaje
					.create("El mensaje no puede indicar metamensaje como contenido y enviar null como valor: "
							+ mensajeAEnviar.toPrettyPrint());
		}
		if (esMetamensaje && valorEsString) {
			// Quizás esto no sea un error en el futuro pero a modo de reaseguro verificamos que
			// como metamensajes no vengan strings para no convertirlos 2 veces
			return ErroresDelMensaje
					.create("El metamensaje no puede ser String. Por ser convertido a String automáticamente: "
							+ mensajeAEnviar.toPrettyPrint());
		}
		if (!esMetamensaje && !valorEsString) {
			return ErroresDelMensaje
					.create("El mensaje no puede tener un valor que no sea String al enviarlo por HTTP. Debe convertirse a String primero: "
							+ mensajeAEnviar.toPrettyPrint());
		}
		return null;
	}

	public static ValidarMensajesPrevioEnvioWorkUnit create(final ContextoDeOperacionHttp contexto,
			final MensajeVortex mensaje) {
		final ValidarMensajesPrevioEnvioWorkUnit validarMensajes = new ValidarMensajesPrevioEnvioWorkUnit();
		validarMensajes.mensajeAEnviar = mensaje;
		validarMensajes.contexto = contexto;
		return validarMensajes;
	}
}
