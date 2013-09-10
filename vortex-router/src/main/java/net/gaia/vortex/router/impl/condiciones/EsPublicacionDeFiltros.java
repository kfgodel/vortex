/**
 * 22/01/2013 19:19:57 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.router.impl.messages.PublicacionDeFiltros;

/**
 * Esta clase representa la condición que evalúa si un mensaje representa la publicación de filtros
 * 
 * @author D. García
 */
@Paralelizable
public class EsPublicacionDeFiltros extends CondicionTipadaSupport {

	public static EsPublicacionDeFiltros create() {
		final EsPublicacionDeFiltros condicion = new EsPublicacionDeFiltros();
		condicion.initializeWith(PublicacionDeFiltros.getFiltroDelTipo());
		return condicion;
	}
}
