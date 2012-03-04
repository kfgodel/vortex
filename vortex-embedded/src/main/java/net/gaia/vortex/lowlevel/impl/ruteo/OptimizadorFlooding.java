/**
 * 04/03/2012 17:51:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta clase representa el optimizador de ruteo que en realidad no optimiza. Sólo envía el mensaje
 * a cada nodo con el tag
 * 
 * @author D. García
 */
public class OptimizadorFlooding implements OptimizadorDeRuteo {

	public static OptimizadorFlooding create() {
		final OptimizadorFlooding optimizador = new OptimizadorFlooding();
		return optimizador;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#filtrar(net.gaia.vortex.lowlevel.impl.ruteo.SeleccionDeReceptores)
	 */
	@Override
	public void filtrar(final SeleccionDeReceptores interesadosEnElMensaje) {
		// En flooding el mensaje les llega a todos
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#nodoAgregado(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void nodoAgregado(final ReceptorVortex nuevoReceptor) {
		// No se modifica nada en base al nodo
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#nodoQuitado(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void nodoQuitado(final ReceptorVortex receptorQuitado) {
		// No cambia nada en base al nodo
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.OptimizadorDeRuteo#ajustarEnBaseA(net.gaia.vortex.lowlevel.impl.ruteo.ReportePerformanceRuteo)
	 */
	@Override
	public void ajustarEnBaseA(final ReportePerformanceRuteo reportePerformance) {
		// No hay un cambio de estado en este optimizador
	}

}
