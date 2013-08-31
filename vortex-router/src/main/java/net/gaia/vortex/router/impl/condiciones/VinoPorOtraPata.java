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

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.router.impl.messages.MetadataDeMensajes;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import net.gaia.vortex.sets.impl.condiciones.Negacion;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que indica si un mensaje fue recibido en otra para.<br>
 * Cuando un mensaje bidi se manda, se indica el ID de la pata donde va a ser recibida para que no
 * rebote de vuelta
 * 
 * @author D. García
 */
@Paralelizable
public class VinoPorOtraPata extends CondicionTipadaSupport {
	private static final Logger LOG = LoggerFactory.getLogger(VinoPorOtraPata.class);

	private PataBidireccional pata;
	public static final String pata_FIELD = "pata";

	public static VinoPorOtraPata create(final PataBidireccional pata) {
		final VinoPorOtraPata condicion = new VinoPorOtraPata();
		condicion.pata = pata;
		condicion.initializeWith(Negacion.de(ValorEsperadoEn.elAtributo(MetadataDeMensajes.idLocalAlReceptor_FIELD,
				pata.getIdLocal())));
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.router.impl.condiciones.CondicionTipadaSupport#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final ResultadoDeCondicion resultado = super.esCumplidaPor(mensaje);
		// Chequeo por debug para evitar el costo de toShortString()
		if (ResultadoDeCondicion.FALSE.equals(resultado) && LOG.isDebugEnabled()) {
			LOG.debug("  En [{}] evitando rebote del mensaje[{}] porque proviene de ahí", pata.toShortString(),
					mensaje.toShortString());
		}
		return resultado;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(pata_FIELD, pata).toString();
	}

}
