/**
 * 20/08/2013 01:03:55 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.atomos;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.MonoEmisor;

/**
 * Esta interfaz representa el componente que permite observar los mensajes recibidos antes de
 * enviarlos al siguiente componentes.<br>
 * Para observar los mensajes se utilizar un receptor que hace las veces de observador
 * 
 * @author D. García
 */
public interface Observador extends Nodo, MonoEmisor {

	/**
	 * Devuelve el componente que será utilizado como observador de los mensajes recibidos
	 * 
	 * @return El componente que recibirá todos los mensajes antes que la salida
	 */
	Receptor getObservador();

	/**
	 * Establece el componente que hará de observador de los mensajes.<br>
	 * Recibiendo cada mensaje antes que el receptor de salida
	 * 
	 * @param el
	 *            componente observador
	 */
	void setObservador(Receptor observador);
}
