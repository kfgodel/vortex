/**
 * 07/12/2012 17:53:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador;


/**
 * Esta clase es base de los elementos usados para las simulaciones.<br>
 * Las subclases de esta, heredan todo el comportamiento de manejo del simulador
 * 
 * @author D. García
 */
public class ComponenteSimulable {

	private Simulador simulador;
	public static final String simulador_FIELD = "simulador";

	protected Simulador getSimulador() {
		return simulador;
	}

	protected void setSimulador(final Simulador simulador) {
		this.simulador = simulador;
	}

	/**
	 * "procesa" el paso indicado agregándolo al simulador para ser ejecutado
	 * 
	 * @param proximoPaso
	 *            El paso a ejecutar en el simulador
	 */
	protected void procesar(final PasoSimulacion proximoPaso) {
		this.getSimulador().agregar(proximoPaso);
	}

}
