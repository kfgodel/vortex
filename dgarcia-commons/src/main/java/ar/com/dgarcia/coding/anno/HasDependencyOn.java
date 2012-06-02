/**
 * Created on: 02/06/2010 14:23:59 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package ar.com.dgarcia.coding.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Este annotation sirve para crear referencias fuertes en el código con elementos que no son
 * puramente código como porciones de texto, scripts, u otros elementos para los cuales no existe
 * una entidad Java referenciable.<br>
 * No se guarda en los compilados ya que su función es sólo comunicativa a modo de ayuda para los
 * programadores. Permite explicitar las relaciones y dependencias implícitas que tiene una porción
 * de código, que de otro modo pasarían inadvertidas
 * 
 * @author D. García
 */
@Retention(RetentionPolicy.SOURCE)
public @interface HasDependencyOn {
	/**
	 * Lista de atributos field o getters de otras clases de los que este elemento de código depende
	 */
	String[] value();
}
