/**
 * 07/07/2013 18:49:53 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.tests;

import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport;

/**
 * Esta clase representa el thread que incrementa el valor de la variable directamentee en un for
 * infinito
 * 
 * @author D. García
 */
public class ThreadIncrementadorBruto extends ThreadBucleSupport {

	public ThreadIncrementadorBruto() {
		super("<> - IncrementadorBruto");
	}

	private VariableTicks variable;

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#run()
	 */
	@Override
	public void run() {
		// Sobreescribir el método nos da mayor performance
		while (running) {
			try {
				realizarAccionRepetida();
			} catch (final Exception e) {
				running = false;
			}
		}
	}

	/**
	 * @see net.gaia.taskprocessor.perf.impl.medidor.ThreadBucleSupport#realizarAccionRepetida()
	 */
	@Override
	protected void realizarAccionRepetida() {
		variable.incrementar();
	}

	public static ThreadIncrementadorBruto create(final VariableTicks variable, final int numeroIdentificador) {
		final ThreadIncrementadorBruto thread = new ThreadIncrementadorBruto();
		thread.variable = variable;
		thread.setName(thread.getName() + " " + numeroIdentificador);
		return thread;
	}
}
