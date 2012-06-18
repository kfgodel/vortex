/**
 * 17/06/2012 14:59:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.moleculas.ruteo;

import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.forward.Nexo;

/**
 * Esta interfaz representa un hub que internamente tiene un nexo para procesar los mensajes
 * recibidos antes de forwardearlo a los destinos conectados
 * 
 * @author D. García
 */
@Molecula
public interface NodoHubConNexoCore extends NodoHub {

	/**
	 * Establece el nexo que actúa como core de la lógica de este hub, permitiendo definir
	 * comportamiento variable en este hub al recibir mensajes.<br>
	 * El nexo pasado recibirá el receptor al que debe delegarle los mensajes mediante una llamada a
	 * {@link Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)}, por lo que su destino
	 * actual se perderá al ser parte de este hub
	 * 
	 * @param coreDelHub
	 *            El nexo que define el comportamiento de este hub ante los mensajes recibidos
	 */
	public void setNexoCore(Nexo coreDelHub);

	/**
	 * Devuelve el nexo que actúa como core de este hub y que define su comportamiento ante los
	 * mensajes recibidos
	 * 
	 * @return El nexo utilizado como lógica de este hub
	 */
	public Nexo getNexoCore();

}
