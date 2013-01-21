/**
 * 27/06/2012 16:51:07 Copyright (C) 2011 Darío L. García
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

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición utilizada para determinar si es la primera vez que el mensaje
 * pasa por el nodo, o ya pasó otras veces.<br>
 * Se utiliza un atributo extra del mensaje para el chequeo
 * 
 * @author D. García
 */
public class NoPasoPreviamente implements Condicion {

	private ComponenteConMemoria componente;
	public static final String componente_FIELD = "componente";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final boolean elComponenteNoTieneRegistradoElId = !componente.yaRecibio(mensaje);
		return ResultadoDeCondicion.paraBooleano(elComponenteNoTieneRegistradoElId);
	}

	public static NoPasoPreviamente por(final ComponenteConMemoria componente) {
		final NoPasoPreviamente condicion = new NoPasoPreviamente();
		condicion.componente = componente;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(componente_FIELD, componente).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	@Override
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

}
