/**
 * 30/05/2012 23:14:20 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.dgarcia.objectsockets.api;

/**
 * Esta interfaz es aplicable a las clases que tienen recursos asociados que deben ser liberados al
 * dejar de usarlos
 * 
 * @author D. García
 */
public interface Disposable {

	/**
	 * Cierra y libera los recursos apropiados por esta instancia
	 */
	public void closeAndDispose();

}
