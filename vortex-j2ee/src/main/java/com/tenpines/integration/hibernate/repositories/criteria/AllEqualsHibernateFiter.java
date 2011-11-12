/**
 * 11/03/2011 19:25:56 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.integration.hibernate.repositories.criteria;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.tenpines.commons.persistence.entities.Persistible;

/**
 * Esta clase implementa un filtro básico que permite restringir las entidades devueltas por
 * hibernate aplicando filtros restrictivos con AND, en las propiedades indicadas.<br>
 * Todas las entidades devueltas corresponden a la misma clase de persistible sobre la que se
 * aplican las restricciones.<br>
 * Las propiedades cuyo valor es null, tendrán una restricción aplicada de "IS NULL" en la base
 * 
 * @author D. García
 */
public class AllEqualsHibernateFiter extends AllInstancesFilter {

	/**
	 * Mapa en el que se guardan todas las restricciones de la clase
	 */
	private final Map<String, Object> restrictedProperties = new HashMap<String, Object>(1);

	/**
	 * Restringe las entidades incluidas en este filtro a que contengan el valor indicado para el
	 * atributo pasado.<br>
	 * Esta restricción es agregada al resto (o sea, se aplica AND con todas las restricciones).<br>
	 * Si el valor pasado es una colección, la semántica aplicada en al query es de IN, en vez de
	 * EQUALS.
	 * 
	 * @param propertyName
	 *            Nombre de la propiedad de la clase filtrada
	 * @param value
	 *            El valor que deben tener las entidades en la propiedad
	 */
	public void restrict(final String propertyName, final Object value) {
		if (propertyName == null) {
			throw new IllegalArgumentException("El nombre de la propiedad no puede ser null");
		}
		restrictedProperties.put(propertyName, value);
	}

	/**
	 * Devuelve el mapa de todas las restricciones aplicadas en este filtro
	 * 
	 * @return El conjunto de propiedad/valor restringido
	 */
	public Map<String, Object> getRestrictions() {
		return restrictedProperties;
	}

	/**
	 * Constructor mínimo que toma la clase persistible.<br>
	 * Si este filtro se usa así se devolverán todas las instancias persistidas de la clase
	 */
	public AllEqualsHibernateFiter(final Class<? extends Persistible<?>> persistibleClass) {
		super(persistibleClass);
	}

	/**
	 * @see com.tenpines.integration.hibernate.repositories.criteria.AllInstancesFilter#createAndConfigureCriteria(org.hibernate.classic.Session)
	 */
	@Override
	protected Criteria createAndConfigureCriteria(final Session hibernateSession) {
		final Criteria criteria = super.createAndConfigureCriteria(hibernateSession);
		final Map<String, Object> allRestrictions = getRestrictions();
		final Set<Entry<String, Object>> entries = allRestrictions.entrySet();
		for (final Entry<String, Object> entry : entries) {
			final String propertyName = entry.getKey();
			final Object propertyValue = entry.getValue();
			Criterion restrictionCriterion;
			if (propertyValue == null) {
				restrictionCriterion = Restrictions.isNull(propertyName);
			} else {
				if (propertyValue instanceof Collection) {
					final Collection<?> restrictedValues = (Collection<?>) propertyValue;
					restrictionCriterion = Restrictions.in(propertyName, restrictedValues);
				} else {
					restrictionCriterion = Restrictions.eq(propertyName, propertyValue);
				}
			}
			criteria.add(restrictionCriterion);
		}
		return criteria;
	}
}
