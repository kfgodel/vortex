/**
 * 23/01/2013 17:34:09 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.router.impl.messages.MetadataDeMensajes;
import net.gaia.vortex.sets.impl.condiciones.Negacion;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

/**
 * Esta clase representa la condición que indica si un mensaje fue recibido en otra para.<br>
 * Cuando un mensaje bidi se manda, se indica el ID de la pata donde va a ser recibida para que no
 * rebote de vuelta
 * 
 * @author D. García
 */
public class NoVinoPorEstaPata extends CondicionTipadaSupport {

	public static NoVinoPorEstaPata create(final Long idLocalDePata) {
		final NoVinoPorEstaPata condicion = new NoVinoPorEstaPata();
		condicion.initializeWith(Negacion.de(ValorEsperadoEn.elAtributo(MetadataDeMensajes.idLocalAlReceptor_FIELD,
				idLocalDePata)));
		return condicion;
	}
}
