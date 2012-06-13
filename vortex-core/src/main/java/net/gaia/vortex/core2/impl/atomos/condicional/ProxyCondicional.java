/**
 * 13/06/2012 00:43:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos.condicional;

import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.annon.Atomo;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.Condicion;
import net.gaia.vortex.core2.impl.atomos.ProxySupport;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase representa un {@link ComponenteProxy} que puede filtrar mensajes recibidos en base a
 * una {@link Condicion}
 * 
 * @author D. García
 */
@Atomo
public class ProxyCondicional extends ProxySupport {

	private Condicion condicion;

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion del proxy no puede ser null. A lo sumo una entre "
					+ CondicionTodos.class + " y " + CondicionNinguno.class);
		}
		this.condicion = condicion;
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core2.api.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		if (condicion == null) {
			throw new FaultyCodeException(
					"El proxy condicional no tiene condicion. Fue creado por reflection y falto settear la condicion?");
		}
		if (!condicion.esCumplidaPor(mensaje)) {
			// Descartamos el mensaje recibido
			return;
		}
		// Delegamos en el delegado
		super.recibirMensaje(mensaje);
	}

	public static ProxyCondicional create(final Condicion condicion) {
		final ProxyCondicional condicional = new ProxyCondicional();
		condicional.setCondicion(condicion);
		return condicional;
	}
}
