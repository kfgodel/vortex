/**
 * 22/08/2011 14:25:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ids;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Esta clase mantiene una secuencia en memoria con un valor incrementado cada vez que se le
 * solicita
 * 
 * @author D. García
 */
public class SecuenciadorCompartible implements SecuenciadorMensajes {

	private final AtomicLong secuencia = new AtomicLong(0);

	/**
	 * @see net.gaia.vortex.model.servidor.ids.SecuenciadorMensajes#proximoNumeroSecuencia()
	 */
	@Override
	public Long proximoNumeroSecuencia() {
		final long proximo = secuencia.getAndIncrement();
		return proximo;
	}

	public static SecuenciadorCompartible create() {
		final SecuenciadorCompartible secuenciador = new SecuenciadorCompartible();
		return secuenciador;
	}
}
