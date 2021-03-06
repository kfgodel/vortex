/**
 * 20/08/2013 01:16:47 Copyright (C) 2013 Darío L. García
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
import net.gaia.vortex.api.basic.emisores.MultiEmisor;

/**
 * Esta interfaz representa un componente vortex que entrega cada mensaje recibido a sus multiples
 * salidas, permitiendo agrupar un conjunto de receptores en un unico receptor.
 * 
 * @author D. García
 */
public interface Multiplexor extends Nodo, MultiEmisor {

	/**
	 * Crea una nueva conexión con el destino indicado, agregandola a las conexiones de este
	 * multiplexor
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Desconecta el receptor indicado quitandolo de las conexiones de este multiplexor
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Desconecta todos los receptores de este multiplexor.<br>
	 * Los mensajes recibidos serán descartados al no tener receptores
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();
}
