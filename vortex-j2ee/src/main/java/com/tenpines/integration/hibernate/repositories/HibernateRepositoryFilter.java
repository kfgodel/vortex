/**
 * 14/03/2011 17:27:30 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories;

import java.util.List;

import org.hibernate.classic.Session;

import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.commons.persistence.repositories.RepositoryFilter;

/**
 * Esta interfaz representa un filtro utilizando hibernate como motor de persistencia para expresar
 * las queries.<br>
 * Hibernate ofrece nativamente tres tipos de queries o búsquedas, Criteria, HQL y SQL. Estos tres
 * tipos se ven reflejados en implementaciones de esta interfaz. Cada uno permite expresar queries
 * de distintas maneras y apuntan a distintos contextos de uso (en el cuál es más apropiado uno u
 * otro)
 * 
 * @author D. García
 */
public interface HibernateRepositoryFilter extends RepositoryFilter {

	/**
	 * Ejecuta este filtro en la sesión actual de Hibernate devolviendo una lista de resultados
	 * posibles
	 * 
	 * @param hibernateSession
	 *            La sesión actual de Hibernate
	 * @return La lista de resultados encontrados en la base (puede ser vacía si no había ninguno)
	 */
	<T> List<T> queryForListOn(Session hibernateSession);

	/**
	 * Ejecuta este filtro en la sesión de hibernate pasada, devolviendo un único objeto como
	 * resultado. Si este filtro implica a más de un objeto se producirá una excepción. Si es menos
	 * se devuelve null
	 * 
	 * @param hibernateSession
	 *            la sesión actual de hibernate
	 * @return El objeto encontrado o null si no había ninguno
	 * @throws FailedAssumptionException
	 *             Si había más de un objeto que cumple con este filtro (por lo que no es posible
	 *             devolver uno)
	 */
	<T> T queryForObjectOn(Session hibernateSession) throws FailedAssumptionException;

}
