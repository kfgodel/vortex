/**
 * 21/08/2011 19:06:31 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests;

import java.util.List;

import net.gaia.vortex.conectores.http.WrapperHttp;
import net.gaia.vortex.externals.json.InterpreteJson;
import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje;
import net.gaia.vortex.model.messages.protocolo.ContenidoVortex;
import net.gaia.vortex.model.messages.protocolo.IdVortex;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.receptores.ReceptorServidor;
import net.gaia.vortex.persistibles.ContenidoVortexPersistible;
import net.gaia.vortex.persistibles.IdVortexPersistible;
import net.gaia.vortex.persistibles.MensajeVortexPersistible;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * Esta calse ofrece alguno métodos helper para utilizar en los tests con casos base
 * 
 * @author D. García
 */
@Component
public class EscenarioDeTest {

	@Autowired
	private InterpreteJson interpreteJson;

	/**
	 * Devuelve un mensaje de publicacion de tags tipo para los tags indicados
	 * 
	 * @return
	 */
	public String crearJsonDeWrapperConMensajeDePublicacionDeTags(final String... tags) {
		final AgregarTagsMetaMensaje metamensaje = AgregarTagsMetaMensaje.create(tags);
		final String metamensajeJson = interpreteJson.escribirAgregarTagsMensaje(metamensaje);

		final String mensajeJSon = crearJsonDeWrapperConMensajeDeTest(null, metamensajeJson,
				ReceptorServidor.DESTINO_PROXIMO_TAG, TipoMetaMensaje.AGREGAR_TAGS.getTipoContenidoAsociado());
		return mensajeJSon;
	}

	/**
	 * Devuelve el json de un mensaje para enviar como mensaje de prueba donde se
	 * 
	 * @param idAsignado
	 * 
	 * @param contenido
	 *            Contenido del mensaje
	 * @param tags
	 *            tags sobre el mensaje vortex
	 * @param tipoDeContenido
	 * @return El texto json con el mensaje vortex
	 */
	public String crearJsonDeWrapperConMensajeDeTest(final Long idAsignado, final String contenido, final String tags,
			final String tipoDeContenido) {
		final MensajeVortex mensajeVortex = new MensajeVortex();
		final ContenidoVortex contenidoVortex = ContenidoVortex.create(tipoDeContenido, contenido);
		mensajeVortex.setContenido(contenidoVortex);
		mensajeVortex.setTagsDestino(Lists.newArrayList(tags));
		final IdVortex idVortex = IdVortex.create("hash", "idEmisor", 1L, 1L);
		mensajeVortex.setIdentificacion(idVortex);

		final List<MensajeVortex> mensajesEnviados = Lists.newArrayList(mensajeVortex);
		final WrapperHttp wrapper = WrapperHttp.create(idAsignado, mensajesEnviados);
		final String jsonDelWrapper = interpreteJson.escribirWrapper(wrapper);
		return jsonDelWrapper;
	}

	/**
	 * Genera un mensaje de desconexión para los tests
	 * 
	 * @param idAsignado
	 * 
	 * @return
	 */
	String crearJsonDeMensajeDeDesconexion(final Long idAsignado) {
		final String mensajeJSon = crearJsonDeWrapperConMensajeDeTest(idAsignado, null,
				ReceptorServidor.DESTINO_PROXIMO_TAG, TipoMetaMensaje.CERRAR_CONEXION.getTipoContenidoAsociado());
		return mensajeJSon;
	}

	/**
	 * Genera un JSOn del wrapper cuando sólo se piden novedades
	 * 
	 * @param idAsignadoAlTagPublicado
	 *            El id para solicitar nuevos mensajes
	 * @return El wraper en su versión json
	 */
	public String crearJsonDeWrapperConPedidoDeNovedades(final Long idAsignadoAlTagPublicado) {
		final WrapperHttp wrapper = WrapperHttp.create(idAsignadoAlTagPublicado, null);
		final String jsonDelWrapper = interpreteJson.escribirWrapper(wrapper);
		return jsonDelWrapper;
	}

	/**
	 * Crea el mensaje persistible para pruebas
	 * 
	 * @return
	 */
	public MensajeVortexPersistible crearMensajePersistible() {
		final MensajeVortexPersistible mensajeCreado = new MensajeVortexPersistible();
		final ContenidoVortexPersistible contenidoVortex = new ContenidoVortexPersistible();
		contenidoVortex.setTipoContenido("Test");
		contenidoVortex.setValor("contenidoPrueba");
		mensajeCreado.setContenido(contenidoVortex);

		mensajeCreado.setCreationMoment(new DateTime());
		mensajeCreado.setTagsDestino(Lists.newArrayList("expresionPrueba"));
		final IdVortexPersistible identificacion = new IdVortexPersistible();
		identificacion.setHashDelContenido("hash");
		identificacion.setIdDelEmisor("idEmisor");
		identificacion.setNumeroDeSecuencia(1L);
		identificacion.setTimestamp(1L);
		mensajeCreado.setIdentificacion(identificacion);
		return mensajeCreado;
	}
}
