/**
 * 20/05/2012 19:23:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.NodoPortal;
import net.gaia.vortex.core.impl.tasks.EnviarMensajeAOtrosVecinosTask;
import net.gaia.vortex.core.impl.tasks.EnviarMensajeATodosLosVecinosTask;
import net.gaia.vortex.core.impl.tasks.InvocarHandlerDeMensajesTask;

/**
 * Esta clase implementa el nodo de comunicaciones vortex
 * 
 * @author D. García
 */
public class NodoImpl implements NodoPortal {

	private AtomicReference<HandlerDeMensajesVecinos> handlerRef;
	private TaskProcessor processor;
	private ConcurrentLinkedQueue<Nodo> nodosVecinos;

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		final InvocarHandlerDeMensajesTask invocarHandler = InvocarHandlerDeMensajesTask.create(handlerRef, mensaje);
		this.processor.process(invocarHandler);
		final EnviarMensajeAOtrosVecinosTask enviarAOtros = EnviarMensajeAOtrosVecinosTask.create(this, emisor,
				mensaje, nodosVecinos, processor);
		this.processor.process(enviarAOtros);
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#conectarCon(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo vecino) {
		nodosVecinos.add(vecino);
	}

	/**
	 * @see net.gaia.vortex.core.api.Nodo#desconectarDe(net.gaia.vortex.core.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo vecino) {
		nodosVecinos.remove(vecino);
	}

	/**
	 * @see net.gaia.vortex.core.api.NodoPortal#enviarAVecinos(java.lang.Object)
	 */
	@Override
	public void enviarAVecinos(final Object mensaje) {
		final EnviarMensajeATodosLosVecinosTask tareaDeEnvios = EnviarMensajeATodosLosVecinosTask.create(this, mensaje,
				nodosVecinos, processor);
		this.processor.process(tareaDeEnvios);
	}

	/**
	 * @see net.gaia.vortex.core.api.NodoPortal#setHandlerDeMensajesVecinos(net.gaia.vortex.core.api.HandlerDeMensajesVecinos)
	 */
	@Override
	public void setHandlerDeMensajesVecinos(final HandlerDeMensajesVecinos handler) {
		if (handler == null) {
			throw new IllegalArgumentException("El handler no puede ser null. Usar el NullHandler si es necesario");
		}
		handlerRef.set(handler);
	}

	public static NodoImpl create() {
		final NodoImpl nodo = new NodoImpl();
		nodo.handlerRef = new AtomicReference<HandlerDeMensajesVecinos>(NullHandler.create());
		nodo.processor = ExecutorBasedTaskProcesor.create();
		nodo.nodosVecinos = new ConcurrentLinkedQueue<Nodo>();
		return nodo;
	}
}
