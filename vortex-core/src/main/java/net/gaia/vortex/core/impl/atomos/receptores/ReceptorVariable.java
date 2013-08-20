/**
 * 17/06/2012 16:23:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.receptores;

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.support.ReceptorSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un receptor que puede ser modificado en runtime sin tener que reconectar
 * los emisores
 * 
 * @author D. García
 */
public class ReceptorVariable<R extends Receptor> extends ReceptorSupport {

	private AtomicReference<R> referenciaAlReceptorActual;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public void recibir(final MensajeVortex mensaje) {
		Loggers.ATOMOS.trace("Recibido en atomo[{}] el mensaje[{}]", this.toShortString(), mensaje);
		final Receptor receptorActual = referenciaAlReceptorActual.get();
		Loggers.ATOMOS.debug("Delegando a nodo variable[{}] el mensaje[{}] desde[{}]",
				new Object[] { receptorActual.toShortString(), mensaje, this.toShortString() });
		receptorActual.recibir(mensaje);
	}

	public R getReceptorActual() {
		return referenciaAlReceptorActual.get();
	}

	public void setReceptorActual(final R nuevoReceptor) {
		if (nuevoReceptor == null) {
			throw new IllegalArgumentException("El receptor variable no puede ser null");
		}
		referenciaAlReceptorActual.set(nuevoReceptor);
	}

	public static <R extends Receptor> ReceptorVariable<R> create(final R receptorInicial) {
		final ReceptorVariable<R> variable = new ReceptorVariable<R>();
		variable.referenciaAlReceptorActual = new AtomicReference<R>(receptorInicial);
		return variable;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con("receptorActual", getReceptorActual()).toString();
	}

}
