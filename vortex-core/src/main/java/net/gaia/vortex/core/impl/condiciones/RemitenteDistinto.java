/**
 * 17/06/2012 15:45:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.condiciones;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa la condición que evalúa si el receptor es distinto del remitente y por lo
 * tanto puede recibir un mensaje
 * 
 * @author D. García
 */
public class RemitenteDistinto implements Condicion {

	private Receptor receptor;
	public static final String receptor_FIELD = "receptor";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		final Receptor remitenteDelMensaje = mensaje.getRemitenteDirecto();
		final boolean elRemitenteEsOtro = !receptor.equals(remitenteDelMensaje);
		return elRemitenteEsOtro;
	}

	public static RemitenteDistinto de(final Receptor receptor) {
		final RemitenteDistinto condicion = new RemitenteDistinto();
		condicion.receptor = receptor;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(receptor_FIELD, receptor).toString();
	}
}
