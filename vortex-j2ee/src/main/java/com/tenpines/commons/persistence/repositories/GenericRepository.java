/**
 * 14/03/2011 15:42:46 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.commons.persistence.repositories;

import java.util.List;

import org.hibernate.classic.Session;

import com.tenpines.commons.annotations.CantBeNull;
import com.tenpines.commons.annotations.MayBeNull;
import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.commons.persistence.entities.Persistible;

/**
 * Esta interfaz define un repositorio genérico que ofrece las operaciones básicas (ABM) para las
 * entidades persistibles de la aplicación.<br>
 * A diferencia de los repositorios dedicados, esta interfaz define los métodos básicos pero son
 * aplicables a cualquier tipo de entidad. Por otro lado esta interfaz no define métodos específicos
 * a un dominio
 * 
 * 
 * @author D. García
 */
public interface GenericRepository {

	/**
	 * Recupera el estado de la base devolviéndolo en el bean que lo representa por el id indicado.<br>
	 * El bean devuelto puede ser una instancia nueva, o la misma existente en la sesión actual
	 * (dependiendo de la estrategia de cacheo del motor de persistencia usado)
	 * 
	 * @param <K>
	 *            Tipo del objeto usado como ID
	 * @param <P>
	 *            Tipo del objeto persistible
	 * @param id
	 *            El id del bean a recuperar
	 * @param persistibleClass
	 *            Clase que indica el tipo de entidad a recuperar
	 * @return El bean si es encontrado y null en otro caso
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@MayBeNull
	public <K, P extends Persistible<K>> P findById(K id, Class<P> persistibleClass) throws RepositoryException;

	/**
	 * Guarda el estado del bean pasado en la base asegurando su creación si no existía previamente.<br>
	 * Este método tiene la misma semántica que saveOrUpdate de Hibernate (lo crea si no existe, lo
	 * actualiza en caso contrario)
	 * 
	 * @param <K>
	 *            Tipo del objeto usado como ID
	 * @param <P>
	 *            Tipo del objeto persistible
	 * @param bean
	 *            La instancia del bean a almacenar
	 * @return El id de la entidad guardada
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	public <K, P extends Persistible<K>> K save(P bean) throws RepositoryException;

	/**
	 * Carga todas las entidades representadas en la base para el tipo persistible indicado
	 * 
	 * @param <P>
	 *            Tipo del objeto persistible
	 * @return La lista de todos los beans que hay en el repositorio o vacía, si no hay ninguno
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	public <P extends Persistible<?>> List<P> findAllOf(Class<P> persistibleClass) throws RepositoryException;

	/**
	 * Realiza una búsqueda arbitraria de datos en el motor de persistencia devolviendo los
	 * resultados.<br>
	 * Dependiendo del motor, y del filtro usado esto pueden ser objetos de dominio o cualquier tipo
	 * de estructura que permite devolver los datos (por ejemplo si se buscan distintas columnas
	 * pueden ser arrays de valores).
	 * 
	 * @param <T>
	 *            Tipo del objeto devuelto (normalmente se corresponde con el filtro usado)
	 * @param filter
	 *            Filtro que representa condiciones que las entidades devueltas deben cumplir
	 * @return Una lista de resultados de tipo dependiente del filtro usado
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	public <T> List<T> findAllMatching(RepositoryFilter filter) throws RepositoryException;

	/**
	 * Realiza una búsqueda arbitraria de datos en la que se espera un único resultado.<br>
	 * El tipo devuelto dependerá del filtro usado. Por ejemplo un 'count(*)' puede devolver Long
	 * 
	 * @param <T>
	 *            Tipo del resultado esperado
	 * @param filter
	 *            Filtro que representa condiciones a las entidades para obtener un valor
	 * @return El único dato que coincide con los criterios o null si no existe ninguno
	 * @throws FailedAssumptionException
	 *             Si existe más de una entidad que cumpla los criterios
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@MayBeNull
	public <T> T findUniqueMatching(RepositoryFilter filter) throws FailedAssumptionException, RepositoryException;

	/**
	 * Guarda el estado del bean pasado en la base, copiando sus atributos al objeto que representa
	 * el estado persistente de la base, y devolviéndolo. Este método es necesario para objetos que
	 * fueron detachados de la sesión.<br>
	 * Si el objeto no tiene ID será creado en la base
	 * 
	 * @param <K>
	 *            Tipo del objeto usado como ID
	 * @param <P>
	 *            Tipo del objeto persistible
	 * @param bean
	 *            Objeto fuera de la sesión cuyo estado será copiado al objeto persistente y
	 *            persistido
	 * @return El objeto que representa el estado persistente en la base y que está vinculado a la
	 *         sesión
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	public <K, P extends Persistible<K>> P merge(P bean) throws RepositoryException;

	/**
	 * Borra el estado del objeto en la base identificándolo por el ID y el tipo del bean pasado
	 * 
	 * @param <P>
	 *            Tipo del objeto persistible
	 * @param bean
	 *            El objeto que indica qué registro borrar
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	public <P extends Persistible<?>> void delete(final P bean) throws RepositoryException;

	/**
	 * Devuelve la sesión actual de Hibernate
	 * 
	 * @return La sesión actual
	 * @throws RepositoryException
	 *             Si no existe la sesión o se produce unlaerror al obtenera
	 */
	@CantBeNull
	public Session getCurrentSession() throws RepositoryException;
}
