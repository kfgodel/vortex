/**
 * 19/08/2013 19:14:00 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.proto;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa el componente vortex que permite conectar con otros receptores para
 * generar rutas de mensajes.<br>
 * Al recibir el mensaje, el conector lo deriva al receptor conectado
 * 
 * @author D. García
 */
public interface Conector extends Receptor {

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

	/**
	 * Devuelve el receptor al que este conector está conectado
	 * 
	 * @return Una instancia receptora de los mensajes de este conector
	 */
	public Receptor getDestino();
}
