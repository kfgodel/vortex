/**
 * 28/11/2011 15:31:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.List;

import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa al nodo como receptor de los metamensajes
 * 
 * @author D. García
 */
public class ReceptorDeMetamensajes implements ReceptorVortex {

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#recibir(net.gaia.vortex.protocol.MensajeVortexEmbebido)
	 */
	public void recibir(final MensajeVortexEmbebido mensaje) {
		// Antes que nada debería enviar la confirmación de recepción y al de consumo.
		// Tiene sentido que se lo diga a sí mismo?
		// Una vez cumplidas las formalidades debería procesar el mensaje según el tipo
		// Como sabemos quién lo mando? Me parece que esto debería ser una tarea mas con su contexto
		// y no mezclar
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ReceptorVortex#estaInteresadoEnCualquieraDe(java.util.List)
	 */
	public boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje) {
		final boolean contieneMetamensaje = tagsDelMensaje.contains(MensajeVortexEmbebido.TAG_INTERCAMBIO_VECINO);
		return contieneMetamensaje;
	}

	public static ReceptorDeMetamensajes create() {
		final ReceptorDeMetamensajes receptor = new ReceptorDeMetamensajes();
		return receptor;
	}
}
