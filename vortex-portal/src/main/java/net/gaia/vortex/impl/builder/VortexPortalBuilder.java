/**
 * Created on: Sep 5, 2013 10:04:21 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.builder;

import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.impl.atomos.Desobjetivizador;
import net.gaia.vortex.impl.atomos.Objetivizador;
import net.gaia.vortex.impl.moleculas.PortalConversor;
import net.gaia.vortex.portal.api.mensaje.HandlerDeObjetos;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.conversion.impl.ConversorDefaultDeMensajes;

/**
 * Esta clase representa el builder del portal
 * 
 * @author dgarcia
 */
public class VortexPortalBuilder implements VortexPortal {

	private VortexCore core;
	private ConversorDeMensajesVortex conversor;

	/**
	 * @see net.gaia.vortex.api.builder.VortexPortal#getCore()
	 */
	public VortexCore getCore() {
		return core;
	}

	public static VortexPortalBuilder create(final VortexCore core) {
		final VortexPortalBuilder builder = new VortexPortalBuilder();
		builder.core = core;
		builder.conversor = ConversorDefaultDeMensajes.create();
		return builder;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexPortal#portalConversor()
	 */
	public Portal portalConversor() {
		PortalConversor portalCreado = PortalConversor.create(this);
		return portalCreado;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexPortal#getConversorDeMensajes()
	 */
	public ConversorDeMensajesVortex getConversorDeMensajes() {
		return conversor;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexPortal#conversorDesdeObjetos()
	 */
	public Desobjetivizador conversorDesdeObjetos() {
		final Desobjetivizador conversorDesdeObjetos = Desobjetivizador.create(getConversorDeMensajes());
		return conversorDesdeObjetos;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexPortal#conversorHaciaObjetos(java.lang.Class,
	 *      net.gaia.vortex.portal.api.mensaje.HandlerDeObjetos)
	 */
	public <T> Objetivizador conversorHaciaObjetos(final Class<? extends T> tipoEsperadoComoMensajes,
			final HandlerDeObjetos<? super T> handlerDeObjetos) {
		final Objetivizador conversor = Objetivizador.create(getConversorDeMensajes(), tipoEsperadoComoMensajes,
				handlerDeObjetos);
		return conversor;
	}
}
