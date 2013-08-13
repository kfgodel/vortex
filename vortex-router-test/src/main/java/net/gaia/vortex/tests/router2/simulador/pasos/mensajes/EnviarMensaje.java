/**
 * 08/12/2012 17:40:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.mensajes;

import net.gaia.vortex.tests.router2.impl.PortalBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa el paso de simulación en el que el cliente de un portal solicita el envio
 * de un mensaje
 * 
 * @author D. García
 */
public class EnviarMensaje extends PasoSupport {

	private PortalBidireccional portal;
	public static final String portal_FIELD = "portal";

	private MensajeNormal mensaje;
	public static final String mensaje_FIELD = "mensaje";

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	public void ejecutar() {
		portal.enviar(mensaje);
	}

	public PortalBidireccional getPortal() {
		return portal;
	}

	public void setPortal(final PortalBidireccional portal) {
		this.portal = portal;
	}

	public MensajeNormal getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeNormal mensaje) {
		this.mensaje = mensaje;
	}

	public static EnviarMensaje create(final PortalBidireccional portal, final MensajeNormal mensaje) {
		final EnviarMensaje envio = new EnviarMensaje();
		envio.setMensaje(mensaje);
		envio.setNodoLocal(portal);
		envio.setPortal(portal);
		return envio;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Envio de mensaje[");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getIdDeMensaje());
		}
		builder.append("] a portal [");

		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append("] con tag[");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getTag());
		}
		builder.append("]: \"");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getTextoAdicional());
		}
		builder.append("\"");
		return builder.toString();
	}
}
