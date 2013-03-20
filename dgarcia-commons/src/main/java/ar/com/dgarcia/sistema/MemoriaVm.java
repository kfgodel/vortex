/**
 * 30/06/2012 22:45:40 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.sistema;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase permite obtener información de la memoria de la Vm
 * 
 * @author D. García
 */
public class MemoriaVm {

	private Runtime vm;
	/**
	 * Constante para devolver valores en megas como fracción
	 */
	public static final double MEGAS = 1024 * 1024;

	public static MemoriaVm create() {
		final MemoriaVm memoria = new MemoriaVm();
		memoria.vm = Runtime.getRuntime();
		return memoria;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con("libres", getMegasLibresDentroDeVm()).con("usada", getMegasOcupadosDentroDeLaVm())
				.con("usables", getMegasMaximosParaLaVm()).toString();
	}

	/**
	 * Devuelve la cantidad de megas actualmente libres según la VM para utilizar en nuevos objetos
	 * o heap
	 * 
	 * @return La cantidad de megas libres dentro del espacio alocado por la VM en el sistema
	 *         operativo
	 */
	public double getMegasLibresDentroDeVm() {
		final double libres = vm.freeMemory() / MEGAS;
		return libres;
	}

	/**
	 * Devuelve la cantidad de megas pedidos por la VM al Sistema Operativo actualmente
	 * 
	 * @return La cantidad de megas usados por el proceso Java
	 */
	public double getMegasUtilizadosPorLaVm() {
		final double usados = vm.totalMemory() / MEGAS;
		return usados;
	}

	/**
	 * Devuelve la cantidad de megas que el proceso puede pedirle al S.O. según los parámetros de
	 * arranque (-Xmx)
	 * 
	 * @return la cantidad de megas utilizables como máximo por la VM
	 */
	public double getMegasMaximosParaLaVm() {
		final double usables = vm.maxMemory() / MEGAS;
		return usables;
	}

	/**
	 * Devuelve la cantidad de megas que están siendo utilizados actualmente dentro de la VM por
	 * objetos existentes o aún no liberada por el GC
	 * 
	 * @return La cantidad de megas utilizada en objetos
	 */
	public double getMegasOcupadosDentroDeLaVm() {
		final double ocupados = getMegasUtilizadosPorLaVm() - getMegasLibresDentroDeVm();
		return ocupados;
	}

}
