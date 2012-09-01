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
package net.gaia.vortex.core.impl.atomos.ids;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoFiltroDuplicados;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase provee la implementación base de un multiplexor que identifica los mensajes que manda
 * para poder descartar los propios al recibirlos
 * 
 * @author D. García
 */
public abstract class MultiplexorIdentificadorSupport extends ComponenteConProcesadorSupport implements Multiplexor,
		ComponenteConMemoria {

	private MultiplexorParalelo multiplexorDeSalida;
	private NexoFiltroDuplicados procesoDeEntrada;

	protected MultiplexorParalelo getMultiplexorDeSalida() {
		return multiplexorDeSalida;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		multiplexorDeSalida.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		multiplexorDeSalida.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		Loggers.ATOMOS.trace("Recibido en nodo[{}] el mensaje[{}]", this.toShortString(), mensaje);
		Loggers.ATOMOS.debug("Delegando a atomo[{}] el mensaje[{}] desde el nodo[{}]",
				new Object[] { procesoDeEntrada.toShortString(), mensaje, this.toShortString() });
		procesoDeEntrada.recibir(mensaje);
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		multiplexorDeSalida = MultiplexorParalelo.create(processor);
		procesoDeEntrada = NexoFiltroDuplicados.create(processor, multiplexorDeSalida);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeComponente_FIELD, getNumeroDeComponente()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = procesoDeEntrada.yaRecibio(mensaje);
		return yaRecibido;
	}
}