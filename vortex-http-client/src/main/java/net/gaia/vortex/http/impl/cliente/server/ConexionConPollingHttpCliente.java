/**
 * 05/08/2012 15:44:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.tasks.MinMaxWorkUnit;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.VortexHttpException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una conexión con un servidor http vortex que utiliza polling internamente
 * para mantener viva la sesión y entregar los mensajes recibidos como si fuera asíncrona
 * 
 * @author D. García
 */
public class ConexionConPollingHttpCliente {

	private static final int POLLING_INICIAL_EN_MILLIS = 5 * 1000;

	private ConexionHttpCliente conexionInterna;
	public static final String conexionInterna_FIELD = "conexionInterna";

	private ConcurrentLinkedQueue<MensajeVortex> mensajesSalientes;
	public static final String mensajesSalientes_FIELD = "mensajesSalientes";

	private HandlerHttpDeMensajesRecibidos handlerDeMensajes;

	private TaskProcessor processor;

	private MinMaxWorkUnit tareaDeIntercambioDeMensajes;

	private SubmittedTask controlDeTareaDeIntercambio;

	public static ConexionConPollingHttpCliente create(final TaskProcessor procesor,
			final ServerVortexHttpRemoto serverRemoto, final VortexHttpTextualizer textualizer,
			final HandlerHttpDeMensajesRecibidos handler) {
		final ConexionConPollingHttpCliente conexion = new ConexionConPollingHttpCliente();
		conexion.mensajesSalientes = new ConcurrentLinkedQueue<MensajeVortex>();
		conexion.conexionInterna = ConexionHttpCliente.create(serverRemoto, textualizer);
		conexion.handlerDeMensajes = handler;
		conexion.processor = procesor;
		conexion.tareaDeIntercambioDeMensajes = MinMaxWorkUnit.crearWrapperDe(new WorkUnit() {

			public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
				conexion.intercambiarMensajesConServer();
			}
		}, procesor, 0, POLLING_INICIAL_EN_MILLIS);
		return conexion;
	}

	/**
	 * Envía los mensajes acumulados y recibe los nuevos
	 */
	protected void intercambiarMensajesConServer() {
		final ArrayList<MensajeVortex> mensajesAEnviar = new ArrayList<MensajeVortex>(mensajesSalientes.size());
		final Iterator<MensajeVortex> iteradorDePendientes = mensajesSalientes.iterator();
		while (iteradorDePendientes.hasNext()) {
			final MensajeVortex enviable = iteradorDePendientes.next();
			mensajesAEnviar.add(enviable);
			iteradorDePendientes.remove();
		}
		final List<MensajeVortex> mensajesRecibidos = conexionInterna.intercambiar(mensajesAEnviar);
		ajustarEsperasDeLaTarea();
		handlerDeMensajes.onMensajesRecibidos(mensajesRecibidos);
	}

	/**
	 * Ajusta los tiempos de espera de la tarea
	 */
	private void ajustarEsperasDeLaTarea() {
		final long esperaMinimaDelServer = conexionInterna.getEsperaMinima();
		tareaDeIntercambioDeMensajes.setEsperaMinima(esperaMinimaDelServer);
		final long esperaMaximaDelServer = conexionInterna.getEsperaMaxima();
		if (esperaMaximaDelServer < getPollingMillis()) {
			// Hay que ajustar el polling para que sea más chico
			final long nuevoPolling = Math.max(esperaMinimaDelServer, esperaMaximaDelServer / 2);
			setPollingMillis(nuevoPolling);
		}
	}

	/**
	 * Devuelve el plazo máximo en milis de cada polling
	 */
	public long getPollingMillis() {
		return tareaDeIntercambioDeMensajes.getEsperaMaxima();
	}

	/**
	 * Establece la espera máxima entre pedidos de polling
	 * 
	 * @param millisEntrePolling
	 *            La espera en milis
	 */
	public void setPollingMillis(final long millisEntrePolling) {
		tareaDeIntercambioDeMensajes.setEsperaMaxima(millisEntrePolling);
	}

	public void iniciarConexion() throws VortexHttpException {
		conexionInterna.setEsperaMaxima(POLLING_INICIAL_EN_MILLIS * 2);
		conexionInterna.conectarAlServer();
		controlDeTareaDeIntercambio = processor.process(tareaDeIntercambioDeMensajes);
	}

	public void terminarConexion() throws VortexHttpException {
		controlDeTareaDeIntercambio.cancelExecution(true);
		conexionInterna.desconectarDelServer();
	}

	public void enviar(final MensajeVortex mensaje) {
		mensajesSalientes.add(mensaje);
		intentarEnviarMensajesPendientes();
	}

	/**
	 * Si no es demasiado teprano ejecutar la tarea de envío de mensajes, procesando los recibidos
	 */
	private void intentarEnviarMensajesPendientes() {
		controlDeTareaDeIntercambio = processor.process(tareaDeIntercambioDeMensajes);
	}

	public ConexionHttpCliente getConexionInterna() {
		return conexionInterna;
	}

	/**
	 * Devuelve el ID de la sesión utilizada por esta conexión
	 * 
	 * @return El id que identifica la sesión http
	 */
	public String getIdDeSesion() {
		return this.conexionInterna.getIdDeSesion();
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con("sesion", conexionInterna.getIdDeSesion())
				.con("minimo", this.conexionInterna.getEsperaMinima())
				.con("maximo", this.conexionInterna.getEsperaMaxima())
				.con("polling", tareaDeIntercambioDeMensajes.getEsperaMaxima())
				.con(mensajesSalientes_FIELD, mensajesSalientes).toString();
	}

}
