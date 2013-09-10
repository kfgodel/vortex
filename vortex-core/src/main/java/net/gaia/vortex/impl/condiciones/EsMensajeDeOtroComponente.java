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
package net.gaia.vortex.impl.condiciones;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion que indica si un mensaje se considera externo respecto del
 * identificador de un nodo
 * 
 * @author D. García
 */
@Paralelizable
public class EsMensajeDeOtroComponente implements Condicion {

	private IdDeComponenteVortex idDelNodo;
	public static final String idDelNodo_FIELD = "idDelNodo";

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final IdDeMensaje idDelMensaje = mensaje.getIdDeMensaje();
		final boolean esMensajeExterno = !idDelMensaje.esOriginadoEn(idDelNodo);
		final ResultadoDeCondicion resultado = ResultadoDeCondicion.paraBooleano(esMensajeExterno);
		return resultado;
	}

	public static EsMensajeDeOtroComponente create(final IdDeComponenteVortex idDelNodo) {
		final EsMensajeDeOtroComponente condicion = new EsMensajeDeOtroComponente();
		condicion.idDelNodo = idDelNodo;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(idDelNodo_FIELD, idDelNodo).toString();
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

}
