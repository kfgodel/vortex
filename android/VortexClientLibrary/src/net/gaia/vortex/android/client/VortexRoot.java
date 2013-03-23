/**
 * 22/09/2012 23:54:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.client;

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.router.api.moleculas.Router;
import net.gaia.vortex.router.impl.moleculas.RouterBidi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase representa el punto de acceso al nodo central y su procesador para ser compartido por
 * los distintos portales de la aplicación
 * 
 * @author D. García
 */
public class VortexRoot {
	private static final Logger LOG = LoggerFactory.getLogger(VortexRoot.class);

	/**
	 * Procesador compartido por todos los componentes Vortex de una aplicación Android
	 */
	private static final AtomicReference<TaskProcessor> rootProcessorRef = new AtomicReference<TaskProcessor>();

	/**
	 * Nodo raiz para utilizar como punto de conexión para los componentes de la aplicación
	 */
	private static final AtomicReference<Nodo> rootNodeRef = new AtomicReference<Nodo>();

	/**
	 * Devuelve el nodo global compartido entre todos los componentes.<br>
	 * Este nodo sirve de punto central de la aplicación para el intercambio de mensajes.<br>
	 * <br>
	 * Este nodo actua como singleton creandosé si no existe previamente. Se garantiza que sólo un
	 * nodo es creado, bloqueando al resto de los threads hasta que es creado
	 * 
	 * @return El nodo global
	 */
	public static Nodo getNode() {
		Nodo nodo = rootNodeRef.get();
		if (nodo != null) {
			// Ya lo crearon
			return nodo;
		}
		return crearNodoUnico();
	}

	/**
	 * Devuelve el procesador para compartir entre los distintos componentes
	 * 
	 * @return El procesador de tareas
	 */
	public static TaskProcessor getProcessor() {
		TaskProcessor processor = rootProcessorRef.get();
		if (processor != null) {
			// Ya fue creado
			return processor;
		}
		return crearProcessorUnico();
	}

	/**
	 * Intenta crear un procesador unico para la referencia de la clase. Puede fallar si se adelanta
	 * otro thread en cuyo caso devuelve el creado por el otro thread
	 * 
	 * @return El procesador usado como unica instancia
	 */
	private static TaskProcessor crearProcessorUnico() {
		ExecutorBasedTaskProcesor possibleProcessor = ExecutorBasedTaskProcesor.create(createAndroidProcessorConfig());
		boolean setted = rootProcessorRef.compareAndSet(null, possibleProcessor);
		if (setted) {
			// El que creamos el es procesador que se utilizara
			LOG.info("Creado processor global: {}", possibleProcessor);
			return possibleProcessor;
		}
		TaskProcessor uniqueProcessor = rootProcessorRef.get();
		if (uniqueProcessor == null) {
			throw new UnhandledConditionException(
					"La referencia al procesador no permitio setearle uno nuevo pero devolvio null como actual");
		}
		return uniqueProcessor;
	}

	/**
	 * Intenta crear un unico nodo central como referencia de la clase. Si otro thread se adelanta,
	 * se usa el creado por el otro thread
	 * 
	 * @return El nodo unico
	 */
	private static Nodo crearNodoUnico() {
		Router possibleNode = RouterBidi.create(getProcessor());
		boolean setted = rootNodeRef.compareAndSet(null, possibleNode);
		if (setted) {
			LOG.info("Creado nodo raiz global: {}", possibleNode);
			// Somos los primeros en setear, el nodo que creamos es el unico
			return possibleNode;
		}
		Nodo uniqueNode = rootNodeRef.get();
		if (uniqueNode == null) {
			throw new UnhandledConditionException(
					"La referencia al no no permitio setearle uno nuevo pero devolvio null como actual");
		}
		return uniqueNode;
	}

	/**
	 * Crea una configuración compartida para android
	 */
	private static TaskProcessorConfiguration createAndroidProcessorConfig() {
		TaskProcessorConfiguration androidConfig = TaskProcessorConfiguration.createOptimun();
		// Tomamos el mínimo como parametro
		int fixedThreadPoolSize = androidConfig.getMinimunThreadPoolSize();
		if (fixedThreadPoolSize < 1) {
			fixedThreadPoolSize = 1;
		}
		// Queda entre 1 y 2 threads segun la cantidad de micros que tenga
		if (fixedThreadPoolSize > 2) {
			fixedThreadPoolSize = 2;
		}
		androidConfig.setMinimunThreadPoolSize(fixedThreadPoolSize);
		androidConfig.setMaximunThreadPoolSize(fixedThreadPoolSize);
		return androidConfig;
	}
}
