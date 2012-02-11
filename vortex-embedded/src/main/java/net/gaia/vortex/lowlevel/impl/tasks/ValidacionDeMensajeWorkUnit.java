/**
 * 27/11/2011 21:24:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.validator.VortexValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea que valida la conformidad del mensaje al protocolo
 * 
 * @author D. García
 */
public class ValidacionDeMensajeWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(ValidacionDeMensajeWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		final ReceptorVortex emisor = contexto.getEmisor();
		return emisor == receptor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final MensajeVortex mensajeRecibido = contexto.getMensaje();

		LOG.debug("Validando mensaje[{}]", mensajeRecibido);
		final String descripcionError = validarMensaje(mensajeRecibido);
		if (descripcionError != null) {
			LOG.warn("Error[{}] encontrado en mensaje[{}] al validar", descripcionError, mensajeRecibido);
			// Existe un error en el mensaje intentamos devolver feedback al respecto
			final DevolverAcuseFallaRecepcionWorkUnit devolucionWorkUnit = DevolverAcuseFallaRecepcionWorkUnit.create(
					contexto, descripcionError);
			contexto.getProcesador().process(devolucionWorkUnit);
			return;
		}

		LOG.debug("Validación superada para mensaje[{}]", mensajeRecibido);
		final VerificarDuplicadoWorkUnit verificarWorkUnit = VerificarDuplicadoWorkUnit.create(contexto);
		contexto.getProcesador().process(verificarWorkUnit);
	}

	/**
	 * Valida la estructura del mensaje generando una descripción de error si no matchea
	 * 
	 * @param mensaje
	 *            Mensaje a validar
	 * @return El error con la descripción del mensaje
	 */
	private String validarMensaje(final MensajeVortex mensaje) {
		final String firstErrorCode = VortexValidator.getFirstErrorCodeFrom(mensaje);
		return firstErrorCode;
	}

	public static ValidacionDeMensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final ValidacionDeMensajeWorkUnit validacion = new ValidacionDeMensajeWorkUnit();
		validacion.contexto = contexto;
		return validacion;
	}
}
