/**
 * 25/01/2013 14:40:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.atomos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParaleloViejo;
import net.gaia.vortex.core.impl.atomos.support.MultiplexorSupport;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import net.gaia.vortex.router.impl.tasks.MultiplexarAPatas;

/**
 * Esta clase representa un multiplexor que a diferencia del {@link MultiplexorParaleloViejo} puede
 * discriminar si un mensaje va hacia una pata y sólo se lo entrega a esa
 * 
 * @author D. García
 */
public class MultiplexorDePatas extends MultiplexorSupport {

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.MultiplexorParaleloViejo#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final Collection<PataBidireccional> patas = obtenerPatas();
		final MultiplexarAPatas multiplexar = MultiplexarAPatas.create(mensaje, patas, getProcessor());
		return multiplexar;
	}

	/**
	 * Toma todas las conexiones de este multiplexor que son patas y las devuelve
	 * 
	 * @return la coleccion de patas a las que este multiplexor está conectado
	 */
	private Collection<PataBidireccional> obtenerPatas() {
		final List<Receptor> destinos = getDestinos();
		final Collection<PataBidireccional> patasConectadas = new ArrayList<PataBidireccional>(destinos.size());
		for (final Receptor destino : destinos) {
			if (destino instanceof PataBidireccional) {
				patasConectadas.add((PataBidireccional) destino);
			}
		}
		return patasConectadas;
	}

	public static MultiplexorDePatas create(final TaskProcessor processor) {
		final MultiplexorDePatas multiplexor = new MultiplexorDePatas();
		multiplexor.initializeWith(processor);
		return multiplexor;
	}

}
