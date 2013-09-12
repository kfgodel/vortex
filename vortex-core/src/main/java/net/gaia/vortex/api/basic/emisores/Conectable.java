/**
 * Created on: Sep 9, 2013 11:16:01 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.basic.emisores;

import java.util.List;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa un componente que es conectable a otro componente receptor, normalmente
 * para enviarle mensajes.<br>
 * La mayoría de los componentes de vortex implementa esta interfaz como manera default de conexión,
 * y además ofrecen métodos específicos a su tipo que suelen ser más apropiados
 * 
 * @author dgarcia
 */
public interface Conectable {

	/**
	 * Realiza una conexión desde este componente al destino de manera que reciba los mensajes
	 * emitidos desde este componente.<br>
	 * <br>
	 * Este método permite una conexión default sin conocer el tipo de componente. Dependiendo del
	 * tipo concreto de este componenente se contarán con métodos adicionales para especificar
	 * parámetros adicionales de la conexión.<br>
	 * <br>
	 * Algunos tipos de componente pueden crear conexiones nuevas ante esta invocación, o reutilizar
	 * la existente
	 * 
	 * @param destino
	 *            El receptor de los mensajes a agregar
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Desconecta todos los receptores de este componente.<br>
	 * Los mensajes recibidos serán entregados al {@link ReceptorNulo}
	 */
	public void desconectar();

	/**
	 * Desconecta el receptor indicado de este componente. El receptor dejará de recibir los
	 * mensajes de este componente.<br>
	 * Si no existen componentes conectados los mensajes serán enviados al {@link ReceptorNulo}
	 * 
	 * @param destino
	 *            El destino a desconectar
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Devuelve la lista de los receptores a los que este componente está conectado.<br>
	 * Dependiendo del tipo de esta instancia la lista contendrá 1 o N elementos, y será o no
	 * modificable
	 * 
	 * @return La lista de receptores de mensajes desde esta instancia
	 */
	public List<Receptor> getConectados();

}
