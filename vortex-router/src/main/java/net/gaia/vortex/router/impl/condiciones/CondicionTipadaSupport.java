/**
 * 22/01/2013 19:05:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.condiciones;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase base define el comportamiento para poder subclasificar y tener condiciones con un
 * tipo, que en realidad utilizan condiciones estándar para definirse
 * 
 * @author D. García
 */
public class CondicionTipadaSupport implements Condicion {

	private Condicion condicionReal;
	public static final String condicionReal_FIELD = "condicionReal";

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		return condicionReal.esCumplidaPor(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	/**
	 * Inicializa esta instancia definiendo qué condición se utilizará para evaluar los mensajes
	 * 
	 * @param condicionReal
	 */
	protected void initializeWith(final Condicion condicionReal) {
		this.condicionReal = condicionReal;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

}
