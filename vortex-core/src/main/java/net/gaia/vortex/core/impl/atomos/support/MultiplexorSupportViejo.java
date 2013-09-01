/**
 * 13/01/2013 15:27:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.atomos.forward.MultiplexorViejo;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;
import net.gaia.vortex.core.prog.Decision;
import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase define comportamiento base para las subclases de {@link MultiplexorViejo} que utiliza
 * un procesador de tareas para procesar sus mensajes
 * 
 * @author D. García
 */
@Deprecated
public abstract class MultiplexorSupportViejo extends ReceptorConProcesador implements MultiplexorViejo {

	/**
	 * La lista de destinos, implementada con un {@link CopyOnWriteArrayList} porque esperamos más
	 * recorridas que modificaciones
	 */
	private List<Receptor> destinos;
	public static final String destinos_FIELD = "destinos";

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.MultiplexorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino del multiplexor no puede ser null");
		}
		destinos.add(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.MultiplexorViejo#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	public void desconectarDe(final Receptor destino) {
		destinos.remove(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */

	@Override
	@HasDependencyOn(Decision.LA_LISTA_DE_DESTINOS_ES_UN_COPY_ON_WRITE)
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		destinos = new CopyOnWriteArrayList<Receptor>();
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(destinos_FIELD, destinos)
				.toString();
	}

	protected List<Receptor> getDestinos() {
		return destinos;
	}

}
