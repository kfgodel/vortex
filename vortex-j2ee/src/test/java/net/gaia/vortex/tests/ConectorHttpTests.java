/**
 * 21/08/2011 18:05:48 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.conectores.http.ConectorHttp;
import net.gaia.vortex.conectores.http.WrapperHttp;
import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.externals.json.InterpreteJson;
import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje;
import net.gaia.vortex.model.messages.protocolo.ContenidoVortex;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

/**
 * Esta clase tiene algunos tests de la respuesta del servidor vortex a pedidos http
 * 
 * @author D. García
 */
@ContextConfiguration(locations = { "classpath:/spring/test-beans-context.xml" }, inheritLocations = true)
public class ConectorHttpTests extends VortexTest {

	@Autowired
	private ConectorHttp conectorHttp;

	@Autowired
	private EscenarioDeTest escenarioDeTest;

	@Autowired
	private InterpreteJson interpreteJson;

	@Test
	public void deberiaResponderMensajesVaciosNoTieneIdNiMensaje() {
		// Setup del test
		final OperacionHttp pedidoMockeado = Mockito.mock(OperacionHttp.class);
		Mockito.when(pedidoMockeado.getWrapperJson()).thenReturn(null);

		conectorHttp.procesar(pedidoMockeado);

		// Se espera
		Mockito.verify(pedidoMockeado, Mockito.never()).responder(Mockito.anyString());
	}

	@Test
	public void deberiaAsignarnosIdAlpublicarTags() {
		// Setup del test
		final AtomicReference<String> respuestaAPublicacion = new AtomicReference<String>();
		final OperacionHttp pedidoMockeado = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaAPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConMensajeDePublicacionDeTags("tagPrueba");
			}
		};

		conectorHttp.procesar(pedidoMockeado);

		// Verificamos que nos haya asignado un ID
		final WrapperHttp wrapperRespuesta = obtenerWrapperDe(respuestaAPublicacion);
		final Long nuevoId = wrapperRespuesta.getId();
		Assert.notNull(nuevoId);
	}

	/**
	 * Obtiene el wrapper encodeado en la respuesta pasada
	 * 
	 * @param respuestaAPublicacion
	 *            La respuesta del servidor
	 * @return El wrapper interpretado
	 */
	private WrapperHttp obtenerWrapperDe(final AtomicReference<String> respuestaAPublicacion) {
		final String respuestaJson = respuestaAPublicacion.get();
		Assert.notNull(respuestaJson, "Debería existir respuesta del pedido");
		final WrapperHttp wrapperRespuesta = interpreteJson.leerWrapper(respuestaJson);
		Assert.notNull(wrapperRespuesta, "Debería tener un wrapper");
		return wrapperRespuesta;
	}

	@Test
	public void deberiaResponderConLosTagsPropiosDelServerAlPublicarTagsElCliente() {
		// Setup del test
		final AtomicReference<String> respuestaAPublicacion = new AtomicReference<String>();
		final OperacionHttp pedidoMockeado = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaAPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConMensajeDePublicacionDeTags("tagPrueba");
			}

		};

		conectorHttp.procesar(pedidoMockeado);

		// Verificamos que nos haya asignado un ID
		final MensajeVortex primerMensaje = obtenerPrimerMensajeDe(respuestaAPublicacion);
		final AgregarTagsMetaMensaje metaMensaje = obtenerMetaAgregarTagsDe(primerMensaje);

		final List<String> tagsDelServidor = metaMensaje.getTags();
		Assert.isTrue(tagsDelServidor.size() == 0,
				"No debería recibir tag del server por que no hay nadie mas conectado");
	}

	/**
	 * Helper para obtener el mensaje respondido
	 * 
	 * @param respuestaAPublicacion
	 *            Respues del servidor
	 * @return El primer mensaje contenido en la respuesta
	 */
	private MensajeVortex obtenerPrimerMensajeDe(final AtomicReference<String> respuestaAPublicacion) {
		final WrapperHttp wrapperRespuesta = obtenerWrapperDe(respuestaAPublicacion);
		return obtenerPrimerMensajeDe(wrapperRespuesta);
	}

	/**
	 * Devuelve el primer mensaje recibido en el wrapper indicado
	 * 
	 * @param wrapperRespuesta
	 *            El wrapper con las respuestas
	 * @return El primer mensaje recibido
	 */
	private MensajeVortex obtenerPrimerMensajeDe(final WrapperHttp wrapperRespuesta) {
		final List<MensajeVortex> mensajesRecibidos = wrapperRespuesta.getMensajes();
		Assert.notNull(mensajesRecibidos);
		Assert.isTrue(!mensajesRecibidos.isEmpty(), "Debería tener al menos un mensaje recibido");
		final MensajeVortex primerMensaje = mensajesRecibidos.get(0);
		return primerMensaje;
	}

	@Test
	public void deberiaRecibirMensajeEnviadoConMisTagsDespuesDePublicar() {
		final String tagPublicado = "MiTag";

		// Primero le mandamos una publicacion de un tag
		final AtomicReference<String> respuestaPublicacion = new AtomicReference<String>();
		final OperacionHttp publicacionDeTags = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				final String mensajePublicacionMiTag = escenarioDeTest
						.crearJsonDeWrapperConMensajeDePublicacionDeTags(tagPublicado);
				return mensajePublicacionMiTag;
			}

		};
		conectorHttp.procesar(publicacionDeTags);

		final WrapperHttp wrapperDePublicacion = obtenerWrapperDe(respuestaPublicacion);
		final Long idAsignadoAlTagPublicado = wrapperDePublicacion.getId();

		// Enviamos un mensaje sin id que matchea el tag para ver si llega
		final String contenidoMensajeEnviado = "contenido";
		final AtomicReference<String> respuestaEnvioMensaje = new AtomicReference<String>();
		final OperacionHttp envioMensaje = new OperacionHttp() {

			@Override
			public void responder(final String respuestaJson) {
				respuestaEnvioMensaje.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConMensajeDeTest(null, contenidoMensajeEnviado, tagPublicado,
						"test");
			}
		};
		conectorHttp.procesar(envioMensaje);

		// Pedimos novedades con el id para ver si recibimos el mensaje anterior
		final AtomicReference<String> respuestaDeNovedades = new AtomicReference<String>();
		final OperacionHttp pedidoDeNovedades = new OperacionHttp() {

			@Override
			public void responder(final String respuestaJson) {
				respuestaDeNovedades.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConPedidoDeNovedades(idAsignadoAlTagPublicado);
			}

		};
		conectorHttp.procesar(pedidoDeNovedades);

		// Deberíamos recibir el mensaje publicado
		final MensajeVortex mensajeRecibido = obtenerPrimerMensajeDe(respuestaDeNovedades);
		Assert.notNull(mensajeRecibido);

		final ContenidoVortex contenido = mensajeRecibido.getContenido();
		final String contenidoRecibido = contenido.getValor();
		Assert.isTrue(contenidoMensajeEnviado.equals(contenidoRecibido),
				"Deberíamos recibir el contenido del mensaje enviado con el tag");
	}

	@Test
	public void deberiaEliminarElReceptorSiCierraExplicitamente() {
		// Primero publicamos tags para que nos cree como receptor
		final AtomicReference<String> respuestaAPublicacion = new AtomicReference<String>();
		final OperacionHttp pedidoMockeado = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaAPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConMensajeDePublicacionDeTags("tagPrueba");
			}
		};

		conectorHttp.procesar(pedidoMockeado);

		// Obtenemos el id que nos asigno a la publicacion de tags
		final WrapperHttp wrapperRespuestaPublicacion = obtenerWrapperDe(respuestaAPublicacion);
		final Long idAsignado = wrapperRespuestaPublicacion.getId();
		Assert.notNull(idAsignado, "Deberíamos tener un ID para poder cerrar la conexion");

		final AtomicReference<String> respuestaDesconexion = new AtomicReference<String>();
		final OperacionHttp cierreConexion = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaDesconexion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeMensajeDeDesconexion(idAsignado);
			}
		};
		conectorHttp.procesar(cierreConexion);

		Assert.isNull(respuestaDesconexion.get(), "No deberíamos tener ningun tipo de mensaje de respuesta");
	}

	@Test
	public void noDeberiaRecibirUnMensajeDuplicado() {
		final String tagPublicado = "MiTag";

		// Primero le mandamos una publicacion de un tag
		final AtomicReference<String> respuestaPublicacion = new AtomicReference<String>();
		final OperacionHttp publicacionDeTags = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				final String mensajePublicacionMiTag = escenarioDeTest
						.crearJsonDeWrapperConMensajeDePublicacionDeTags(tagPublicado);
				return mensajePublicacionMiTag;
			}

		};
		conectorHttp.procesar(publicacionDeTags);

		final WrapperHttp wrapperDePublicacion = obtenerWrapperDe(respuestaPublicacion);
		final Long idAsignadoAlTagPublicado = wrapperDePublicacion.getId();

		// Enviamos un mensaje dos veces. Deberia descartar el segundo
		final String contenidoMensajeEnviado = "contenido";
		final AtomicReference<String> respuestaEnvioMensaje = new AtomicReference<String>();
		final OperacionHttp envioMensaje = new OperacionHttp() {

			@Override
			public void responder(final String respuestaJson) {
				respuestaEnvioMensaje.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConMensajeDeTest(null, contenidoMensajeEnviado, tagPublicado,
						"test");
			}
		};
		conectorHttp.procesar(envioMensaje);
		conectorHttp.procesar(envioMensaje);

		// Pedimos novedades con el id para ver si recibimos el mensaje anterior
		final AtomicReference<String> respuestaDeNovedades = new AtomicReference<String>();
		final OperacionHttp pedidoDeNovedades = new OperacionHttp() {

			@Override
			public void responder(final String respuestaJson) {
				respuestaDeNovedades.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConPedidoDeNovedades(idAsignadoAlTagPublicado);
			}

		};
		conectorHttp.procesar(pedidoDeNovedades);

		// Deberíamos recibir el mensaje publicado
		final WrapperHttp wrapperRespuesta = obtenerWrapperDe(respuestaDeNovedades);
		final List<MensajeVortex> mensajesRecibidos = wrapperRespuesta.getMensajes();

		// Solo deberiamos recibirlo una vez
		Assert.isTrue(mensajesRecibidos.size() == 1);
	}

	@Test
	public void deberiaRecibirLaPublicacionDeTagsDeOtroConector() {
		final String primerTagPublicado = "PrimerTagPublicado";

		// Primero le mandamos una publicacion de un tag
		final AtomicReference<String> respuestaPublicacion = new AtomicReference<String>();
		final OperacionHttp publicacionDeTags = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				final String mensajePublicacionMiTag = escenarioDeTest
						.crearJsonDeWrapperConMensajeDePublicacionDeTags(primerTagPublicado);
				return mensajePublicacionMiTag;
			}

		};
		conectorHttp.procesar(publicacionDeTags);

		// Verificamos que inicialmente no hay nadie conectado
		final WrapperHttp primerWrapper = obtenerWrapperDe(respuestaPublicacion);
		final MensajeVortex primerMensaje = obtenerPrimerMensajeDe(primerWrapper);
		final AgregarTagsMetaMensaje metaMensaje = obtenerMetaAgregarTagsDe(primerMensaje);
		final List<String> tagsDelServidor = metaMensaje.getTags();
		Assert.isTrue(tagsDelServidor.size() == 0, "No debería recibir tags del server el primero");

		final String segundoTagPublicado = "SegundoTagPublicado";

		// Publicamos con un segundo receptor
		final AtomicReference<String> respuestaSegundaPublicacion = new AtomicReference<String>();
		final OperacionHttp segundaPublicacionDeTags = new OperacionHttp() {
			@Override
			public void responder(final String respuestaJson) {
				respuestaSegundaPublicacion.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				final String mensajePublicacionMiTag = escenarioDeTest
						.crearJsonDeWrapperConMensajeDePublicacionDeTags(segundoTagPublicado);
				return mensajePublicacionMiTag;
			}

		};
		conectorHttp.procesar(segundaPublicacionDeTags);

		// Verificamos que al segundo conectado le dice lo del primero nada más
		final WrapperHttp segundoWrapper = obtenerWrapperDe(respuestaSegundaPublicacion);
		final MensajeVortex segundoMensaje = obtenerPrimerMensajeDe(segundoWrapper);
		final AgregarTagsMetaMensaje segundoMetaMensaje = obtenerMetaAgregarTagsDe(segundoMensaje);
		final List<String> tagsDelPrimero = segundoMetaMensaje.getTags();
		Assert.isTrue(tagsDelPrimero.size() == 1, "Debería recibir sólo los tags del primero");
		Assert.isTrue(tagsDelPrimero.contains(primerTagPublicado), "El tag del primero debería llegar al segundo");

		// Verificamos que al segundo le llegue el mensaje de notificacion
		final Long idPrimerReceptor = primerWrapper.getId();

		// Pedimos novedades con el id para ver si recibimos el mensaje anterior
		final AtomicReference<String> respuestaDeNovedades = new AtomicReference<String>();
		final OperacionHttp pedidoDeNovedades = new OperacionHttp() {

			@Override
			public void responder(final String respuestaJson) {
				respuestaDeNovedades.set(respuestaJson);
			}

			@Override
			public String getWrapperJson() {
				return escenarioDeTest.crearJsonDeWrapperConPedidoDeNovedades(idPrimerReceptor);
			}

		};
		conectorHttp.procesar(pedidoDeNovedades);

		// Verificamos que reciba la actualizacion de tags
		final MensajeVortex tercerMensajeRecibido = obtenerPrimerMensajeDe(respuestaDeNovedades);
		final AgregarTagsMetaMensaje tercerMetamensaje = obtenerMetaAgregarTagsDe(tercerMensajeRecibido);
		final List<String> tagsDelSegundo = tercerMetamensaje.getTags();
		Assert.isTrue(tagsDelSegundo.size() == 1, "Debería recibir sólo los tags del sergundo receptor");
		Assert.isTrue(tagsDelSegundo.contains(segundoTagPublicado), "El tag del segundo debería llegar al primero");

	}

	/**
	 * Devuelve el metamensaje agregar tasg interpretado del mensaje recibido
	 */
	private AgregarTagsMetaMensaje obtenerMetaAgregarTagsDe(final MensajeVortex primerMensaje) {
		Assert.notNull(primerMensaje);

		final ContenidoVortex contenido = primerMensaje.getContenido();
		Assert.isTrue(TipoMetaMensaje.AGREGAR_TAGS.getTipoContenidoAsociado().equals(contenido.getTipoContenido()),
				"Debería ser una publicacion de tags de parte del server");
		final String metaMensajeAgregarJson = contenido.getValor();
		final AgregarTagsMetaMensaje metaMensaje = interpreteJson.leerAgregarTagsDe(metaMensajeAgregarJson);
		Assert.notNull(metaMensaje);
		return metaMensaje;
	}
}
