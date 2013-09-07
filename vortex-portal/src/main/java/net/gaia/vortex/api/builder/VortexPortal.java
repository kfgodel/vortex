/**
 * Created on: Sep 5, 2013 9:58:16 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.builder;

import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.impl.atomos.Desobjetivizador;
import net.gaia.vortex.impl.atomos.Objetivizador;
import net.gaia.vortex.portal.api.mensaje.HandlerDeObjetos;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;

/**
 * Esta interfaz extiende las opciones de creacion de {@link VortexCore} y agrega metodos propios de
 * los portales
 * 
 * @author dgarcia
 */
public interface VortexPortal {

	/**
	 * Devuelve el builder core de este builder para los componentes más básicos
	 * 
	 * @return El builder basico de los componentes core
	 */
	VortexCore getCore();

	/**
	 * Crea la forma más basica de portal que solo convierte entre objetos y mensajes sin poder
	 * discriminar mensajes repetidos, o mensajes propios.<br>
	 * Este tipo de portal es apropiado cuando se conoce la topología de toda la red y no existen
	 * bucles en la misma
	 * 
	 * @return El portal creado
	 */
	Portal portalConversor();

	/**
	 * Crea un portal que puede identificar los mensajes enviados y recibidos de manera de descartar
	 * mensajes propios y duplicados.<br>
	 * Este tipo de portal basico puede ser utilizado en topologías simples cuando existen bucles en
	 * la red
	 * 
	 * @return El portal creado
	 */
	Portal portalIdentificador();

	/**
	 * Devuelve el conversor utilizado para pasar objetos a mensajes y viceversa
	 * 
	 * @return El conversor de este builder
	 */
	ConversorDeMensajesVortex getConversorDeMensajes();

	/**
	 * Crea y devuelve un componente que convierte objetos en mensajes
	 * 
	 * @return El conversor de objetos a mensajes
	 */
	Desobjetivizador conversorDesdeObjetos();

	/**
	 * Crea y devuelve un componente que convierte mensajes en objetos
	 * 
	 * @param tipoEsperadoComoMensajes
	 *            El tipo al que se deben convertir los mensajes
	 * @param handlerDeObjetos
	 *            El handler que recibirá los objetos convertidos al tipo indicado
	 * @return El conversor creado
	 */
	<T> Objetivizador conversorHaciaObjetos(final Class<? extends T> tipoEsperadoComoMensajes,
			final HandlerDeObjetos<? super T> handlerDeObjetos);

}
