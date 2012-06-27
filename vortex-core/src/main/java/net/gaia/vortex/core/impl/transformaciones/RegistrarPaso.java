/**
 * 27/06/2012 17:06:54 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación realizada en un nodo para registrar en el mensaje el
 * pasaje.<br>
 * Esta marca evita recibir mensajes ya vistos
 * 
 * @author D. García
 */
public class RegistrarPaso implements Transformacion {

	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensaje) {
		mensaje.registrarPasajePor(identificador);
		return mensaje;
	}

	public static RegistrarPaso por(final IdentificadorVortex identificador) {
		final RegistrarPaso transformacion = new RegistrarPaso();
		transformacion.identificador = identificador;
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(identificador_FIELD, identificador).toString();
	}

}
