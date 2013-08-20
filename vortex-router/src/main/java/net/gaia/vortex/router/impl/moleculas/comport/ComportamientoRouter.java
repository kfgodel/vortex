/**
 * 22/01/2013 16:26:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.comport;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.portal.impl.transformaciones.GenerarIdEnMensaje;
import net.gaia.vortex.router.impl.atomos.MultiplexorDePatas;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comportamiento bidi que tiene el router
 * 
 * @author D. García
 */
public class ComportamientoRouter implements ComportamientoBidi {

	public static ComportamientoRouter create() {
		final ComportamientoRouter comportamiento = new ComportamientoRouter();
		return comportamiento;
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.comport.ComportamientoBidi#crearFlujoParaMensajesRecibidos()
	 */
	
	public FlujoVortexViejo crearFlujoParaMensajesRecibidos(final TaskProcessor processor) {
		// Antes que nada filtramos los mensajes duplicados que ya recibimos
		final NexoSinDuplicados filtroDeDuplicados = NexoSinDuplicados.create(processor, ReceptorNulo.getInstancia());

		// Todo lo que entra en el router va a parar a las patas
		final Multiplexor multiplexorDePatas = MultiplexorDePatas.create(processor);
		filtroDeDuplicados.setDestino(multiplexorDePatas);

		final FlujoVortexViejo flujoDeMensajes = FlujoInmutableViejo.create(filtroDeDuplicados, multiplexorDePatas);
		return flujoDeMensajes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.comport.ComportamientoBidi#obtenerGeneradorDeIdParaMensajes()
	 */
	
	public GenerarIdEnMensaje obtenerGeneradorDeIdParaMensajes() {
		// Generamos uno nuevo
		return GenerarIdEnMensaje.create(GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId());
	}
}
