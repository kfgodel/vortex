/**
 * 31/08/2012 23:07:30 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que evalúa si un mensaje es previamente conocido. Para lo cual
 * registra los IDs de los nuevos mensajes no conocidos hasta cierto límite.<br>
 * Si se supera el límite se van descartando los IDs viejos;
 * 
 * @author D. García
 */
public class EsMensajeNuevo implements Condicion {

	private MemoriaDeMensajes mensajesConocidos;
	public static final String mensajesConocidos_FIELD = "mensajesConocidos";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final boolean agregadoComoNuevo = mensajesConocidos.registrarNuevo(mensaje);
		return ResultadoDeCondicion.paraBooleano(agregadoComoNuevo);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(mensajesConocidos_FIELD, mensajesConocidos).toString();
	}

	public static EsMensajeNuevo create(final MemoriaDeMensajes memoria) {
		final EsMensajeNuevo condicion = new EsMensajeNuevo();
		condicion.mensajesConocidos = memoria;
		return condicion;
	}
}
