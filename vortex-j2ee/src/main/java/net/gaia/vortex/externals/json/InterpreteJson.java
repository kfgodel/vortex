/**
 * 21/08/2011 17:46:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.externals.json;

import java.util.List;

import net.gaia.vortex.conectores.http.WrapperHttp;
import net.gaia.vortex.model.messages.MensajesPendientes;
import net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje;
import net.gaia.vortex.model.messages.meta.MetamensajeSobreTags;
import net.gaia.vortex.model.messages.meta.QuitarTagsMetaMensaje;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;

/**
 * Esta interfaz define la dependencia que el servidor vortex tiene con la librería de JSON para
 * serializar y des-serializar mensajes
 * 
 * @author D. García
 */
public interface InterpreteJson {

	/**
	 * Lee una cadena en JSON y la convierte a mensaje Vortex
	 * 
	 * @param mensajeJson
	 * @return
	 */
	public abstract MensajeVortex leerMensaje(final String mensajeJson);

	/**
	 * Genera una versión textual del mensaje pasado en formato JSON
	 * 
	 * @param mensajeVortex
	 *            El mensaje a serializar
	 * @return El texto Json para representar el mensaje pasado
	 */
	public abstract String escribirMensaje(MensajeVortex mensajeVortex);

	/**
	 * Interpreta el texto pasado como respuesta de mensajes pendientes
	 * 
	 * @param respuestaJson
	 *            El json para generar el objeto
	 * @return El mensaje de pendientes recibido
	 */
	public abstract MensajesPendientes leerMensajesDe(String respuestaJson);

	/**
	 * Devuelve el wrapper interpretado de la cadena pasada
	 * 
	 * @param wrapperComoJson
	 *            El texto que representa un wrapper de mensajes
	 * @return El wrapper interpretado
	 */
	public abstract WrapperHttp leerWrapper(String wrapperComoJson);

	/**
	 * Lee una lista de mensajes de la cadena pasada
	 * 
	 * @param mensajesComoJson
	 *            La lista de mensajes expresada como array json
	 * @return La lista de mensajes recibidos para envío
	 */
	public abstract List<MensajeVortex> leerListaDeMensajesDe(String mensajesComoJson);

	/**
	 * Genera una versión JSON del wrapper pasado
	 * 
	 * @param wrapperRespuesta
	 *            El wrapper de la respuesta con los mensajes para el receptor
	 * @return La versión JSON del wrapper
	 */
	public abstract String escribirWrapper(WrapperHttp wrapperRespuesta);

	/**
	 * Interpreta el meta mensaje recibido como agregar tags
	 * 
	 * @param metamensajeString
	 *            El metamensaje recibido
	 * @return El metamensaje a recibir
	 */
	public abstract AgregarTagsMetaMensaje leerAgregarTagsDe(String metamensajeString);

	/**
	 * Interpreta el metamensaje recibido para quitar tags de un receptor
	 * 
	 * @param metamensajeString
	 *            El metamensaje como string
	 * @return El mensaje interpretado
	 */
	public abstract QuitarTagsMetaMensaje leerQuitarTagsDe(String metamensajeString);

	/**
	 * Define como json el metamensaje indicado
	 * 
	 * @param metaMensaje
	 *            El metamensaje a convertir
	 * @return La version JSON del metamensaje
	 */
	public abstract String escribirAgregarTagsMensaje(AgregarTagsMetaMensaje metaMensaje);

	public abstract String escribirMetamensajeSobre(MetamensajeSobreTags metaMensaje);

}