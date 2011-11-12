/**
 * 20/08/2011 13:29:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.servidor.receptores;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.externals.hash.StringHasher;
import net.gaia.vortex.externals.json.InterpreteJackson;
import net.gaia.vortex.externals.json.InterpreteJson;
import net.gaia.vortex.externals.time.TimeStamper;
import net.gaia.vortex.model.messages.MensajeRecibido;
import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.model.messages.meta.AgregarTagsMetaMensaje;
import net.gaia.vortex.model.messages.meta.MetamensajeSobreTags;
import net.gaia.vortex.model.messages.meta.QuitarTagsMetaMensaje;
import net.gaia.vortex.model.messages.protocolo.ContenidoVortex;
import net.gaia.vortex.model.messages.protocolo.IdVortex;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.ids.GeneradorIdEmisor;
import net.gaia.vortex.model.servidor.ids.SecuenciadorMensajes;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaPorId;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaPorInstancia;
import net.gaia.vortex.persistibles.ReceptorHttp;
import net.gaia.vortex.persistibles.TagPersistible;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.tenpines.commons.exceptions.UnhandledConditionException;
import com.tenpines.commons.persistence.repositories.GenericRepository;
import com.tenpines.integration.hibernate.repositories.criteria.CriteriaBasedFilter;

/**
 * Esta clase representa al servidor como cliente de si mismo. Como tal es un receptor más de los
 * mensajes.<br>
 * Este receptor permite tratar los mensajes de control como el resto de los mensajes sin necesidad
 * de acudir a excepciones en el protocolo.<br>
 * <br>
 * Esta instancia es compartida por todos los threads y creada por Spring, quien se asegura de que
 * sea única. De esa manera nos aseguramos que los "intereses" del servidor sean los mismos para
 * todos los threads
 * 
 * @author D. García
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ReceptorServidor implements ReceptorVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ReceptorServidor.class);

	/**
	 * Tag utilizado para la comunicación directa con el nodo al que se esta conectado
	 */
	public static String DESTINO_PROXIMO_TAG = "CHE";

	@Autowired
	private GenericRepository genericRepository;

	@Autowired
	private StringHasher hasher;

	@Autowired
	private TimeStamper timeStamper;

	@Autowired
	private GeneradorIdEmisor generadorIdEmisor;

	@Autowired
	private SecuenciadorMensajes secuenciadorMensajes;

	private String idEmisor;

	public String getIdEmisor() {
		if (idEmisor == null) {
			idEmisor = generadorIdEmisor.getIdEmisor();
		}
		return idEmisor;
	}

	private InterpreteJson interpreteJson;

	public InterpreteJson getInterpreteJson() {
		if (interpreteJson == null) {
			interpreteJson = InterpreteJackson.create();
		}
		return interpreteJson;
	}

	public GenericRepository getGenericRepository() {
		return genericRepository;
	}

	public void setGenericRepository(final GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}

	/**
	 * @see net.gaia.vortex.model.servidor.receptores.ReceptorVortex#recibir(net.gaia.vortex.model.messages.MensajeRecibido)
	 */
	@Override
	public void recibir(final MensajeRecibido mensajeRecibido) {
		final MensajeVortex mensaje = mensajeRecibido.getMensaje();
		final ContenidoVortex contenido = mensaje.getContenido();
		final String tipoContenidoRecibido = contenido.getTipoContenido();
		final TipoMetaMensaje tipoMetaMensaje = TipoMetaMensaje.obtenerDeTipoDeContenido(tipoContenidoRecibido);
		if (tipoMetaMensaje == null) {
			LOG.error("Ignorando un tipo de metamensaje desconocido: {}", mensaje);
			return;
		}
		switch (tipoMetaMensaje) {
		case CERRAR_CONEXION:
			final ReferenciaAReceptor referenciaAEliminar = mensajeRecibido.getOrigen();
			eliminarReceptor(referenciaAEliminar);
			break;
		case AGREGAR_TAGS:
			final String agregarString = contenido.getValor();
			final AgregarTagsMetaMensaje agregarTags = getInterpreteJson().leerAgregarTagsDe(agregarString);
			final ReferenciaAReceptor origen = mensajeRecibido.getOrigen();
			modificarTagsDesde(agregarTags, origen);
			break;
		case QUITAR_TAGS:
			final String quitarString = contenido.getValor();
			final QuitarTagsMetaMensaje quitarTags = getInterpreteJson().leerQuitarTagsDe(quitarString);
			final ReferenciaAReceptor origen2 = mensajeRecibido.getOrigen();
			modificarTagsDesde(quitarTags, origen2);
			break;
		default:
			throw new UnhandledConditionException("Tipo de meta mensaje desconocido: " + tipoMetaMensaje);
		}
	}

	/**
	 * Elimina el receptor que indicó el cierre de conexión
	 * 
	 * @param origen
	 *            La referencia al receptor a eliminar
	 */
	private void eliminarReceptor(final ReferenciaAReceptor origen) {
		if (!origen.esIdentificableEnLaBase()) {
			// No es un receptor que se pueda eliminar
			return;
		}
		final ReceptorHttp exReceptor = origen.localizar();
		LOG.info("Receptor[{}] desconectandose", exReceptor.getId());
		genericRepository.delete(exReceptor);

		final ReferenciaPorInstancia referenciaPerdida = ReferenciaPorInstancia.create(null);
		origen.reemplazarPor(referenciaPerdida);
	}

	/**
	 * @param metaMensaje
	 * @param origen
	 * @return
	 */
	private void modificarTagsDesde(final MetamensajeSobreTags metaMensaje, final ReferenciaAReceptor origen) {
		final ReceptorHttp receptorHttp = origen.localizar();
		final boolean esPrimeraVez = receptorHttp.getId() == null;
		metaMensaje.modificarTagsInteresantesPara(receptorHttp);
		genericRepository.save(receptorHttp);
		if (esPrimeraVez) {
			LOG.info("ID[{}] asignado al nuevo receptor: {}", receptorHttp.getId(),
					receptorHttp.getTagsQueLeInteresan());
			// Tenemos que darle a conocer su ID recien asignado
			final ReferenciaPorId referenciaNueva = ReferenciaPorId.create(genericRepository, receptorHttp.getId());
			origen.reemplazarPor(referenciaNueva);
		}
		notificarCambiosEnTagsAReceptores();
	}

	/**
	 * Genera mensajes para actualizar el estado de tags para los receptores que hayan quedado
	 * desactualizados después de los cambios de tags
	 */
	private void notificarCambiosEnTagsAReceptores() {
		final List<ReceptorHttp> allReceptores = genericRepository.findAllOf(ReceptorHttp.class);
		for (final ReceptorHttp receptorHttp : allReceptores) {
			receptorHttp.setGenericRepository(genericRepository);
			final Set<String> tagsDelServer = getTagsDelServerExcluyendo(receptorHttp);
			notificarCambiosEnTagsSiNecesario(receptorHttp, tagsDelServer);
		}
	}

	/**
	 * Verifica si el receptor pasado necesita ser notificado de cambios en los tags
	 * 
	 * @param receptorHttp
	 *            El receptor a actualizar
	 * @param tagsDelServer
	 *            Los tags que incluye el server
	 */
	private void notificarCambiosEnTagsSiNecesario(final ReceptorHttp receptorHttp,
			final Set<String> tagsActualesDelServer) {
		final Set<String> tagsAgregadosParaElReceptor = receptorHttp.detectarMensajesNuevosEn(tagsActualesDelServer);
		if (!tagsActualesDelServer.isEmpty()) {
			final AgregarTagsMetaMensaje metaMensajeTagsAgregados = AgregarTagsMetaMensaje
					.create(tagsAgregadosParaElReceptor);
			enviarMetaMensaje(receptorHttp, metaMensajeTagsAgregados);
		}
		final Set<String> tagsQuitadosParaElReceptor = receptorHttp.detectarMensajesQuitadosEn(tagsActualesDelServer);
		if (!tagsQuitadosParaElReceptor.isEmpty()) {
			final QuitarTagsMetaMensaje metaMensajeTagsAgregados = QuitarTagsMetaMensaje
					.create(tagsQuitadosParaElReceptor);
			enviarMetaMensaje(receptorHttp, metaMensajeTagsAgregados);
		}
	}

	/**
	 * @param receptorHttp
	 */
	private void enviarRespuestaDeTags(final ReceptorHttp receptorHttp) {
		final Set<String> tagsDelServerExcluyendoAlReceptor = getTagsDelServerExcluyendo(receptorHttp);
		final AgregarTagsMetaMensaje metaMensaje = AgregarTagsMetaMensaje.create(tagsDelServerExcluyendoAlReceptor);
		enviarMetaMensaje(receptorHttp, metaMensaje);
	}

	/**
	 * Envia el metamensaje pasado al receptor
	 */
	private void enviarMetaMensaje(final ReceptorHttp receptorHttp, final MetamensajeSobreTags metaMensaje) {
		final MensajeVortex respuestaConTags = crearMensajeDeModificacionDeTagsPara(receptorHttp, metaMensaje);
		final ReferenciaAReceptor autoReferencia = ReferenciaPorInstancia.create(null);
		final MensajeRecibido nuevoMensaje = MensajeRecibido.create(respuestaConTags, autoReferencia, interpreteJson);
		receptorHttp.recibir(nuevoMensaje);
		metaMensaje.modificarTagsNotificadosA(receptorHttp);
	}

	/**
	 * Genera el mensaje que contiene el metamensaje con el id asignado
	 * 
	 * @param receptorHttp
	 *            .getId() El id asignado al receptor
	 * @param metaMensaje
	 * @return El nuevo mensaje
	 */
	private MensajeVortex crearMensajeDeModificacionDeTagsPara(final ReceptorHttp receptorHttp,
			final MetamensajeSobreTags metaMensaje) {
		final String metaMensajeJson = getInterpreteJson().escribirMetamensajeSobre(metaMensaje);
		final MensajeVortex mensaje = new MensajeVortex();
		final TipoMetaMensaje tipoDeMetamensaje = metaMensaje.getTipoDeMetamensaje();
		final ContenidoVortex contenidoVortex = ContenidoVortex.create(tipoDeMetamensaje.getTipoContenidoAsociado(),
				metaMensajeJson);
		mensaje.setContenido(contenidoVortex);

		mensaje.setTagsDestino(Lists.newArrayList(ReceptorServidor.DESTINO_PROXIMO_TAG));

		final String hashDelContenido = hasher.hashDe(metaMensajeJson);
		final Long timestamp = timeStamper.currentTimestamp();
		final String idEmisor = getIdEmisor();
		final long numeroSecuenciamensaje = secuenciadorMensajes.proximoNumeroSecuencia();
		final IdVortex nuevoId = IdVortex.create(hashDelContenido, idEmisor, numeroSecuenciamensaje, timestamp);
		mensaje.setIdentificacion(nuevoId);
		return mensaje;
	}

	/**
	 * Devuelve los tags que le interesan a este servidor. Los tags devueltos representan a los tags
	 * de todos los clientes conectados, más los metamensajes.
	 * 
	 * @param receptorHttp
	 * 
	 * @return
	 */
	private Set<String> getTagsDelServerExcluyendo(final ReceptorHttp receptorHttp) {
		final List<String> tagsDeClientesConectados = genericRepository.findAllMatching(new CriteriaBasedFilter() {
			@Override
			protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
				final Criteria tagsDeReceptores = hibernateSession.createCriteria(TagPersistible.class);
				tagsDeReceptores.add(Restrictions.isNotNull(TagPersistible.receptor_FIELD));
				if (receptorHttp != null) {
					// Si no se indica receptor no se excluyen tags
					tagsDeReceptores.add(Restrictions.ne(TagPersistible.receptor_FIELD, receptorHttp));
				}
				tagsDeReceptores.setProjection(Projections.distinct(Projections.property(TagPersistible.valor_FIELD)));
				return tagsDeReceptores;
			}
		});
		final Set<String> tagsQueMeInteresan = new LinkedHashSet<String>();
		tagsQueMeInteresan.addAll(tagsDeClientesConectados);
		return tagsQueMeInteresan;
	}

}
