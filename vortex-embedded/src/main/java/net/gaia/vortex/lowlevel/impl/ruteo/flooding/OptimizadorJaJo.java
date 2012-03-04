/**
 * 04/03/2012 19:01:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo.flooding;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo;
import net.gaia.vortex.lowlevel.impl.ruteo.ReportePerformanceRuteo;

/**
 * Esta clase representa el optimizador de ruteos que utiliza un estado binario para determinar por
 * dónde circulan los mensajes. <br>
 * Basado en la explicación de lo hecho por Javier y José
 * 
 * @author D. García
 */
public class OptimizadorJaJo implements OptimizadorDeRuteo {

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#debeRecibirMensajeConTag(java.lang.String,
	 *      net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean debeRecibirMensajeConTag(final String tagDelMensaje, final ReceptorVortex interesadoEnElTag) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#nodoAgregado(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void nodoAgregado(final ReceptorVortex nuevoReceptor) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#nodoQuitado(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void nodoQuitado(final ReceptorVortex receptorQuitado) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#ajustarEnBaseA(net.gaia.vortex.lowlevel.impl.ruteo.ReportePerformanceRuteo)
	 */
	@Override
	public void ajustarEnBaseA(final ReportePerformanceRuteo reportePerformance) {
		// TODO Auto-generated method stub

	}

}
