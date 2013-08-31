/**
 * 01/09/2012 02:20:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.memoria;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.impl.atomos.support.NexoFiltroSupport;
import net.gaia.vortex.core.impl.condiciones.EsMensajeNuevo;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;

/**
 * Esta clase representa el atomo de vortex que permite filtrar mensajes duplicados descartándolos.<br>
 * Un mensaje es duplicado si pasa por segunda vez con el mismo ID por este filtro
 * 
 * @author D. García
 */
@Deprecated
public class NexoSinDuplicadosViejo extends NexoFiltroSupport implements ComponenteConMemoria {

	public static final int CANTIDAD_MENSAJES_RECORDADOS = 1000;

	private MemoriaDeMensajes memoria;

	public static NexoSinDuplicadosViejo create(final TaskProcessor processor, final Receptor delegado) {
		final MemoriaDeMensajes memoriaDelNexo = MemoriaLimitadaDeMensajes.create(CANTIDAD_MENSAJES_RECORDADOS);
		return create(processor, memoriaDelNexo, delegado);
	}

	public static NexoSinDuplicadosViejo create(final TaskProcessor processor, final MemoriaDeMensajes memoriaDelNexo,
			final Receptor delegado) {
		final NexoSinDuplicadosViejo nexo = new NexoSinDuplicadosViejo();
		nexo.memoria = memoriaDelNexo;
		final Condicion condicion = EsMensajeNuevo.create(memoriaDelNexo);
		nexo.initializeWith(processor, delegado, condicion);
		return nexo;
	}

	/**
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = memoria.tieneRegistroDe(mensaje);
		return yaRecibido;
	}
}
