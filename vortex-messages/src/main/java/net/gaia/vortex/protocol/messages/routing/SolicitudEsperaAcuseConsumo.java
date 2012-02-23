/**
 * 12/01/2012 00:01:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages.routing;

import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MetamensajeVortex;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje de solicitud de espera. Enviado por un ruteador cuando
 * recibe una {@link SolicitudAcuseConsumo} y aún no puede devolverla
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolicitudEsperaAcuseConsumo implements MetamensajeVortex {

	@NotNull
	private IdVortex idMensajeRecibido;
	public static final String idMensajeRecibido_FIELD = "idMensajeRecibido";

	public IdVortex getIdMensajeRecibido() {
		return idMensajeRecibido;
	}

	public void setIdMensajeRecibido(final IdVortex idMensajeRecibido) {
		this.idMensajeRecibido = idMensajeRecibido;
	}

	public static SolicitudEsperaAcuseConsumo create(final IdVortex idMensajeRecibido) {
		final SolicitudEsperaAcuseConsumo name = new SolicitudEsperaAcuseConsumo();
		name.idMensajeRecibido = idMensajeRecibido;
		return name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idMensajeRecibido_FIELD, idMensajeRecibido).toString();
	}
}
