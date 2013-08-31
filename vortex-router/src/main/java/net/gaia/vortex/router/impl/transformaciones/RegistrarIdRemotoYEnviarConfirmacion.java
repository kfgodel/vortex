/**
 * 23/01/2013 15:40:14 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.router.impl.messages.bidi.ConfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.messages.bidi.RespuestaDeIdRemoto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que registra el ID remoto recibido en la respuesta y
 * genera una confirmacion
 * 
 * @author D. García
 */
@Paralelizable
public class RegistrarIdRemotoYEnviarConfirmacion implements Transformacion {
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarIdRemotoYEnviarConfirmacion.class);

	private AtomicReference<Long> idDePataRemota;
	public static final String idDePataRemota_FIELD = "idDePataRemota";

	private Long idLocalDePata;
	public static final String idLocalDePata_FIELD = "idLocalDePata";

	private ConversorDeMensajesVortex mapeador;

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public MensajeVortex transformar(final MensajeVortex mensajeDeRespuesta) {
		final RespuestaDeIdRemoto respuesta = mapeador.convertirDesdeVortex(mensajeDeRespuesta,
				RespuestaDeIdRemoto.class);
		final Long idRemoto = respuesta.getIdLocalAlEmisor();
		idDePataRemota.set(idRemoto);

		final ConfirmacionDeIdRemoto confirmacion = ConfirmacionDeIdRemoto.create(idRemoto, idLocalDePata);
		final MensajeVortex mensajeEnviable = mapeador.convertirAVortex(confirmacion);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug("Enviando confirmacion[{}] para la respuesta[{}] recibido", confirmacion,
					mensajeDeRespuesta.toShortString());
		}
		return mensajeEnviable;
	}

	public static RegistrarIdRemotoYEnviarConfirmacion create(final Long idLocal, final AtomicReference<Long> idRemoto,
			final ConversorDeMensajesVortex mapeador) {
		final RegistrarIdRemotoYEnviarConfirmacion registrar = new RegistrarIdRemotoYEnviarConfirmacion();
		registrar.idDePataRemota = idRemoto;
		registrar.idLocalDePata = idLocal;
		registrar.mapeador = mapeador;
		return registrar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(idLocalDePata_FIELD, idLocalDePata).con(idDePataRemota_FIELD, idDePataRemota)
				.toString();
	}

}
