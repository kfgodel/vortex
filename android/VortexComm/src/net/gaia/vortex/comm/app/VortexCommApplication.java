/**
 * 14/07/2012 21:59:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.app;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import ar.com.iron.android.extensions.applications.CustomApplication;

/**
 * Esta clase representa la aplicación como punto goblal compartido
 * 
 * @author D. García
 */
public class VortexCommApplication extends CustomApplication {

	private Nodo nodoDeAplicacion;
	private TaskProcessor procesadorDeAplicacion;
	private ClienteDeChatVortex clienteActual;

	public static VortexCommApplication I;

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#getMainThreadName()
	 */
	@Override
	protected String getMainThreadName() {
		return "VortexCommThread";
	}

	public Nodo getNodoDeAplicacion() {
		return nodoDeAplicacion;
	}

	public void setNodoDeAplicacion(Nodo nodoDeAplicacion) {
		this.nodoDeAplicacion = nodoDeAplicacion;
	}

	public TaskProcessor getProcesadorDeAplicacion() {
		return procesadorDeAplicacion;
	}

	public void setProcesadorDeAplicacion(TaskProcessor procesadorDeAplicacion) {
		this.procesadorDeAplicacion = procesadorDeAplicacion;
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#initializeGlobalComponents()
	 */
	@Override
	protected void initializeGlobalComponents() {
		procesadorDeAplicacion = ExecutorBasedTaskProcesor.createOptimun();
		nodoDeAplicacion = MultiplexorParalelo.create(procesadorDeAplicacion);
		I = this;
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#shutdownGlobalComponents()
	 */
	@Override
	public void shutdownGlobalComponents() {
		procesadorDeAplicacion.detener();
	}

	/**
	 * Reemplaza la referencia al cliente actual si hay alguno
	 * 
	 * @param nuevoCliente
	 *            El cliente reemplazante
	 */
	public void reemplazarClienteActual(ClienteDeChatVortex nuevoCliente) {
		if (clienteActual != null) {
			clienteActual.closeAndDispose();
		}
		this.clienteActual = nuevoCliente;
	}

	public ClienteDeChatVortex getClienteActual() {
		return clienteActual;
	}

}
