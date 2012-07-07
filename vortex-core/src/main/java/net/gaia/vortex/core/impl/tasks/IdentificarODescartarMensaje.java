/**
 * 07/07/2012 19:09:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.ids.NexoIdentificador;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.NoPasoPreviamente;
import net.gaia.vortex.core.impl.transformaciones.RegistrarPaso;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la acción realizada por el {@link NexoIdentificador} para identificar un
 * mensaje o descartarlo si ya lo identificó previamente.<br>
 * Esta clase es equivalente a conectar un {@link NexoFiltro} y luego un {@link NexoTransformador}
 * que hagan las operaciones que hace esta tarea. La razón para utilizar esta tarea como una sola
 * acción es de performance
 * 
 * @author D. García
 */
public class IdentificarODescartarMensaje implements WorkUnit {

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";
	private Receptor delegado;
	public static final String delegado_FIELD = "delegado";
	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";
	private NoPasoPreviamente noPasoPreviamente;
	private RegistrarPaso registrarPaso;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		final boolean elMensajeYaTieneRegistradoElId = !noPasoPreviamente.esCumplidaPor(mensaje);
		if (elMensajeYaTieneRegistradoElId) {
			Loggers.ATOMOS.debug("Detectado duplicado ID=[{}] en el mensaje[{}]. Descartando mensaje", identificador,
					mensaje);
			// Descartamos el mensaje
			return null;
		}
		final MensajeVortex mensajeConIdRegistrado = registrarPaso.transformar(mensaje);

		Loggers.ATOMOS.debug("Agregado ID[{}] a mensaje[{}]", identificador, mensajeConIdRegistrado);
		return DelegarMensaje.create(mensajeConIdRegistrado, delegado);
	}

	/**
	 * Crea la tarea para identificar un mensaje con el identificador pasado sólo si no lo tenía
	 * antes. Si lo tenía se descarta el mensaje
	 * 
	 * @param mensaje
	 *            El mensaje a procesar
	 * @param identificador
	 *            El identificador que será asignado o evaluado en el mensaje
	 * @param destino
	 *            El delegado al que se le entrega el mensaje si no había pasado antes por el
	 *            identificador
	 * @return La tarea creada
	 */
	public static IdentificarODescartarMensaje create(final MensajeVortex mensaje,
			final IdentificadorVortex identificador, final Receptor destino) {
		final IdentificarODescartarMensaje identificar = new IdentificarODescartarMensaje();
		identificar.mensaje = mensaje;
		identificar.delegado = destino;
		identificar.identificador = identificador;
		identificar.noPasoPreviamente = NoPasoPreviamente.por(identificador);
		identificar.registrarPaso = RegistrarPaso.por(identificador);
		return identificar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(identificador_FIELD, identificador).con(delegado_FIELD, delegado)
				.con(mensaje_FIELD, mensaje).toString();
	}

}
