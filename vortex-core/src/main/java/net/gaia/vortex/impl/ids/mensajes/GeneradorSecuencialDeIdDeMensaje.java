/**
 * 01/09/2012 12:10:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.ids.mensajes;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación del generador de IDS de mensajes que utiliza el identificador del
 * nodo como base, y un número de secuencia asociado
 * 
 * @author D. García
 */
public class GeneradorSecuencialDeIdDeMensaje implements GeneradorDeIdsDeMensajes {

	private static final int SECUENCIA_INICIAL = 0;

	private IdDeComponenteVortex identificadorBase;
	public static final String identificadorBase_FIELD = "identificadorBase";

	private AtomicLong proximaSecuencia;
	public static final String proximaSecuencia_FIELD = "proximaSecuencia";

	/**
	 * @see net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes#generarId()
	 */
	
	public IdDeMensaje generarId() {
		final Long nuevaSecuencia = proximaSecuencia.getAndIncrement();
		final IdInmutableDeMensaje nuevoId = IdInmutableDeMensaje.create(identificadorBase, nuevaSecuencia);
		return nuevoId;
	}

	public static GeneradorSecuencialDeIdDeMensaje create(final IdDeComponenteVortex identificadorBase) {
		final GeneradorSecuencialDeIdDeMensaje generador = new GeneradorSecuencialDeIdDeMensaje();
		generador.identificadorBase = identificadorBase;
		generador.proximaSecuencia = new AtomicLong(SECUENCIA_INICIAL);
		return generador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(identificadorBase_FIELD, identificadorBase)
				.con(proximaSecuencia_FIELD, proximaSecuencia).toString();
	}

}
