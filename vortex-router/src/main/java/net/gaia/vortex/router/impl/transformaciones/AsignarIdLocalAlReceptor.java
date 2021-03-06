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

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.mensajes.ContenidoVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.mensajes.clones.ClonadorDeMensajes;
import net.gaia.vortex.router.impl.messages.MetadataDeMensajes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación utilizada para que el mensaje indique la pata por la que
 * llega
 * 
 * @author D. García
 */
@Paralelizable
public class AsignarIdLocalAlReceptor implements Transformacion {
	private static final Logger LOG = LoggerFactory.getLogger(AsignarIdLocalAlReceptor.class);

	private AtomicReference<Long> idLocalAlReceptor;
	public static final String idLocalAlReceptor_FIELD = "idLocalAlReceptor";

	/**
	 * @see net.gaia.vortex.api.transformaciones.Transformacion#transformar(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public MensajeVortex transformar(final MensajeVortex mensaje) {
		// Tenemos que clonar el mensaje porque no podemos modificarselo a otras patas
		final MensajeVortex copiaModificable = ClonadorDeMensajes.create(mensaje).clonar();

		final ContenidoVortex contenidoModificable = copiaModificable.getContenido();
		final Object valorAnterior = contenidoModificable.get(MetadataDeMensajes.idLocalAlReceptor_FIELD);
		final Long valorActualDelId = idLocalAlReceptor.get();
		contenidoModificable.put(MetadataDeMensajes.idLocalAlReceptor_FIELD, valorActualDelId);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug("  Actualizando id de receptor desde[{}] a [{}] en mensaje[{}] antes de ruteo a destino",
					new Object[] { valorAnterior, valorActualDelId, mensaje.toShortString() });
		}
		return copiaModificable;
	}

	public static AsignarIdLocalAlReceptor create(final AtomicReference<Long> idDePataRemota) {
		final AsignarIdLocalAlReceptor transformacion = new AsignarIdLocalAlReceptor();
		transformacion.idLocalAlReceptor = idDePataRemota;
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(idLocalAlReceptor_FIELD, idLocalAlReceptor).toString();
	}

}
