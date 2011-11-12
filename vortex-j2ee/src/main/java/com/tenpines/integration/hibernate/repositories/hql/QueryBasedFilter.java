/**
 * 14/03/2011 18:31:02 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.hql;

import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter;

/**
 * Esta clase sirve de base para las implementaciones de filtros basados en objetos {@link Query}
 * (HQL y SQL)
 * 
 * @author D. García
 */
public abstract class QueryBasedFilter implements HibernateRepositoryFilter {

	/**
	 * @see com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter#queryForListOn(org.hibernate.classic.Session)
	 */
	@Override
	public <T> List<T> queryForListOn(final Session hibernateSession) {
		final Query createdQuery = createAndConfigureQuery(hibernateSession);
		@SuppressWarnings("unchecked")
		final List<T> results = createdQuery.list();
		return results;
	}

	/**
	 * Crea y configura la query para reflejar las condiciones de este filtro.<br>
	 * 
	 * @param hibernateSession
	 *            Sesión actual de hibernate de la cual crear la query
	 * @return El objeto query de hibernate que representa una query HQL
	 */
	protected abstract Query createAndConfigureQuery(final Session hibernateSession);

	/**
	 * @see com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter#queryForObjectOn(org.hibernate.classic.Session)
	 */
	@Override
	public <T> T queryForObjectOn(final Session hibernateSession) throws FailedAssumptionException {
		final Query createdQuery = createAndConfigureQuery(hibernateSession);
		// Sólo necesitamos uno, pero queremos otro para detectar si hay más de uno en la base
		createdQuery.setMaxResults(2);
		try {
			@SuppressWarnings("unchecked")
			final T uniqueResult = (T) createdQuery.uniqueResult();
			return uniqueResult;
		} catch (final NonUniqueResultException e) {
			throw new FailedAssumptionException("Se esperaba como mucho un sólo elemento y existen más", e);
		}
	}

}