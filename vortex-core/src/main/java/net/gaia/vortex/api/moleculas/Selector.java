/**
 * Created on: Aug 31, 2013 8:15:00 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.moleculas;

import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.condiciones.SiempreTrue;

/**
 * Esta interfaz representa la molecula vortex que selecciona el conjunto de receptores de un
 * mensaje a partir de las condiciones asociadas a cada uno.<br>
 * Cada receptor conocido por este selector tiene una condicion que determina si recibe o no el
 * mensaje.<br>
 * Este componente es similar al {@link Bifurcador} pero permite tener multiples salidas en vez de
 * solo dos, y las salidas no son excluyentes.<br>
 * <br>
 * 
 * @author dgarcia
 */
public interface Selector extends Nodo {

	/**
	 * Crea un componente de salida de este selector al cual se puede conectar un receptor para
	 * recibir los mensajes que cumplen la condición indicada
	 * 
	 * @param condicion
	 *            La condición a cumplir por los mensajes para ser entregados por el conectable
	 *            creado
	 * @return El conectable que solo emite mensajes que cumplen la condición
	 */
	Filtro filtrandoCon(Condicion condicion);

	/**
	 * Quita el componente condicionado de los conectados en este selector de manera de que deje de
	 * recibir los mensajes de este componente
	 * 
	 * @param filtroCondicionado
	 *            El conectable previamente creado con {@link #condicionadoPor(Condicion)}
	 */
	void quitarFiltro(Filtro filtroCondicionado);

	/**
	 * Conecta este selector con el receptor indicado utilizando la condicion {@link SiempreTrue}
	 * como condicion default.<br>
	 * Es preferible usar {@link #filtrandoCon(Condicion)} en vez de este método
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Elimina todos los filtros asosciados, desconectando este selector de los receptores
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();

	/**
	 * Desconecta el receptor de este selector eliminando el filtro asociado
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Al recibir un mensaje este selector lo envía a todos los receptores cuya condición asociada
	 * se cumple
	 * 
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(MensajeVortex mensaje);
}
