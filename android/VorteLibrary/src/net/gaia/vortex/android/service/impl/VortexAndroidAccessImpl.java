/**
 * 14/07/2012 15:41:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service.impl;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.android.service.VortexAndroidAccess;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.ids.MultiplexorIdentificador;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta clase implementa el facade de vortex para android utilizando un
 * {@link MultiplexorIdentificador} como punto central de conexión para el resto de los componentes
 * 
 * @author D. García
 */
public class VortexAndroidAccessImpl implements VortexAndroidAccess, Disposable {

	private TaskProcessor processor;
	private MultiplexorIdentificador nodoCentral;

	/**
	 * @see net.gaia.vortex.android.service.VortexAndroidAccess#getNodoCentral()
	 */
	public MultiplexorIdentificador getNodoCentral() {
		return nodoCentral;
	}

	public void setNodoCentral(MultiplexorIdentificador nodoCentral) {
		this.nodoCentral = nodoCentral;
	}

	public static VortexAndroidAccessImpl create() {
		VortexAndroidAccessImpl impl = new VortexAndroidAccessImpl();
		impl.processor = ExecutorBasedTaskProcesor.createOptimun();
		IdentificadorVortex identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		impl.nodoCentral = MultiplexorIdentificador.create(impl.processor, identificador);
		return impl;
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose() {
		processor.detener();
	}

}