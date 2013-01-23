/**
 * 23/01/2013 16:14:03 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.router.impl.messages.bidi.ConfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.messages.bidi.ReconfirmacionDeIdRemoto;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la transformación que registra el id remoto recibido en una confirmacion y
 * envia una reconfirmación
 * 
 * @author D. García
 */
public class RegistrarIdRemotoYEnviarReconfirmacion implements Transformacion {
	private AtomicLong idDePataRemota;
	public static final String idDePataRemota_FIELD = "idDePataRemota";

	private Long idLocalDePata;
	public static final String idLocalDePata_FIELD = "idLocalDePata";

	private ConversorDeMensajesVortex mapeador;

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensajeDeRespuesta) {
		final ConfirmacionDeIdRemoto confirmacion = mapeador.convertirDesdeVortex(mensajeDeRespuesta,
				ConfirmacionDeIdRemoto.class);
		final Long idRemoto = confirmacion.getIdLocalAlEmisor();
		idDePataRemota.set(idRemoto);

		final ReconfirmacionDeIdRemoto reconfirmacion = ReconfirmacionDeIdRemoto.create(idRemoto, idLocalDePata);
		final MensajeVortex mensajeEnviable = mapeador.convertirAVortex(reconfirmacion);
		return mensajeEnviable;
	}

	public static RegistrarIdRemotoYEnviarReconfirmacion create(final Long idLocal, final AtomicLong idRemoto,
			final ConversorDeMensajesVortex mapeador) {
		final RegistrarIdRemotoYEnviarReconfirmacion registrar = new RegistrarIdRemotoYEnviarReconfirmacion();
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
