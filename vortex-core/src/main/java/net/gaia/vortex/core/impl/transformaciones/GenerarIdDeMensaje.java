/**
 * 01/09/2012 12:07:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.transformaciones;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.ids.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.core.impl.ids.IdsSecuencialesParaMensajes;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que a un mensaje le asigna el identificador de mensaje.
 * El identificador se basa en un nodo de referencia
 * 
 * @author D. García
 */
public class GenerarIdDeMensaje implements Transformacion {

	private GeneradorDeIdsDeMensajes generadorDeIds;
	public static final String generadorDeIds_FIELD = "generadorDeIds";

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensaje) {
		final IdDeMensaje idNuevo = generadorDeIds.generarId();
		mensaje.asignarId(idNuevo);
		return mensaje;
	}

	public static GenerarIdDeMensaje create(final IdDeComponenteVortex identificadorDeEmisor) {
		final GeneradorDeIdsDeMensajes generador = IdsSecuencialesParaMensajes.create(identificadorDeEmisor);
		return create(generador);
	}

	public static GenerarIdDeMensaje create(final GeneradorDeIdsDeMensajes generador) {
		final GenerarIdDeMensaje transformacion = new GenerarIdDeMensaje();
		transformacion.generadorDeIds = generador;
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(generadorDeIds_FIELD, generadorDeIds).toString();
	}

}