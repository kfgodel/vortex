/**
 * Created on: Aug 31, 2013 4:49:47 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.nulos;

import net.gaia.vortex.api.basic.emisores.MonoEmisor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.support.EmisorSupport;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el componente emisor nulo que puede ser usado cuando se requiere un emisor
 * y no se tiene uno.<br>
 * Este componente no tiene comportamiento, por lo que no emite mensajes
 * 
 * @author dgarcia
 */
public class EmisorNulo extends EmisorSupport implements MonoEmisor {

	private static final WeakSingleton<EmisorNulo> ultimaReferencia = new WeakSingleton<EmisorNulo>(
			DefaultInstantiator.create(EmisorNulo.class));

	public static EmisorNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MonoEmisor#getConectorUnico()
	 */
	public Conector getConectorUnico() {
		return ConectorNulo.getInstancia();
	}

}
