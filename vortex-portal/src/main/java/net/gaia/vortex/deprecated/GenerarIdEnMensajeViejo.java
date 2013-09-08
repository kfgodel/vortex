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
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.annotations.paralelizable.NoParalelizable;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que le asigna a un mensaje un identificador que intenta
 * ser unico. El identificador se basa en un identificador de componente como referencia
 * 
 * @author D. García
 */
@NoParalelizable
@Deprecated
public class GenerarIdEnMensajeViejo implements Transformacion {

	private GeneradorDeIdsDeMensajes generadorDeIds;
	public static final String generadorDeIds_FIELD = "generadorDeIds";

	private IdDeComponenteVortex idDeComponente;

	/**
	 * @see net.gaia.vortex.api.transformaciones.Transformacion#transformar(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public MensajeVortex transformar(final MensajeVortex mensaje) {
		final IdDeMensaje idNuevo = generadorDeIds.generarId();
		mensaje.asignarId(idNuevo);
		return mensaje;
	}

	public static GenerarIdEnMensajeViejo create(final IdDeComponenteVortex identificadorDeEmisor) {
		final GenerarIdEnMensajeViejo transformacion = new GenerarIdEnMensajeViejo();
		transformacion.generadorDeIds = GeneradorSecuencialDeIdDeMensaje.create(identificadorDeEmisor);
		transformacion.idDeComponente = identificadorDeEmisor;
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(generadorDeIds_FIELD, generadorDeIds).toString();
	}

	public IdDeComponenteVortex getIdDeComponente() {
		return idDeComponente;
	}

}
