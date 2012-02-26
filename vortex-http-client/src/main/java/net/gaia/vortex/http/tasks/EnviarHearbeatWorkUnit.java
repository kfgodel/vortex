/**
 * 23/02/2012 00:16:21 Copyright (C) 2011 Darío L. García
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el cliente para enviar un heartbeat al server si
 * no hubo comunicación durante el periodo de tiempo establecido.<br>
 * El heartbeat nos es más ni menos que un wrapper vacío
 * 
 * @author D. García
 */
public class EnviarHearbeatWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarHearbeatWorkUnit.class);

	private ContextoDeOperacionHttp contexto;

	private Long proximaEjecucion;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Verificamos que la sesion no esté cerrada
		if (contexto.getSesionInvolucrada().estaCerrada()) {
			// No tenemos que hacer más nada. Dejamos de ejecutarnos
			return;
		}

		if (proximaEjecucion == null) {
			// Es la primera vez, tenemos que esperar para mandar el primer heartbeat
			calcularProximaEjecucion();
			LOG.debug("Programando primer hearbeat de la sesion[{}]", contexto.getSesionInvolucrada());
			programarProximaEjecucion();
			return;
		}

		// Vemos si ya es hora de ejecutar
		final long millisFaltantesParaProximaEjecucion = calcularMillisFaltantesParaProximaEjecucion();
		if (millisFaltantesParaProximaEjecucion > 0) {
			// Todavía no es momento de ejecutarnos, nos auto postergamos
			programarProximaEjecucion();
			return;
		}

		// Es hora de mandar el heartbeat!
		LOG.debug("HEARTBEAT enviado para sesion[{}]", contexto.getSesionInvolucrada().getSessionId());
		final List<MensajeVortex> mensajes = Collections.emptyList();
		final EnviarMensajesWorkUnit enviarVacio = EnviarMensajesWorkUnit.create(contexto, mensajes);
		contexto.getProcessor().process(enviarVacio);

		// Programamos la próxima ejecución
		calcularProximaEjecucion();
		programarProximaEjecucion();
	}

	/**
	 * Agrega esta tarea de manera postergada para ser ejecutada cuando indique la próxima ejecucion
	 */
	private void programarProximaEjecucion() {
		final long milisFaltantes = calcularMillisFaltantesParaProximaEjecucion();
		if (milisFaltantes > 0) {
			final TimeMagnitude proximaEjecucion = TimeMagnitude.of(milisFaltantes, TimeUnit.MILLISECONDS);
			LOG.debug("Proximo HEARTBEAT de la sesion[{}] en {} segs", contexto.getSesionInvolucrada().getSessionId(),
					milisFaltantes / 1000d);
			contexto.getProcessor().processDelayed(proximaEjecucion, this);
		} else {
			LOG.error("HEARTBEAT de la sesion[{}] atrasado. Ejecutando ahora?");
			contexto.getProcessor().process(this);
		}

	}

	/**
	 * Devuelve la cantidad de milisegundos que faltan para la proxima ejecucion. Puede ser negativo
	 * si ya pasamos el momento
	 * 
	 * @return La cantidad de milisegundos que falta para que sea el momento de ejecucion
	 */
	private long calcularMillisFaltantesParaProximaEjecucion() {
		final long momentoActual = getCurrentMillis();
		final long faltantesParaProximaEjecucion = proximaEjecucion - momentoActual;
		return faltantesParaProximaEjecucion;
	}

	/**
	 * Devuelve el momento de ejecucion del proximo heartbeat en milisegundos
	 * 
	 * @return El momento en milisegundos que debe pasar para mandar el proximo heartbeat
	 */
	private void calcularProximaEjecucion() {
		final long momentoActual = getCurrentMillis();
		final long momentoDeEjecucion = momentoActual + getSegundosParaEjecucion() * 1000;
		proximaEjecucion = momentoDeEjecucion;
	}

	/**
	 * Devuelve un timestamp considerado actual como milisegundos
	 */
	private long getCurrentMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * Devuelve la cantidad de segundos que son necesarios entre heartbeats si no hay actividad. <br>
	 * El valor lo tomamos como 2 / 3 del valor límite para cerrar la sesión
	 * 
	 * @return La cantidad de segundos para mandar otro heartbeat
	 */
	private long getSegundosParaEjecucion() {
		final long cantidadLimite = contexto.getCantidadDeSegundosSolicitadosSinActividad();
		final long segundosEntreHeartbeat = (cantidadLimite * 2) / 3;
		return segundosEntreHeartbeat;
	}

	public static EnviarHearbeatWorkUnit create(final ContextoDeOperacionHttp contexto) {
		final EnviarHearbeatWorkUnit name = new EnviarHearbeatWorkUnit();
		name.contexto = contexto;
		return name;
	}

	/**
	 * Recalcula el momento de ejecución de esta tarea.<br>
	 * Si la tarea debe ejecutarse antes de lo previsto se cancela la actual y se agrega una nueva,
	 * si no (la mayoría de los casos) se deja tal cual que se adapta sola.
	 */
	public void recalcularEjecucion() {
		final long momentoEjecucionAnterior = proximaEjecucion;
		calcularProximaEjecucion();
		final boolean debeEjecutarseAntesDeLoPrevisto = proximaEjecucion < momentoEjecucionAnterior;
		if (debeEjecutarseAntesDeLoPrevisto) {
			// Tenemos que cancelar la tarea actual
			cancelarEjecucion();
			// Y programarla para más temprano
			programarProximaEjecucion();
		}
	}

	/**
	 * Cancela la ejecución de esta tarea en el procesador
	 */
	public void cancelarEjecucion() {
		contexto.getProcessor().removeTasksMatching(new TaskCriteria() {
			@Override
			public boolean matches(final WorkUnit workUnit) {
				final boolean esEstaTarea = workUnit == EnviarHearbeatWorkUnit.this;
				return esEstaTarea;
			}
		});
	}
}
