/**
 * 27/07/2012 21:43:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.atomos;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.procesador.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.tasks.DelegarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que al recibir un mensaje desde http lo introduce en
 * la red a través del receptor que conoce
 * 
 * @author D. García
 */
public class Deshttpizador extends ComponenteConProcesadorSupport implements Emisor {
	private static final Logger LOG = LoggerFactory.getLogger(Deshttpizador.class);

	private Receptor destino;
	public static final String destino_FIELD = "destino";

	/**
	 * Reemplaza el receptor previo con el nuevo pasado
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino[" + destino + "] no puede ser null en este deshttpizador");
		}
		this.destino = destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		LOG.info("Se intentó desconectar un destino[" + destino + "] del desocketizador. Ignorando");
	}

	public void onMensajeDesdeHttp(final MensajeVortex recibido) {
		// Le pasamos el mensaje que vino desde el request al receptor destino de la red
		final DelegarMensaje delegacion = DelegarMensaje.create(recibido, destino);
		procesarEnThreadPropio(delegacion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(destino_FIELD, destino)
				.toString();
	}

	public static Deshttpizador create(final TaskProcessor processor, final Receptor destino) {
		final Deshttpizador desocketizador = new Deshttpizador();
		desocketizador.initializeWith(processor);
		desocketizador.conectarCon(destino);
		return desocketizador;
	}

}
