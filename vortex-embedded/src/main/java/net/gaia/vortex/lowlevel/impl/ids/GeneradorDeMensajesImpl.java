/**
 * 21/01/2012 20:36:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ids;

import java.util.List;
import java.util.Set;

import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Esta clase es la implementación del generador de mensajes de un nodo
 * 
 * @author D. García
 */
public class GeneradorDeMensajesImpl implements GeneradorMensajesDeNodo {

	private IdentificadorDeEmisor identificadorDeEmisor;
	private SecuenciadorMensajes secuenciadorMensajes;
	private TimeStamper timeStamper;
	private ContentHasher hasher;

	public static GeneradorDeMensajesImpl create() {
		final GeneradorDeMensajesImpl generador = new GeneradorDeMensajesImpl();
		generador.identificadorDeEmisor = IdentificadorDeEmisorAleatorio.create();
		generador.secuenciadorMensajes = SecuenciadorCompartible.create();
		generador.hasher = HashcodeHasher.create();
		generador.timeStamper = SystemMillisStamper.create();
		return generador;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ids.GeneradorMensajesDeNodo#generarMensajePara(java.lang.Object,
	 *      java.lang.String[])
	 */
	@Override
	public MensajeVortex generarMensajePara(final Object contenido, final String tipoDeContenido,
			final Set<String> tagsDeclarados) {
		final ContenidoVortex contenidoVortex = ContenidoVortex.create(tipoDeContenido, contenido);
		final IdVortex idVortex = generarNuevoIdPara(contenido);
		final List<String> tagsDelMensaje = Lists.newArrayList(tagsDeclarados);
		final MensajeVortex mensajeGenerado = MensajeVortex.create(contenidoVortex, idVortex, tagsDelMensaje);
		return mensajeGenerado;
	}

	/**
	 * Genera un nuevo ID para tomando el hash del contenido pasado. Este metodo incrementa el
	 * número de secuencia de mensajes
	 * 
	 * @param contenido
	 *            El contenido cuyo hash será parte del mensaje
	 * @return El Id generado para ese contenido
	 */
	private IdVortex generarNuevoIdPara(final Object contenido) {
		final String hashDelContenido = hasher.hashDe(contenido);
		final String idDelEmisor = identificadorDeEmisor.getValor();
		final Long numeroDeMensaje = secuenciadorMensajes.proximoNumeroSecuencia();
		final Long timestamp = timeStamper.currentTimestamp();
		final IdVortex idGenerado = IdVortex.create(hashDelContenido, idDelEmisor, numeroDeMensaje, timestamp);
		return idGenerado;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ids.GeneradorMensajesDeNodo#generarMetaMensajePara(java.lang.Object)
	 */
	@Override
	public MensajeVortex generarMetaMensajePara(final Object contenido) {
		final String tipoDeContenido = generarDescripcionDeTipoPara(contenido);
		final MensajeVortex metamensaje = generarMensajePara(contenido, tipoDeContenido,
				Sets.newHashSet(MensajeVortex.TAG_INTERCAMBIO_VECINO));
		return metamensaje;
	}

	/**
	 * Devuelve el texto qeu describe el tipo de contenido. Utiliza el nombre de la clase como tipo
	 * de contenido
	 * 
	 * @param contenido
	 *            El contenido a evaluar
	 * @return Una cadena que describe el tipo para todos los nodos
	 */
	private String generarDescripcionDeTipoPara(final Object contenido) {
		if (contenido == null) {
			return "null";
		}
		final Class<? extends Object> claseDelContenido = contenido.getClass();
		final String tipoDeContenido = claseDelContenido.getName();
		return tipoDeContenido;
	}

}
