/**
 * 22/08/2011 14:22:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.externals.time;

import net.gaia.vortex.prog.Decision;

import org.springframework.stereotype.Component;

import com.tenpines.commons.annotations.HasDependencyOn;

/**
 * Esta clase utiliza los millis del sistema para establecer un timestamp
 * 
 * @author D. García
 */
@Component
@HasDependencyOn(Decision.EL_NOMBRE_DE_LA_CLASE_SE_USA_EN_XML_DE_TESTING)
public class SystemMillisStamper implements TimeStamper {
	/**
	 * @see net.gaia.vortex.externals.time.TimeStamper#currentTimestamp()
	 */
	@Override
	public Long currentTimestamp() {
		return System.currentTimeMillis();
	}

}
