/**
 * 13/06/2012 00:47:39 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa comportamiento base para las sub clases de {@link Nexo} utilizando un
 * procesador de tareas interno para realizar las tareas, y teniendo un destino único al cual
 * pasarle los mensajes
 * 
 * @author D. García
 */
public abstract class NexoSupport extends ReceptorConProcesador implements Nexo {
	private static final Logger LOG = LoggerFactory.getLogger(NexoSupport.class);

	private Receptor destino;
	public static final String destino_FIELD = "destino";

	/**
	 * Inicializa esta instancia con las dependencias mínimas
	 * 
	 * @param processor
	 *            El procesador para las tareas internas
	 * @param delegado
	 *            El delegado que debe recibir el mensaje
	 */
	protected void initializeWith(final TaskProcessor processor, final Receptor delegado) {
		super.initializeWith(processor);
		setDestino(delegado);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(destino_FIELD, getDestino())
				.toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	
	public void conectarCon(final Receptor destino) {
		setDestino(destino);
	}

	/**
	 * En esta implementación el nexo queda conectado al {@link ReceptorNulo}
	 * 
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	
	public void desconectarDe(final Receptor destino) {
		if (!getDestino().equals(destino)) {
			LOG.debug("Se intentó desconectar un nexo[{}] de un destino[{}] al que no estaba conectado. Ignorando",
					this, destino);
			return;
		}
		setDestino(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	
	public void setDestino(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El delegado del nexo no puede ser null. A lo sumo un "
					+ ReceptorNulo.class);
		}
		this.destino = destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#getDestino()
	 */
	
	public Receptor getDestino() {
		return destino;
	}

}