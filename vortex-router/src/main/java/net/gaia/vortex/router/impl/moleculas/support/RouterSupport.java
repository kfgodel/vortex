/**
 * 26/01/2013 11:45:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.support;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.router.api.moleculas.Router;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.moleculas.NodoBidi;
import net.gaia.vortex.router.impl.moleculas.comport.ComportamientoRouter;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

/**
 * Esta clase base define comportamiento para los routers
 * 
 * @author D. García
 */
public abstract class RouterSupport extends NodoBidi implements Router {

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.NodoBidi#calcularFiltroDeEntradaPara(net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */
	
	protected Condicion calcularFiltroDeEntradaPara(final PataBidireccional pataConectora) {
		ParteDeCondiciones parteDeLaPata = null;
		if (pataConectora != null) {
			parteDeLaPata = pataConectora.getParteDeCondicion();
		}
		final Condicion condicioDelRestoDeLasPatas = getConjuntoDeCondiciones().getCondicionDelConjuntoMenos(
				parteDeLaPata);
		return condicioDelRestoDeLasPatas;
	}

	/**
	 * Inicializa este componente con el comportamiento de router
	 * 
	 * @param processor
	 *            El procesador para crear el componente
	 */
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor, ComportamientoRouter.create());
	}
}