/**
 * 22/01/2013 17:12:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.moleculas.condicional;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.condiciones.Condicion;

/**
 * Esta interfaz representa un componente vortex de muchas salidas que permite asociar una condición
 * a cada salida. Al recibir un mensaje este sólo saldrá por las salidas que cumplan su condición
 * 
 * @author D. García
 */
public interface Selector extends Multiplexor {

	/**
	 * En un {@link Selector} al conectar con un receptor sin indicar condición se asume siempre
	 * true para esa salida (equivalente al multiplexor)
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(Receptor destino);

	/**
	 * Conecta al destino indicado utilizando la condición pasada para filtrar los mensajes que el
	 * destino recibirá
	 * 
	 * @param destino
	 *            El destino a conectar
	 * @param condicion
	 *            La condición que se usará para filtrar los mensajes. El destino sólo recibirá los
	 *            mensajes que esta condición evalue a true, o indecidible
	 */
	public void conectarCon(Receptor destino, Condicion condicion);

	/**
	 * Modifica la condición utilizada para filtrar los mensajes del destino.<br>
	 * Nada sucede si el destino no está conectado desde este selector
	 * 
	 * @param destino
	 *            El destino al que se le modificará la condición
	 * @param nuevaCondicion
	 *            La condición que reemplazará la otra
	 */
	public void modificarCondicionPara(Receptor destino, Condicion nuevaCondicion);

}
