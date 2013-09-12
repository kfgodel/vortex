/**
 * 19/08/2013 19:37:14 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.proto;

import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el comportamiento del componente conector que permite derivar mensajes a
 * otros receptores utilizando el mismo thread en el que recibe el mensaje.<br>
 * Este tipo de conector es el default y más común porque es también el más rápido para procesar los
 * mensajes (no requiere administración de tareas y threads).<br>
 * Por ser un conector sincrónico: <br>
 * - Al terminar de ejecutar {@link Conector#recibir(MensajeVortex)} el thread invocante puede estar
 * seguro que el mensaje fue entregado al destino <br>
 * - El thread es bloqueado hasta que la entrega se realiza y procesa. Lo que puede involucrar una
 * larga cadena de componentes<br>
 * <br>
 * Al utilizar el mismo thread para procesar el mensaje recibido, este componete hace crecer la pila
 * tanto como componentes sincrónicos haya conectados en cada rama. Lo cual puede resultar en un
 * {@link StackOverflowError} si hay demasiados componentes o si están conectados sincronamente en
 * circulo<br>
 * <br>
 * Este tipo de componente tiene los siguientes casos de uso:<br>
 * - La red de componentes es lineal (no tiene bucles)<br>
 * - Se busca el máximo de performance en el procesamiento de mensajes<br>
 * - No se requiere una paralelización del procesamiento de los mensajes<br>
 * - Es importante conocer el resultado de la entrega de los mensajes, sin utilizar elementos
 * externos de sincronización
 * 
 * @author D. García
 */
public class ConectorBloqueante extends ConectorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ConectorBloqueante.class);

	public static ConectorBloqueante create() {
		final ConectorBloqueante conector = new ConectorBloqueante();
		// Hacemos que comience desconectado
		conector.desconectar();
		return conector;
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Chequeo para evitar el toShortString() si no es necesario
		if (Loggers.ATOMOS.isDebugEnabled()) {
			Loggers.ATOMOS.debug("Delegando a nodo[{}] el mensaje[{}]", conectado.toShortString(), mensaje);
		}
		// No paso por el delegado, o no podemos saberlo, en cualquier caso lo entregamos
		try {
			conectado.recibir(mensaje);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + conectado
					+ "]. Descartando", e);
			ReceptorNulo.getInstancia().recibir(mensaje);
		}
		// Nada más que hacer
	}
}
