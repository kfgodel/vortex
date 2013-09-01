/**
 * Created on: Sep 1, 2013 5:09:26 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.proto;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.tasks.EntregarAsincromente;

/**
 * Esta clase representa un conector que utiliza threads propios para entregar los mensajes que
 * recibe al receptor conectado.<br>
 * A diferencia del {@link ConectorBloqueante} al recibir un mensaje esta instancia libera al thread
 * que entrega el mensaje y difiere la entrega del mensaje para ser realizada posteriormente.<br>
 * Esto implica principalmente dos cosas (tipicas de la asincronia):<br>
 * - El thread original no puede asumir que el mensaje fue realmente entregado<br>
 * - El thread original no es bloqueado hasta que se realiza la entrega<br>
 * <br>
 * Al utilizar un {@link TaskProcessor} para procesar los mensajes recibidos en un pool de threads,
 * este componente incurre en una penalización de perfomance pequeña al requerir tareas y
 * sincronización de threads. Esta penalización se vuelve más significativa al tener mucha
 * concurrencia de threads externos sobre el mismo componente<br>
 * <br>
 * Este tipo de conector tiene los siguientes casos de uso:<br>
 * - Se quiere independizar la ejecucion entre dos sistemas (por ejemplo el que hace IO del que
 * procesa la logica)<br>
 * - Se tiene bucles en el circuito de mensajes, por lo que al utilizar conectores sincrónicos la
 * pila crece indefinidamente por recursividad<br>
 * Esto también es válido para cadenas muy largas de componentes (más componentes que niveles
 * disponibles en la pila).<br>
 * - Se quiere ejecutar componentes en paralelo, utilizando distintos threads para distintas ramas
 * de la red<br>
 * - Existen esperas inevitables durante el procesamiento de los mensajes, que bloquearía al
 * procesamiento de toda la red
 */
public class ConectorAsincrono extends ConectorSupport {

	/**
	 * El procesor de las tareas en un poll de threads propio
	 */
	private TaskProcessor processor;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Creamos la tarea de delegación y la ejecutamos en el pool, liberando al thread actual
		// Usamos el receptor nulo en caso de error al entregar
		final EntregarAsincromente delegacion = EntregarAsincromente.create(mensaje, getConectado(),
				ReceptorNulo.getInstancia());
		processor.process(delegacion);
	}

	public static ConectorAsincrono create(final TaskProcessor processor) {
		final ConectorAsincrono conector = new ConectorAsincrono();
		conector.processor = processor;
		// Hacemos que comience desconectado
		conector.desconectar();
		return conector;
	}
}
