/**
 * 14/03/2011 16:15:12 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories;

import org.hibernate.classic.Session;

/**
 * Esta interfaz es una manera de encapsular código dependiente de la sesión de Hibernate que
 * requiere una para ejecutarse correctamente.<br>
 * 
 * @author D. García
 */
public interface HibernateSessionDependent<T> {

	/**
	 * Ejecuta el código representado por esta instancia usando la sesión indicada.<br>
	 * El objeto resultante es devuelto por el método.<br>
	 * Este método puede lanzar excepciones normalmente que serán tratadas según el contexto de
	 * ejecución
	 * 
	 * @param hibernateSession
	 *            Sesión de hibernate para utilizar
	 * @return El resultado de la ejecución del código
	 */
	public T withSession(Session hibernateSession);

}
