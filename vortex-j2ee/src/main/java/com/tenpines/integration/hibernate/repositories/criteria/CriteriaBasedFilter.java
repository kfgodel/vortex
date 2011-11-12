/**
 * 11/03/2011 16:28:54 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.criteria;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueResultException;
import org.hibernate.classic.Session;

import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter;

/**
 * Esta clase ofrece la implementación base para filtros de hibernate utilizando {@link Criteria}
 * para armar la query.
 * 
 * @author D. García
 */
public abstract class CriteriaBasedFilter implements HibernateRepositoryFilter {

	/**
	 * @see com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter#queryForListOn(org.hibernate.classic.Session)
	 */
	@Override
	public <T> List<T> queryForListOn(final Session hibernateSession) {
		final Criteria criteria = createAndConfigureCriteria(hibernateSession);
		@SuppressWarnings("unchecked")
		final List<T> results = criteria.list();
		return results;
	}

	/**
	 * Crea el criteria a partir de la sesión actual pasada, y la configura de acuerdo a las
	 * condiciones de este filtro, de manera que los resultados devueltos en hibernate cumplan con
	 * las condiciones representadas por este filtro
	 * 
	 * 
	 * @param hibernateSession
	 *            La sesión que permite obtener un criteria
	 * @return El criteria que se configurará para representar este filtro como query en la base
	 */
	protected abstract Criteria createAndConfigureCriteria(Session hibernateSession);

	/**
	 * @see com.tenpines.integration.hibernate.repositories.HibernateRepositoryFilter#queryForObjectOn(org.hibernate.classic.Session)
	 */
	@Override
	public <T> T queryForObjectOn(final Session hibernateSession) throws FailedAssumptionException {
		final Criteria criteria = createAndConfigureCriteria(hibernateSession);
		// Sólo necesitamos uno, pero queremos otro para detectar si hay más de uno en la base
		criteria.setMaxResults(2);
		try {
			@SuppressWarnings("unchecked")
			final T uniqueResult = (T) criteria.uniqueResult();
			return uniqueResult;
		} catch (final NonUniqueResultException e) {
			throw new FailedAssumptionException("Se esperaba como mucho un sólo elemento y existen más", e);
		}
	}

}
