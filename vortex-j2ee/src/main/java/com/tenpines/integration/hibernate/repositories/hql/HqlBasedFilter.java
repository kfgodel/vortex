/**
 * 14/03/2011 18:13:24 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.hql;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.tenpines.commons.annotations.CantBeNull;

/**
 * Esta clase ofrece la implementación base para filtros de hibernate utilizando HQL como lenguaje
 * para expresar la query.<br>
 * Esta clase abstracta sirve como template para subclases que deseen representar el filtro como
 * texto HQL
 * 
 * @author D. García
 */
public abstract class HqlBasedFilter extends QueryBasedFilter {

	/**
	 * En la implementación actual sólo se crea la query en base a {@link #getHqlQuery()} de manera
	 * que las subclases tengan esa implementación disponible de la cual agregar más comportamiento
	 * (como parámetros con nombre)
	 * 
	 * @see com.tenpines.integration.hibernate.repositories.hql.QueryBasedFilter#createAndConfigureHqlQuery(org.hibernate.classic.Session)
	 */
	@Override
	protected Query createAndConfigureQuery(final Session hibernateSession) {
		final Query createdQuery = hibernateSession.createQuery(getHqlQuery());
		return createdQuery;
	}

	/**
	 * Devuelve la query hql con la que se crea una instancia de {@link Query}, a la cual es posible
	 * agregarle parámetros
	 * 
	 * @return El texto de la query HQL
	 */
	@CantBeNull
	protected abstract String getHqlQuery();

}
