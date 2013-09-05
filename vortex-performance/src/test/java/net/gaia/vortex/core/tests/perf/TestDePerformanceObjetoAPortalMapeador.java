/**
 * 04/09/2013 23:45:24 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.portal.api.moleculas.PortalViejo;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;

/**
 * Esta clase prueba la performance de procesamiento de mensajes enviando objetos por el portal
 * 
 * @author D. García
 */
public class TestDePerformanceObjetoAPortalMapeador extends TestDePerformanceObjetoAPortalViejoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceObjetoAPortalViejoSupport#crearPortalATestear()
	 */
	@Override
	protected PortalViejo crearPortalATestear() {
		return PortalMapeador.createForOutputWith(getProcessor(), ReceptorNulo.getInstancia());
	}

}
