/**
 * 19/01/2013 11:31:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.mensaje.copia;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;

/**
 * Esta clase representa el clonador de mensajes que permite generar duplicados de los mensajes
 * vortex que son modificables por separado
 * 
 * @author D. García
 */
public class ClonadorDeMensajes {

	private MensajeVortex mensajeOriginal;

	public static ClonadorDeMensajes create(final MensajeConContenido mensajeOriginal) {
		final ClonadorDeMensajes clonador = new ClonadorDeMensajes();
		clonador.mensajeOriginal = mensajeOriginal;
		return clonador;
	}

	/**
	 * Genera una copia del mensaje con el que se construyó esta instancia
	 * 
	 * @return Un mensaje con contenido igual al original
	 */
	public MensajeVortex clonar() {
		final ContenidoVortex contenidoOriginal = mensajeOriginal.getContenido();
		final ContenidoVortex contenidoCopiado = copiarContenido(contenidoOriginal);

		final MensajeVortex mensajeCopiado = MensajeConContenido.create(contenidoCopiado);
		mensajeCopiado.asignarId(mensajeOriginal.getIdDeMensaje());
		return mensajeCopiado;
	}

	/**
	 * Crea una copia en profundidad del contenido pasado
	 * 
	 * @param contenidoOriginal
	 *            El contenido a copiar
	 * @return El contenido copiado
	 */
	private ContenidoVortex copiarContenido(final ContenidoVortex contenidoOriginal) {
		final ContenidoMapa contenidoCopia = ContenidoMapa.create();
		CopiarMapaVortex.copiarContenidoVortexDesde(contenidoOriginal, contenidoCopia);
		return contenidoCopia;
	}

}
