/**
 * Created on: Sep 1, 2013 2:11:16 AM by: Dario L. Garcia
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
package net.gaia.vortex.api.atomos;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa el componente vortex que evalua una condicion sobre los mensajes
 * recibidos para determinar si descartarlos o entregarlos a su salida.<br>
 * Si la condición de este filtro es verdadera el mensaje será entregado. Si es falsa se descarta
 * 
 * @author dgarcia
 */
@Atomo
public interface Filtro extends Nodo {
	/**
	 * Devuelve el conector utilizado en caso de true.<br>
	 * Los mensajes que evalúen a true serán entregados al receptor conectado a este conector.
	 * 
	 * @return El conector por casos true
	 */
	public Conector getConectorPorTrue();

	/**
	 * Devuelve la condición utilizada por este componente para evaluar los mensajes y decidir su
	 * conector destino
	 * 
	 * @return La condición evaluada en cada mensaje recibido
	 */
	public Condicion getCondicion();

	/**
	 * Establece la condición utilizada por este filtro para aceptar los mensajes y pasarlos al
	 * receptor que corresponda. Si la condición pasada devuelve true el mensaje es entregado al
	 * receptor del conector {@link #getConectorPorTrue()}, si es false, es entregada al conector de
	 * {@link #getConectorPorFalse()}. Si falla o es incierta, se descarta el mensaje enviandolo al
	 * receptor nulo
	 * 
	 * @param condicion
	 *            La condición a utilizar
	 */
	public void setCondicion(Condicion condicion);

}
