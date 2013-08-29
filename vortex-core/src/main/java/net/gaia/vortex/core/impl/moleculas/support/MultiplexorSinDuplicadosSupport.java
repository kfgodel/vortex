/**
 * 27/06/2012 20:16:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas.support;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.api.atomos.forward.MultiplexorViejo;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParaleloViejo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicadosViejo;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase provee la implementación base de un multiplexor que identifica los mensajes que manda
 * para poder descartar los propios al recibirlos
 * 
 * @author D. García
 */
public abstract class MultiplexorSinDuplicadosSupport extends NodoMoleculaSupport implements MultiplexorViejo,
		ComponenteConMemoria {

	private NexoSinDuplicadosViejo procesoDeEntrada;

	protected void initializeWith(final TaskProcessor processor) {
		// A la salida enviamos a muchos
		final MultiplexorParaleloViejo multiplexorDeSalida = MultiplexorParaleloViejo.create(processor);
		// A la entrada evitamos los duplicados
		procesoDeEntrada = NexoSinDuplicadosViejo.create(processor, multiplexorDeSalida);
		final FlujoVortexViejo flujo = FlujoInmutableViejo.create(procesoDeEntrada, multiplexorDeSalida);
		initializeWith(flujo);
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = procesoDeEntrada.yaRecibio(mensaje);
		return yaRecibido;
	}
}