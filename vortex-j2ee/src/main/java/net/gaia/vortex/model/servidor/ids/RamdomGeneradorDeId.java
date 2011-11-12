/**
 * 22/08/2011 14:24:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.servidor.ids;

import java.util.Random;

import net.gaia.vortex.prog.Decision;

import org.springframework.stereotype.Component;

import com.tenpines.commons.annotations.HasDependencyOn;

/**
 * Esta clase genera un ID random utilizando el hashcode de un Long obtenido aleatoreamente
 * 
 * @author D. García
 */
@Component
@HasDependencyOn(Decision.EL_NOMBRE_DE_LA_CLASE_SE_USA_EN_XML_DE_TESTING)
public class RamdomGeneradorDeId implements GeneradorIdEmisor {
	/**
	 * @see net.gaia.vortex.model.servidor.ids.GeneradorIdEmisor#getIdEmisor()
	 */
	@Override
	public String getIdEmisor() {
		final Random random = new Random();
		final long nextLong = random.nextLong();
		final String idEmisor = Long.toHexString(nextLong);
		return idEmisor;
	}

}
