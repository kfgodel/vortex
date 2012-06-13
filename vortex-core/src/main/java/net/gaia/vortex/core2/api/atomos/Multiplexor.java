/**
 * 12/06/2012 22:19:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.api.atomos;

import net.gaia.vortex.core2.api.annon.Atomo;

/**
 * Esta interfaz representa el componente vortex que permite enviar un mensaje a multiples
 * destinatarios al a vez.<br>
 * Normalmente la implementación de esta interfaz utilizará varios threads para multiplexar en
 * paralelo aunque no es obligatorio
 * 
 * @author D. García
 */
@Atomo
public interface Multiplexor extends ComponenteVortex {

	/**
	 * Agrega en este emisor un componente destino al que se debe derivar el mensaje recibido
	 * 
	 * @param destino
	 *            Uno de los componentes que recibirán el mensaje recibido por esta instancia
	 */
	public void agregarDestino(ComponenteVortex destino);

	/**
	 * Quita del conjunto de destinos de este multiplexor el componente indicado.<br>
	 * Si el componente no era parte de esta instancia este método no tendrá efecto
	 * 
	 * @param destino
	 *            El componente a quitar de este {@link Multiplexor}
	 */
	public void quitarDestino(ComponenteVortex destino);

}
