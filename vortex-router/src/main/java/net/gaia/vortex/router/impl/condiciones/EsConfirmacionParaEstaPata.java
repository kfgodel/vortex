/**
 * 23/01/2013 15:53:54 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.router.impl.messages.bidi.ConfirmacionDeIdRemoto;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion que evalúa si el mensaje corresponde ser recibido por una pata
 * 
 * @author D. García
 */
public class EsConfirmacionParaEstaPata extends CondicionTipadaSupport {

	private Long idLocalDePata;
	public static final String idLocalDePata_FIELD = "idLocalDePata";

	public static EsConfirmacionParaEstaPata create(final Long idLocalDePata) {
		final EsConfirmacionParaEstaPata condicion = new EsConfirmacionParaEstaPata();
		condicion.idLocalDePata = idLocalDePata;
		condicion.initializeWith(ValorEsperadoEn.elAtributo(ConfirmacionDeIdRemoto.idLocalAlReceptor_FIELD,
				idLocalDePata));
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocalDePata_FIELD, idLocalDePata).toString();
	}

}
