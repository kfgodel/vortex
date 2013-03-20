/**
 * Created on: 12/06/2010 16:48:26 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.com.dgarcia.coding.caching;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase implementa in instanciador por defecto, utilizando el constructir niladico para crear
 * nuevas instancias de la clase indicada
 * 
 * @param <T>
 *            Tipo de las instancias creadas
 * @author D. Garcia
 */
public class DefaultInstantiator<T> implements Instantiator<Object, T> {

	private Constructor<T> niladicConstructor;

	/**
	 * @see ar.com.dgarcia.coding.caching.Instantiator#instantiate(java.lang.Object)
	 */
	public T instantiate(final Object input) {
		try {
			final T newInstance = niladicConstructor.newInstance();
			return newInstance;
		} catch (final IllegalAccessException e) {
			throw new UnhandledConditionException("Couldn't access niladic constructor in invocation", e);
		} catch (final IllegalArgumentException e) {
			throw new UnhandledConditionException("Niladic constructor rejected niladic invocation ?!", e);
		} catch (final InstantiationException e) {
			throw new UnhandledConditionException("Constructor invoked is an abstract class or interface?!", e);
		} catch (final InvocationTargetException e) {
			throw new FaultyCodeException("There's an internal error in niladic constructor code", e);
		}
	}

	public static <T> DefaultInstantiator<T> create(final Class<T> baseClass) {
		if (baseClass == null) {
			throw new IllegalArgumentException("baseClass cannot be null");
		}
		if (baseClass.isInterface()) {
			throw new IllegalArgumentException("baseClass cannot be an interface");
		}
		if (Modifier.isAbstract(baseClass.getModifiers())) {
			throw new IllegalArgumentException("baseClass cannot be abstract");
		}
		Constructor<T> niladicConstructor;
		try {
			niladicConstructor = baseClass.getConstructor();
			niladicConstructor.setAccessible(true);
		} catch (final SecurityException e) {
			throw new UnhandledConditionException("There's a security restriction to use niladic constructor", e);
		} catch (final NoSuchMethodException e) {
			throw new IllegalArgumentException("baseClass doesn't have a default niladic constructor", e);
		}
		final DefaultInstantiator<T> instantiator = new DefaultInstantiator<T>();
		instantiator.niladicConstructor = niladicConstructor;
		return instantiator;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
		builder.append(": ");
		builder.append(this.niladicConstructor);
		return builder.toString();
	}

}
