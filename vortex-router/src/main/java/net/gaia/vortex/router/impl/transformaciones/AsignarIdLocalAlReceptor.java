/**
 * 23/01/2013 17:49:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.transformaciones;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.mensaje.copia.ClonadorDeMensajes;
import net.gaia.vortex.router.impl.messages.MetadataDeMensajes;

/**
 * Esta clase representa la transformación utilizada para que el mensaje indique la pata por la que
 * llega
 * 
 * @author D. García
 */
public class AsignarIdLocalAlReceptor implements Transformacion {

	private Long idLocalAlReceptor;

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensaje) {
		// Tenemos que clonar el mensaje porque no podemos modificarselo a otras patas
		final MensajeVortex copiaModificable = ClonadorDeMensajes.create(mensaje).clonar();

		final ContenidoVortex contenidoModificable = copiaModificable.getContenido();
		contenidoModificable.put(MetadataDeMensajes.idLocalAlReceptor_FIELD, idLocalAlReceptor);
		return copiaModificable;
	}

	public static AsignarIdLocalAlReceptor create(final Long idDePataRemota) {
		final AsignarIdLocalAlReceptor transformacion = new AsignarIdLocalAlReceptor();
		transformacion.idLocalAlReceptor = idDePataRemota;
		return transformacion;
	}
}
