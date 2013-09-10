/**
 * 19/08/2013 20:38:36 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.support;

import net.gaia.vortex.api.basic.Emisor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.nulos.ConectorNulo;

/**
 * Esta clase sirve de base a todas las implementaciones de emisor ofreciendo métodos comunes a
 * todos los emisores
 * 
 * @author D. García
 */
public class EmisorSupport extends ComponenteSupport implements Emisor {

	/**
	 * Devuelve el conector utilizado para los mensajes descartados.<br>
	 * A través de este conector, las subclases puede descartar los mensajes que no pueden ser
	 * entregados a destino
	 * 
	 * @return El conector para los mensajes perdidos
	 */
	protected Conector getConectorParaDescartes() {
		return ConectorNulo.getInstancia();
	}
}
