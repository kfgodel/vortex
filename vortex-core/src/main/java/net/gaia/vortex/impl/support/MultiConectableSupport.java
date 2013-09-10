/**
 * Created on: Aug 31, 2013 10:52:28 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.support;

import net.gaia.vortex.api.basic.emisores.MultiConectable;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.proto.ConectorBloqueante;

/**
 * Esta clase sirve de base para todos los componentes que son {@link MultiConectable}s
 * 
 * @author dgarcia
 */
public class MultiConectableSupport extends MultiEmisorSupport implements MultiConectable {

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MultiEmisor#crearConector()
	 */
	public Conector crearConector() {
		final ConectorBloqueante conectorCreado = ConectorBloqueante.create();
		getConectores().add(conectorCreado);
		return conectorCreado;
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MultiEmisor#eliminarConector(net.gaia.vortex.api.proto.Conector)
	 */
	public void eliminarConector(final Conector conector) {
		getConectores().remove(conector);
	}

}
