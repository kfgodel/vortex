/**
 * 11/03/2011 14:04:39 Copyright (C) 2011 10Pines S.R.L.
 */
package com.tenpines.commons.persistence.entities;


/**
 * Esta interfaz representa una entidad persistible que es identificada por un objeto que representa
 * su ID.<br>
 * 
 * @author D. García
 */
public interface Persistible<K> {

	/**
	 * Devuelve el objeto que representa el ID de esta entidad
	 * 
	 * @return El id de esta entidad o null si aún no fue persistida
	 */
	public abstract K getId();

	/**
	 * Establece el ID de esta entidad.<br>
	 * Normalmente no es necesario llamará a este método, ya que hibernate le asigna uno al
	 * persistir
	 * 
	 * @param id
	 *            El nuevo ID para esta entidad
	 */
	public abstract void setId(K id);

}