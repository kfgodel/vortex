/**
 * 19/08/2013 20:07:09 Copyright (C) 2013 Darío L. García
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

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa un componente vortex que bifurca el camino del mensaje en función de una
 * {@link Condicion} aplicada sobre él.<br>
 * Si la condición es verdadera seguirá un camino, y si es falsa otro. Es descartado si la
 * evaluación de la condición falla
 * 
 * @author D. García
 */
public interface Bifurcador extends Filtro {

	/**
	 * Establece el receptor de los mensajes en caso de que la condición evalúe a false.<br>
	 * Los mensajes evaluados a false serán entregados al receptor indicado.
	 * 
	 * @return El conector para casos false
	 */
	public void conectarPorFalseCon(Receptor receptorPorFalse);

	/**
	 * Desconecta el receptor de los mensajes que pasan la condición con false.<br>
	 * Los mensajes que evaluen a false serán entregados al {@link ReceptorNulo}
	 */
	public void desconectarPorFalse();

	/**
	 * Desconecta este bifurcador de las dos salidas (tanto true como false)
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();

	/**
	 * Desconecta el receptor indicado de la salida de este componente.<br>
	 * Si el receptor pasado esta conectado tanto a true como false, será desconectado de ambas
	 * salidas
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);
}
