/**
 * 20/08/2013 00:23:53 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos.consumidores;

import net.gaia.vortex.api.basic.consumidores.Consumidor;
import net.gaia.vortex.api.mensajes.ListenerDeMensajes;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un atomo consumidor que utilizar un listener al cual le delega el mensaje
 * conumido.<br>
 * Todo mensaje recibido cesa su circulación
 * 
 * @author D. García
 */
public class AtomoConsumidor extends ReceptorSupport implements Consumidor {
	private static final Logger LOG = LoggerFactory.getLogger(AtomoConsumidor.class);

	private ListenerDeMensajes listener;
	public static final String listener_FIELD = "listener";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		try {
			listener.onMensajeRecibido(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el listener[" + listener + "] al recibir el mensaje[" + mensaje + "]", e);
			ReceptorNulo.getInstancia().recibir(mensaje);
		}
	}

	/**
	 * Crea un receptor que sirve de adaptador para invocar el listener con los mensajes recibidos
	 * 
	 * @param listener
	 *            El listener a invocar
	 * @return El receptor creado
	 */
	public static AtomoConsumidor create(final ListenerDeMensajes listener) {
		final AtomoConsumidor receptor = new AtomoConsumidor();
		receptor.listener = listener;
		return receptor;
	}

	/**
	 * Crea una instancia de este receptor que invocará el {@link Runnable} pasado al recibir un
	 * mensaje
	 * 
	 * @param runnable
	 *            El runable a ejecutar
	 * @return El receptor creado para le ejecución del runnable
	 */
	public static AtomoConsumidor create(final Runnable runnable) {
		final RunnableListenerAdapter asListener = RunnableListenerAdapter.create(runnable);
		return create(asListener);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(listener_FIELD, listener).toString();
	}

}
