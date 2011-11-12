package com.tenpines.commons.persistence.repositories;

import java.util.List;

import com.tenpines.commons.annotations.CantBeNull;
import com.tenpines.commons.annotations.MayBeNull;
import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.commons.persistence.entities.Persistible;

/**
 * Esta interfaz representa un repositorio de datos desde el cual puede obtenerse objetos de un
 * tipo.<br>
 * El repositorio ofrece los métodos comunes para la persistencia de las entidades (ABM), y además
 * según el tipo de entidad el repositorio puede ofrecer métodos adicionales que permitan
 * interactuar con la capa de persistencia de maneras más cómodas según la entidad.<br>
 * Normalmente habrá un repositorio por entidad de dominio persistente y algunos métodos adicionales
 * para queries específicas que son útiles al dominio.
 * 
 * @param <B>
 *            Tipo de la entidad persistente
 * @param <K>
 *            Tipo de la clave primaria
 */
public interface Repository<B extends Persistible<K>, K> {

	/**
	 * Recupera el estado de la base devolviéndolo en el bean que lo representa por el id indicado.<br>
	 * El bean devuelto puede ser una instancia nueva, o la misma existente en la sesión actual
	 * 
	 * @param id
	 *            El id del bean a recuperar
	 * @return El bean si es encontrado y null en otro caso
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@MayBeNull
	B findById(K id) throws RepositoryException;

	/**
	 * Guarda el estado del bean pasado en la base asegurando su creación si no existía previamente.<br>
	 * Este método tiene la misma semántica que saveOrUpdate de Hibernate
	 * 
	 * @param bean
	 *            La instancia del bean a almacenar
	 * @return El id de la entidad guardada
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	K save(B bean) throws RepositoryException;

	/**
	 * Carga todas las entidades representadas en la base para el tipo correspondiente a este
	 * repositorio
	 * 
	 * @return La lista de todos los beans que hay en el repositorio o vacía, si no hay ninguno
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	List<B> findAll() throws RepositoryException;

	/**
	 * Carga todas las entidades de la base que cumplan con los criterios del filtro pasado
	 * 
	 * @param filter
	 *            Filtro que representa condiciones que las entidades devueltas deben cumplir
	 * @return La lista de las entidades acotadas al filtro indicado, o una lista vacía si no existe
	 *         ninguno
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	List<B> findAllMatching(RepositoryFilter filter) throws RepositoryException;

	/**
	 * Carga la entidad de la base que cumple con el criterio del filtro pasado
	 * 
	 * @param filter
	 *            Filtro que representa condiciones a las entidades para obtener un valor
	 * @return El único objeto que coincide con los criterios o null si no existe ninguno
	 * @throws FailedAssumptionException
	 *             Si existe más de una entidad que cumpla los criterios
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@MayBeNull
	B findUniqueMatching(RepositoryFilter filter) throws FailedAssumptionException, RepositoryException;

	/**
	 * Guarda el estado del bean pasado en la base, copiando sus atributos al objeto que representa
	 * el estado persistente de la base, y devolviéndolo. Este método es necesario para objetos que
	 * fueron detachados de la sesión.<br>
	 * Si el objeto no tiene ID será creado en la base
	 * 
	 * @param bean
	 *            Objeto fuera de la sesión cuyo estado será copiado al objeto persistente y
	 *            persistido
	 * @return El objeto que representa el estado persistente en la base y que está vinculado a la
	 *         sesión
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	@CantBeNull
	B merge(B bean) throws RepositoryException;

	/**
	 * Borra el estado de la base identificada por el ID del bean pasado
	 * 
	 * @param bean
	 *            El objeto que indica qué registro borrar
	 * @throws RepositoryException
	 *             Si se produce un error interno durante el acceso al repositorio
	 */
	void delete(final B bean) throws RepositoryException;
}
