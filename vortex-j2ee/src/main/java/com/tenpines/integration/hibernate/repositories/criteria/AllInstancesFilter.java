/**
 * 14/03/2011 17:51:21 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.criteria;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;

import com.tenpines.commons.persistence.entities.Persistible;

/**
 * Este filtro permite obtener todas las instancias persistidas de una clase persistible utilizando
 * {@link Criteria} para la búsqueda
 * 
 * @author D. García
 */
public class AllInstancesFilter extends CriteriaBasedFilter {

	private final Class<? extends Persistible<?>> persistibleClass;

	/**
	 * Constructor que toma la clase base sobre la que se realizará la query
	 */
	public AllInstancesFilter(final Class<? extends Persistible<?>> persistibleClass) {
		this.persistibleClass = persistibleClass;
	}

	/**
	 * @see com.tenpines.integration.hibernate.repositories.criteria.CriteriaBasedFilter#createAndConfigureCriteria(org.hibernate.classic.Session)
	 */
	@Override
	protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
		final Criteria criteria = hibernateSession.createCriteria(persistibleClass);
		return criteria;
	}

}
