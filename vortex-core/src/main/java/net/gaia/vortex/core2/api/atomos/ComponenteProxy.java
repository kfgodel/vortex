/**
 * 13/06/2012 00:27:41 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa un componente vortex que puede delegar el mensaje recibido a otro
 * componente pero en el medio puede realizar alguna acción.<br>
 * A través de esta interfaz la implementaciones permite definir distintos comportamientos posibles
 * entre dos componentes al recibir un mensaje en uno de ellos
 * 
 * @author D. García
 */
@Atomo
public interface ComponenteProxy extends ComponenteVortex {

	/**
	 * Establece cuál es el delegado a utilizar por esta instancia cuando reciba un mensaje
	 * 
	 * @param delegado
	 *            El componente delegado que recibirá el mensaje si esta instancia lo decide
	 */
	public void setDelegado(ComponenteVortex delegado);

}
