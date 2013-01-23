/**
 * 23/01/2013 16:06:41 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.impl.condiciones.support.CondicionTipadaSupport;
import net.gaia.vortex.router.impl.messages.bidi.ReconfirmacionDeIdRemoto;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

/**
 * Esta clase representa la condición que evaluá si un mensaje es una reconfirmacion para una pata
 * 
 * @author D. García
 */
public class EsReconfirmacionParaEstaPata extends CondicionTipadaSupport {

	public static EsReconfirmacionParaEstaPata create(final Long idLocalDePata) {
		final EsReconfirmacionParaEstaPata condicion = new EsReconfirmacionParaEstaPata();
		condicion.initializeWith(ValorEsperadoEn.elAtributo(ReconfirmacionDeIdRemoto.idLocalAlReceptor_FIELD,
				idLocalDePata));
		return condicion;
	}
}
