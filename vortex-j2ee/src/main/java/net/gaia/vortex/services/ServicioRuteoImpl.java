/**
 * 20/08/2011 13:05:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.model.messages.MensajeRecibido;
import net.gaia.vortex.model.messages.protocolo.ContenidoVortex;
import net.gaia.vortex.model.messages.protocolo.IdVortex;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.receptores.ReceptorServidor;
import net.gaia.vortex.model.servidor.receptores.ReceptorVortex;
import net.gaia.vortex.persistibles.ContenidoVortexPersistible;
import net.gaia.vortex.persistibles.IdVortexPersistible;
import net.gaia.vortex.persistibles.MensajeVortexPersistible;
import net.gaia.vortex.persistibles.ReceptorHttp;
import net.gaia.vortex.persistibles.TagPersistible;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenpines.commons.persistence.repositories.GenericRepository;
import com.tenpines.commons.persistence.repositories.RepositoryFilter;
import com.tenpines.integration.hibernate.repositories.criteria.CriteriaBasedFilter;

/**
 * Implementación del servicio de ruteo
 * 
 * @author D. García
 */
@Service
public class ServicioRuteoImpl implements ServicioRuteo {
	private static final Logger LOG = LoggerFactory.getLogger(ServicioRuteoImpl.class);

	@Autowired
	private GenericRepository genericRepository;

	@Autowired
	private ReceptorServidor servidor;

	/**
	 * @see net.gaia.vortex.services.ServicioRuteo#rutear(net.gaia.vortex.model.messages.MensajeRecibido)
	 */
	@Override
	@Transactional(readOnly = false)
	public void rutear(final MensajeRecibido mensajeRecibido) {
		final MensajeVortex mensajeVortex = mensajeRecibido.getMensaje();
		final boolean esMensajeDuplicado = esMensajeYaEntregado(mensajeVortex);
		if (esMensajeDuplicado) {
			// Los mensajes duplicados los descartamos
			LOG.warn("Descartando mensaje como duplicado: {}", mensajeVortex);
			return;
		}

		final Set<String> tagsDelMensaje = mensajeRecibido.getTagsDelMensaje();
		final List<ReceptorVortex> receptoresInteresados = buscarReceptoresInteresadosEn(tagsDelMensaje);
		final ReceptorHttp emisor = mensajeRecibido.getOrigen().localizar();
		for (final ReceptorVortex receptorInteresado : receptoresInteresados) {
			if (receptorInteresado == emisor) {
				// No le mandamos el mensaje a sí mismo
				continue;
			}
			receptorInteresado.recibir(mensajeRecibido);
		}
		// Registramos que se comunicó recientemente
		emisor.setMomentoUltimaComunicacion(new DateTime());
	}

	/**
	 * Indica si el mensaje pasado coincide con alguno existente previamente en la base
	 * 
	 * @param mensajeVortex
	 *            El mensaje a consultar
	 * @return true si ya existe un mensaje en la base con el mismo ID y contenido
	 */
	private boolean esMensajeYaEntregado(final MensajeVortex mensajeVortex) {
		final RepositoryFilter duplicadoFilter = new CriteriaBasedFilter() {
			@Override
			protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
				final Criteria mensajeDuplicadoCriteria = hibernateSession
						.createCriteria(MensajeVortexPersistible.class);
				// Solo pedimos el ID para que no se creen todos los objetos
				mensajeDuplicadoCriteria.setProjection(Property.forName(MensajeVortexPersistible.id_FIELD));

				final Criteria contenidoCriteria = mensajeDuplicadoCriteria
						.createCriteria(MensajeVortex.contenido_FIELD);
				final ContenidoVortex contenidoRecibido = mensajeVortex.getContenido();
				contenidoCriteria.add(Restrictions.eq(ContenidoVortexPersistible.tipoContenido_FIELD,
						contenidoRecibido.getTipoContenido()));
				contenidoCriteria.add(Restrictions.eq(ContenidoVortexPersistible.valor_FIELD,
						contenidoRecibido.getValor()));

				final Criteria idCriteria = mensajeDuplicadoCriteria
						.createCriteria(MensajeVortexPersistible.identificacion_FIELD);
				final IdVortex idRecibido = mensajeVortex.getIdentificacion();
				idCriteria.add(Restrictions.eq(IdVortexPersistible.hashDelContenido_FIELD,
						idRecibido.getHashDelContenido()));
				idCriteria.add(Restrictions.eq(IdVortexPersistible.idDelEmisor_FIELD, idRecibido.getIdDelEmisor()));
				idCriteria.add(Restrictions.eq(IdVortexPersistible.numeroDeSecuencia_FIELD,
						idRecibido.getNumeroDeSecuencia()));
				idCriteria.add(Restrictions.eq(IdVortexPersistible.timestamp_FIELD, idRecibido.getTimestamp()));

				return mensajeDuplicadoCriteria;
			}
		};
		final List<Object> duplicados = genericRepository.findAllMatching(duplicadoFilter);
		final boolean existeEntregaPrevia = duplicados.size() > 0;
		return existeEntregaPrevia;
	}

	/**
	 * Busca los receptores registrados para los tags del nuevo mensaje
	 * 
	 * @param tagsDelMensaje
	 *            Tags que indican el contenido esperado
	 * @return La lista de todos los receptores interesados en los tags
	 */
	private List<ReceptorVortex> buscarReceptoresInteresadosEn(final Set<String> tagsDelMensaje) {
		final RepositoryFilter filter = new CriteriaBasedFilter() {
			@Override
			protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
				final Criteria receptorCriteria = hibernateSession.createCriteria(ReceptorHttp.class);
				final Criteria tagsDeReceptores = receptorCriteria
						.createCriteria(ReceptorHttp.tagsQueLeInteresan_FIELD);
				tagsDeReceptores.add(Restrictions.in(TagPersistible.valor_FIELD, tagsDelMensaje));
				return receptorCriteria;
			}
		};
		final List<ReceptorHttp> receptoresEncontrados = genericRepository.findAllMatching(filter);
		for (final ReceptorHttp receptorHttp : receptoresEncontrados) {
			// Les pasamos el repositorio para que puedan modificar su estado en la base
			receptorHttp.setGenericRepository(genericRepository);
		}

		final ArrayList<ReceptorVortex> receptoresInteresados = new ArrayList<ReceptorVortex>(
				receptoresEncontrados.size() + 1);
		if (tagsDelMensaje.contains(ReceptorServidor.DESTINO_PROXIMO_TAG)) {
			// Es un metamensaje que le interesa al servidor como receptor
			// Lo agregamos primero para que tenga prioridad en la entrega del mensaje
			receptoresInteresados.add(servidor);
		}
		receptoresInteresados.addAll(receptoresEncontrados);
		return receptoresInteresados;
	}

	/**
	 * @see net.gaia.vortex.services.ServicioRuteo#limpiarMensajesViejos()
	 */
	@Override
	@Transactional(readOnly = false)
	public void limpiarMensajesViejos() {
		LOG.debug("Iniciando limpieza de mensajes");

		// Le damos un día de gracia al mensaje
		final DateTime yesterday = new DateTime().minusDays(1);
		final RepositoryFilter receptoresViejosFilter = new CriteriaBasedFilter() {
			@Override
			protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
				final DetachedCriteria mensajesNoBorrables = DetachedCriteria.forClass(ReceptorHttp.class);
				final DetachedCriteria mensajes = mensajesNoBorrables.createCriteria(ReceptorHttp.pendientes_FIELD);
				mensajes.setProjection(Property.forName(MensajeVortexPersistible.id_FIELD));

				final Criteria mensajeCriteria = hibernateSession.createCriteria(MensajeVortexPersistible.class);
				mensajeCriteria.add(Restrictions.le(MensajeVortexPersistible.creationMoment_FIELD, yesterday));
				mensajeCriteria.add(Property.forName(MensajeVortexPersistible.id_FIELD).notIn(mensajesNoBorrables));
				return mensajeCriteria;
			}
		};
		final List<MensajeVortexPersistible> mensajesViejos = genericRepository.findAllMatching(receptoresViejosFilter);
		if (mensajesViejos.size() > 0) {
			LOG.info("Eliminando {} mensajes viejos", mensajesViejos.size());
		} else {
			LOG.debug("Sin mensajes para eliniar en esta ronda");
		}

		for (final MensajeVortexPersistible mensajeViejo : mensajesViejos) {
			genericRepository.delete(mensajeViejo);
		}
	}

	/**
	 * @see net.gaia.vortex.services.ServicioRuteo#limpiarReceptoresViejos()
	 */
	@Override
	@Transactional(readOnly = false)
	public void limpiarReceptoresViejos() {
		LOG.debug("Iniciando limpieza de receptores");

		// Le damos un día de gracia para que se comunique, si no lo sacamos
		final DateTime yesterday = new DateTime().minusDays(1);
		final RepositoryFilter receptoresViejosFilter = new CriteriaBasedFilter() {
			@Override
			protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
				final Criteria receptorCriteria = hibernateSession.createCriteria(ReceptorHttp.class);
				receptorCriteria.add(Restrictions.le(ReceptorHttp.momentoUltimaComunicacion_FIELD, yesterday));
				return receptorCriteria;
			}
		};
		final List<ReceptorHttp> receptoresViejos = genericRepository.findAllMatching(receptoresViejosFilter);
		if (receptoresViejos.size() > 0) {
			LOG.info("Eliminando {} receptores viejos", receptoresViejos.size());
		} else {
			LOG.debug("No hay receptores para eliminar en esta ronda");
		}

		for (final ReceptorHttp receptorHttp : receptoresViejos) {
			genericRepository.delete(receptorHttp);
		}
	}
}
