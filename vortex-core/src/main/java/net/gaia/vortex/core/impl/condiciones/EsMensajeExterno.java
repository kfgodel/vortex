/**
 * 01/09/2012 11:55:32 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion que indica si un mensaje se considera externo respecto del
 * identificador de un nodo
 * 
 * @author D. García
 */
public class EsMensajeExterno implements Condicion {

	private IdDeComponenteVortex idDelNodo;
	public static final String idDelNodo_FIELD = "idDelNodo";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		final IdDeMensaje idDelMensaje = mensaje.getIdDeMensaje();
		final boolean esMensajeDelNodo = idDelMensaje.esOriginadoEn(idDelNodo);
		return !esMensajeDelNodo;
	}

	public static EsMensajeExterno create(final IdDeComponenteVortex idDelNodo) {
		final EsMensajeExterno condicion = new EsMensajeExterno();
		condicion.idDelNodo = idDelNodo;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDelNodo_FIELD, idDelNodo).toString();
	}

}
