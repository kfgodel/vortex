/**
 * 19/12/2012 00:25:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.tests.listeners;

import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un paso de ruteo en el que se registra desde donde a donde se mandó un
 * mensaje
 * 
 * @author D. García
 */
public class PasoDeRuteo {

	private Emisor origen;
	public static final String origen_FIELD = "origen";
	private Receptor destino;
	public static final String destino_FIELD = "destino";
	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	public static PasoDeRuteo create(final Emisor origen, final MensajeVortex mensaje, final Receptor destino) {
		final PasoDeRuteo paso = new PasoDeRuteo();
		paso.destino = destino;
		paso.mensaje = mensaje;
		paso.origen = origen;
		return paso;
	}

	public Emisor getOrigen() {
		return origen;
	}

	public void setOrigen(final Emisor origen) {
		this.origen = origen;
	}

	public Receptor getDestino() {
		return destino;
	}

	public void setDestino(final Receptor destino) {
		this.destino = destino;
	}

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(origen_FIELD, origen).con(destino_FIELD, destino).con(mensaje_FIELD, mensaje)
				.toString();
	}

	/**
	 * Indica si este paso corresponde el emisor enviando el mensaje indicado al receptor
	 * 
	 * @param emisor
	 *            El emisor del mensaje
	 * @param receptor
	 *            El receptor
	 * @return true si corresponde a este paso
	 */
	public boolean representaA(final Emisor emisor, final Receptor receptor) {
		return this.origen.equals(emisor) && this.destino.equals(receptor);
	}
}
