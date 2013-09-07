/**
 * Created on: Sep 6, 2013 7:27:37 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.transformaciones;

import net.gaia.vortex.api.annotations.paralelizable.NoParalelizable;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que le asigna a un mensaje un identificador que intenta
 * ser unico. El identificador se basa en un identificador de componente como referencia
 * 
 * @author dgarcia
 */
@NoParalelizable
public class GenerarIdEnMensaje implements Transformacion {

	private GeneradorDeIdsDeMensajes generadorDeIds;
	public static final String generadorDeIds_FIELD = "generadorDeIds";

	/**
	 * @see net.gaia.vortex.api.transformaciones.Transformacion#transformar(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public MensajeVortex transformar(final MensajeVortex mensaje) {
		final IdDeMensaje idNuevo = generadorDeIds.generarId();
		mensaje.asignarId(idNuevo);
		return mensaje;
	}

	public static GenerarIdEnMensaje create(final GeneradorDeIdsDeMensajes generadorDeIds) {
		final GenerarIdEnMensaje transformacion = new GenerarIdEnMensaje();
		transformacion.generadorDeIds = generadorDeIds;
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
