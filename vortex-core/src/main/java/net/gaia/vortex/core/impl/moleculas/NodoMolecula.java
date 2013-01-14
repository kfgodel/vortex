/**
 * 14/01/2013 11:35:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas;

import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.moleculas.FlujoVortex;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;

/**
 * Esta clase representa un nodo cuyo comportamiento se define por el flujo con el que se construyó.
 * A través del flujo se define qué componentes procesan los mensajes que este nodo recibe
 * 
 * @author D. García
 */
@Molecula
public class NodoMolecula extends NodoMoleculaSupport {

	public static NodoMolecula create(final FlujoVortex flujo) {
		final NodoMolecula molecula = new NodoMolecula();
		molecula.initializeWith(flujo);
		return molecula;
	}
}
