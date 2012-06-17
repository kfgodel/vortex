/**
 * 17/06/2012 16:04:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.transformaciones;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.transformaciones.Transformacion;

import com.google.common.base.Objects;

/**
 * Esta clase representa la transformación que asigna el receptor indicado como remitente del
 * mensaje
 * 
 * @author D. García
 */
public class AsignarComoRemitente implements Transformacion {

	private Receptor remitente;
	public static final String remitente_FIELD = "remitente";

	/**
	 * @see net.gaia.vortex.core3.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensaje) {
		mensaje.setRemitenteDirecto(remitente);
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(remitente_FIELD, remitente).toString();
	}

	public static AsignarComoRemitente a(final Receptor remitente) {
		final AsignarComoRemitente asignacion = new AsignarComoRemitente();
		asignacion.remitente = remitente;
		return asignacion;
	}

}
