/**
 * 14/03/2011 18:13:24 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.sql;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import com.tenpines.commons.annotations.CantBeNull;
import com.tenpines.integration.hibernate.repositories.hql.QueryBasedFilter;

/**
 * Esta clase ofrece la implementación base para filtros de hibernate utilizando SQL como lenguaje
 * para expresar la query.<br>
 * Esta clase sirve de template para implementar filtros con una query SQL
 * 
 * @author D. García
 */
public abstract class SqlBasedFilter extends QueryBasedFilter {

	/**
	 * Crea y configura la query SQL para reflejar las condiciones de este filtro.<br>
	 * En la implementación actual sólo se crea la query en base a {@link #getSqlQuery()} de manera
	 * que las subclases tengan esa implementación disponible de la cual agregar más comportamiento
	 * (como parámetros con nombre)
	 * 
	 * @param hibernateSession
	 *            Sesión actual de hibernate de la cual crear la query
	 * @return El objeto query de hibernate que representa una query HQL
	 */
	@Override
	protected SQLQuery createAndConfigureQuery(final Session hibernateSession) {
		final SQLQuery createdQuery = hibernateSession.createSQLQuery(getSqlQuery());
		return createdQuery;
	}

	/**
	 * Devuelve la query hql con la que se crea una instancia de {@link Query}, a la cual es posible
	 * agregarle parámetros
	 * 
	 * @return El texto de la query HQL
	 */
	@CantBeNull
	protected abstract String getSqlQuery();

}
