/**
 * 14/03/2011 16:06:11 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tenpines.commons.annotations.CantBeNull;
import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.commons.persistence.entities.Persistible;
import com.tenpines.commons.persistence.repositories.GenericRepository;
import com.tenpines.commons.persistence.repositories.RepositoryException;
import com.tenpines.commons.persistence.repositories.RepositoryFilter;
import com.tenpines.integration.hibernate.repositories.criteria.AllInstancesFilter;

/**
 * Esta clase implementa el repositorio de objetos genérico (para cualquier tipo) con Hibernate
 * 
 * @author D. García
 */
@Component
public class GenericHibernateRepository implements GenericRepository {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#findById(java.lang.Object,
	 *      java.lang.Class)
	 * @throws IllegalArgumentException
	 *             Si el id pasado no es serializable
	 */
	@Override
	public <K, P extends Persistible<K>> P findById(final K id, final Class<P> persistibleClass)
			throws RepositoryException, IllegalArgumentException {
		final Serializable hibernateId = checkHibernateId(id);
		return doWithHibernateSession(new HibernateSessionDependent<P>() {
			@Override
			public P withSession(final Session hibernateSession) {
				@SuppressWarnings("unchecked")
				final P entity = (P) hibernateSession.get(persistibleClass, hibernateId);
				return entity;
			}
		});
	}

	/**
	 * Verifica que el id pasado sea serializable (hibernate requiere este tipo para los ids)
	 * 
	 * @param id
	 *            El id a chequear
	 * @return La versión casteada a serializable o tira una excepción si no es casteable
	 * @throws IllegalArgumentException
	 *             Si el objeto pasado no es una instancia de serializable
	 */
	private Serializable checkHibernateId(final Object id) throws IllegalArgumentException {
		if (!(id instanceof Serializable)) {
			throw new IllegalArgumentException("El id usado no es serializable, y hibernate necesita uno: " + id);
		}
		final Serializable hibernateId = (Serializable) id;
		return hibernateId;
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#save(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public <K, P extends Persistible<K>> K save(final P bean) throws RepositoryException {
		return doWithHibernateSession(new HibernateSessionDependent<K>() {
			@Override
			public K withSession(final Session hibernateSession) {
				hibernateSession.saveOrUpdate(bean);
				return bean.getId();
			}
		});
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#findAllOf(java.lang.Class)
	 *      Este método en realidad es un helper method que facilita la interfaz pero internamente
	 *      delega en {@link #findAllMatching(RepositoryFilter)} usando un
	 *      {@link AllInstancesFilter}
	 */
	@Override
	public <P extends Persistible<?>> List<P> findAllOf(final Class<P> persistibleClass) throws RepositoryException {
		final AllInstancesFilter allInstancesFilter = new AllInstancesFilter(persistibleClass);
		return findAllMatching(allInstancesFilter);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#findAllMatching(com.tenpines.commons.persistence.repositories.RepositoryFilter)
	 */
	@Override
	public <T> List<T> findAllMatching(final RepositoryFilter filter) throws RepositoryException {
		final HibernateRepositoryFilter hibernateFilter = checkHibernateFilter(filter);
		return doWithHibernateSession(new HibernateSessionDependent<List<T>>() {
			@Override
			public List<T> withSession(final Session hibernateSession) {
				final List<T> results = hibernateFilter.queryForListOn(hibernateSession);
				return results;
			}
		});
	}

	/**
	 * Verifica que el argumento pasado sea una instancia de filtro hibernate y devuelve su versión
	 * casteada
	 * 
	 * @param filter
	 *            El filtro a verificar
	 * @return El filtro casteado
	 * @throws IllegalArgumentException
	 *             Si el filtro no es una instancia de un {@link HibernateRepositoryFilter}
	 */
	private HibernateRepositoryFilter checkHibernateFilter(final RepositoryFilter filter)
			throws IllegalArgumentException {
		if (!(filter instanceof HibernateRepositoryFilter)) {
			throw new IllegalArgumentException("El filtro pasado no es un filtro Hibernate: " + filter);
		}
		final HibernateRepositoryFilter hibernateFilter = (HibernateRepositoryFilter) filter;
		return hibernateFilter;
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#findUniqueMatching(com.tenpines.commons.persistence.repositories.RepositoryFilter)
	 */
	@Override
	public <T> T findUniqueMatching(final RepositoryFilter filter) throws FailedAssumptionException,
			RepositoryException {
		final HibernateRepositoryFilter hibernateFilter = checkHibernateFilter(filter);
		return doWithHibernateSession(new HibernateSessionDependent<T>() {
			@Override
			public T withSession(final Session hibernateSession) {
				final T result = hibernateFilter.<T>queryForObjectOn(hibernateSession);
				return result;
			}
		});
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#merge(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public <K, P extends Persistible<K>> P merge(final P bean) throws RepositoryException {
		return doWithHibernateSession(new HibernateSessionDependent<P>() {
			@Override
			public P withSession(final Session hibernateSession) {
				@SuppressWarnings("unchecked")
				final P merged = (P) hibernateSession.merge(bean);
				return merged;
			}
		});
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.GenericRepository#delete(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public <P extends Persistible<?>> void delete(final P bean) throws RepositoryException {
		doWithHibernateSession(new HibernateSessionDependent<Void>() {
			@Override
			public Void withSession(final Session hibernateSession) {
				hibernateSession.delete(bean);
				return null;
			}
		});
	}

	/**
	 * Devuelve la sesión actual de Hibernate
	 * 
	 * @return La sesión actual
	 * @throws RepositoryException
	 *             Si no existe la sesión o se produce unlaerror al obtenera
	 */
	@CantBeNull
	public Session getCurrentSession() throws RepositoryException {
		try {
			final Session currentSession = sessionFactory.getCurrentSession();
			if (currentSession == null) {
				// No debería pasar
				throw new FailedAssumptionException("Hibernate devolvió null como sesión actual");
			}
			return currentSession;
		} catch (final HibernateException e) {
			throw new RepositoryException("No fue posible acceder a la sesión actual de Hibernate", e);
		}
	}

	/**
	 * Ejecuta una porción de código pasándole la sesión de hibernate actual para que opere con la
	 * base
	 * 
	 * @param <T>
	 *            Tipo del objeto retornado como resultado
	 * @return El objeto devuelto por el código
	 * @throws RepositoryException
	 *             Si se produce un error al operar sobre la sesión
	 */
	protected <T> T doWithHibernateSession(final HibernateSessionDependent<T> code) throws RepositoryException {
		final Session currentSession = getCurrentSession();
		try {
			final T result = code.withSession(currentSession);
			return result;
		} catch (final HibernateException e) {
			throw new RepositoryException("Se produjo un error interno utilizando Hibernate", e);
		}
	}
}
