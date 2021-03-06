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

import java.util.List;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa el componente que permite secuenciar los mensajes recibidos
 * estableciendo un orden de entrega. Primero se le pasa el mensaje al delegado y cuando este
 * termina se continúa por el conector de salida.<br>
 * 
 * @author D. García
 */
public interface Secuenciador extends Nodo {

	/**
	 * Conecta el receptor indicado como salida de este secuenciador, entregando el mensaje recibido
	 * después de pasarlo al delegado.<br>
	 * Este método es equivalente al {@link #conectarReceptorFinal(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	void conectarCon(Receptor destino);

	/**
	 * Desconecta este secuenciador del receptor final Y del delegado
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	void desconectar();

	/**
	 * Desconecta el receptor indicado ya sea delegado o receptor final
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	void desconectarDe(Receptor destino);

	/**
	 * Conecta el receptor pasado como receptor final de los mensajes recibidos por el secuenciador.<br>
	 * El receptor recibirá los mensajes después del delegado
	 * 
	 * @param receptorFinal
	 *            El receptor conectado al final de la cadena
	 */
	void conectarReceptorFinal(Receptor receptorFinal);

	/**
	 * Devuelve el receptor que recibe los mensajes al final de la secuencia
	 * 
	 * @return El receptor final de los mensajes
	 */
	Receptor getReceptorFinal();

	/**
	 * Desconecta de este secuenciador el receptor final, enviando todos los mensajes al
	 * {@link ReceptorNulo}
	 */
	void desconectarReceptorFinal();

	/**
	 * Conecta el receptor medio que recibirá los mensajes antes que el receptor final
	 * 
	 * @param receptorMedio
	 */
	void conectarReceptorMedio(Receptor receptorMedio);

	/**
	 * Devuelve el componente que será utilizado como observador de los mensajes recibidos,
	 * recibiendo antes que el receptor final los mensajes
	 * 
	 * @return El componente que recibirá todos los mensajes antes que la salida
	 */
	Receptor getReceptorMedio();

	/**
	 * Desconecta el receptor medio, derivando los mensajes al {@link ReceptorNulo} antes de
	 * entregarlos al final
	 */
	void desconectarReceptorMedio();

	/**
	 * Devuelve los dos receptores conectados, el medio y el final, en ese orden
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados();
}
