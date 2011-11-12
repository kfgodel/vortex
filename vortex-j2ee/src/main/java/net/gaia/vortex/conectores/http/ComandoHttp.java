/**
 * 20/08/2011 16:07:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.conectores.http;

import java.util.List;

import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;

/**
 * Esta clase representa un comando interpretado de un request que captura las intenciones de un
 * cliente conectado a un servidor
 * 
 * @author D. García
 */
public class ComandoHttp {

	private ReferenciaAReceptor referenciaReceptor;
	private List<MensajeVortex> mensajesParaEnviar;

	public List<MensajeVortex> getMensajesParaEnviar() {
		return mensajesParaEnviar;
	}

	public void setMensajesParaEnviar(final List<MensajeVortex> mensajesParaEnviar) {
		this.mensajesParaEnviar = mensajesParaEnviar;
	}

	/**
	 * Indica si este comando posee un mensajesParaEnviar para ser entregado
	 * 
	 * @return true si este comando posee un mensajesParaEnviar para ser entregado
	 */
	public boolean solicitaEnvioDeMensajes() {
		final boolean requiereEnvio = mensajesParaEnviar != null && mensajesParaEnviar.size() > 0;
		return requiereEnvio;
	}

	/**
	 * @return
	 */
	public ReferenciaAReceptor getReferenciaReceptor() {
		return referenciaReceptor;
	}

	public static ComandoHttp create(final ReferenciaAReceptor localizador, final List<MensajeVortex> mensajesAEnviar) {
		final ComandoHttp comando = new ComandoHttp();
		comando.referenciaReceptor = localizador;
		comando.mensajesParaEnviar = mensajesAEnviar;
		return comando;
	}

	/**
	 * Indica si este comando requiere recepción de mensajes pendientes.<br>
	 * Si requiere mensajes, se debe enviar en la respuesta los mensajes pendientes del receptor
	 * 
	 * @return si el receptor del request es identificable en la base (tiene pendientes)
	 */
	public boolean solicitaRecepcionDeMensajes() {
		final boolean esIdentificableEnLaBase = this.referenciaReceptor.esIdentificableEnLaBase();
		return esIdentificableEnLaBase;
	}
}
