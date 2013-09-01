/**
 * Created on: Sep 1, 2013 12:01:25 PM by: Dario L. Garcia
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

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa los métodos de conexión primitivos que no utilizan {@link Conector}es
 * para las conexiones.<br>
 * Los componentes que normales utilizarán conectores, los que son más básicos tienen esta interfaz
 * 
 * @author dgarcia
 */
public interface ProtoConectable {
	/**
	 * Realiza una conexión desde este componente al componente destino de manera que el destino
	 * reciba los mensajes emitidos desde este componente.<br>
	 * Si ya existía una conexión previa con otro receptor, esta será deshecha y reemplazada
	 * 
	 * @param destino
	 *            El único receptor de los mensajes enviados por este conector
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Se desconecta del receptor actual.<br>
	 * Al desconectar este conector, los mensajes serán entregados al {@link ReceptorNulo}
	 */
	public void desconectar();

}
