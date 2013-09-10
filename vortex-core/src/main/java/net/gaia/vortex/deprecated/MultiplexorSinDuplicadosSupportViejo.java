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
package net.gaia.vortex.deprecated;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase provee la implementación base de un multiplexor que identifica los mensajes que manda
 * para poder descartar los propios al recibirlos
 * 
 * @author D. García
 */
@Deprecated
public abstract class MultiplexorSinDuplicadosSupportViejo extends NodoMoleculaSupportViejo implements MultiplexorViejo,
		ComponenteConMemoriaViejo {

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
	 * @see net.gaia.vortex.deprecated.ComponenteConMemoriaViejo#yaRecibio(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = procesoDeEntrada.yaRecibio(mensaje);
		return yaRecibido;
	}
}