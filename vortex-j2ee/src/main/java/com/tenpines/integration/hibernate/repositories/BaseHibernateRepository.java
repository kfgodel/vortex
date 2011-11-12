package com.tenpines.integration.hibernate.repositories;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;

import com.tenpines.commons.annotations.CantBeNull;
import com.tenpines.commons.exceptions.FailedAssumptionException;
import com.tenpines.commons.persistence.entities.Persistible;
import com.tenpines.commons.persistence.repositories.Repository;
import com.tenpines.commons.persistence.repositories.RepositoryException;
import com.tenpines.commons.persistence.repositories.RepositoryFilter;
import com.tenpines.commons.reflection.ReflectionUtils;

/**
 * Implementación base para repositorios JPA, respetando la interfaz {@link Repository} utilizando
 * Hibernate para el acceso a la base
 * 
 * @param <B>
 *            Tipo de la entidad persistente
 * @param <K>
 *            Tipo de la clave primaria (debe ser serializable ver tipos posibles en {@link Id})
 */
public class BaseHibernateRepository<B extends Persistible<K>, K extends Serializable> implements Repository<B, K> {

	@Autowired
	private GenericHibernateRepository hibernateRepository;

	/**
	 * Instancia de la clase que este repositorio sabe persistir
	 */
	private final Class<B> clazz;

	/**
	 * Devuelve la clase persistible que este repositorio sabe persistir
	 * 
	 * @return La clase persistida por este repositorio
	 */
	@CantBeNull
	public Class<B> getPersistibleClazz() {
		return clazz;
	}

	/**
	 * Constructor que determina el tipo concreto de bean utilizando reflection para obtenerlo de la
	 * subclase parametrizada
	 */
	public BaseHibernateRepository() {
		try {
			// Asumimos que la instancia actual es de una clase que define el tipo de bean (como
			// parámetro generic de la superclase)
			// Si el tipo de bean se definiera en una superclase de esta instancia esta llamada
			// genera un error por que no encuentra el parámetro generic
			this.clazz = ReflectionUtils.getFirstGenerifiedTypeArgumentFrom(getClass());
		} catch (final IllegalArgumentException e) {
			throw new FailedAssumptionException(
					"No fue posible determinar por reflection el tipo de bean para un repositorio", e);
		}
	}

	/**
	 * Devuelve la sesión actual de Hibernate
	 * 
	 * @return La sesión actual
	 * @throws RepositoryException
	 */
	@CantBeNull
	protected Session getCurrentSession() throws RepositoryException {
		return hibernateRepository.getCurrentSession();
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#save(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public K save(final B bean) throws RepositoryException {
		return hibernateRepository.save(bean);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#merge(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public B merge(final B bean) throws RepositoryException {
		return hibernateRepository.merge(bean);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#findById(java.lang.Object)
	 */
	@Override
	public B findById(final K id) throws RepositoryException {
		return hibernateRepository.findById(id, clazz);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#delete(com.tenpines.commons.persistence.entities.Persistible)
	 */
	@Override
	public void delete(final B bean) throws RepositoryException {
		hibernateRepository.delete(bean);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#findAll()
	 */
	@Override
	public List<B> findAll() throws RepositoryException {
		return hibernateRepository.findAllOf(clazz);
	}

	/**
	 * Esta implementación espera un {@link HibernateRepositoryFilter} como filtro pasado
	 * 
	 * @see com.tenpines.commons.persistence.repositories.Repository#findAllMatching(com.tenpines.commons.persistence.repositories.RepositoryFilter)
	 * @throws IllegalArgumentException
	 *             Si el filtro pasado no es un filtro dHibernate
	 * @throws RepositoryException
	 *             Si se produce un error interno al buscar en la base
	 */
	@Override
	public List<B> findAllMatching(final RepositoryFilter filter) throws IllegalArgumentException, RepositoryException {
		return hibernateRepository.findAllMatching(filter);
	}

	/**
	 * @see com.tenpines.commons.persistence.repositories.Repository#findUniqueMatching(com.tenpines.commons.persistence.repositories.RepositoryFilter)
	 * @throws FailedAssumptionException
	 *             Si existe más de un resultado esperado
	 * @throws IllegalArgumentException
	 *             Si el filtro pasado no es un {@link HibernateRepositoryFilter}
	 * @throws RepositoryException
	 *             Si se produce un error interno al buscar en la base
	 */
	@Override
	public B findUniqueMatching(final RepositoryFilter filter) throws FailedAssumptionException,
			IllegalArgumentException, RepositoryException {
		return hibernateRepository.<B>findUniqueMatching(filter);
	}

	/**
	 * Retorna repositorio generico a las subclases.
	 * @return {@link GenericHibernateRepository}
	 */
    protected GenericHibernateRepository getHibernateRepository() {
        return hibernateRepository;
    }

	
}
