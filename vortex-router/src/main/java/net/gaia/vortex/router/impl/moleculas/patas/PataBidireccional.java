/**
 * 22/12/2012 19:07:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.patas;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;

/**
 * Esta clase representa la interfaz que ofrece una pata de conexión bidireccional para un
 * {@link NodoBidireccional}
 * 
 * @author D. García
 */
public interface PataBidireccional extends Receptor {

	/**
	 * Indica si esta pata está conectada al nodo indicado como nodo remoto
	 * 
	 * @param nodo
	 *            El nodo a evaluar
	 * @return true si esta pata enviará los mensajes al nodo indicado
	 */
	boolean tieneComoNodoRemotoA(Receptor nodo);

	/**
	 * Devuelve la parte de las condiciones como conjunto que administra esta pata
	 * 
	 * @return La parte que es modificada por esta pata
	 */
	ParteDeCondiciones getParteDeCondicion();

	/**
	 * Devuelve el nodo al que esta pata está conectado
	 * 
	 * @return El nodo destino de esta pata
	 */
	Receptor getNodoRemoto();

	/**
	 * Modifica el estado de esta pata avisando que cambio el filtro utilizado para recibir mensajes
	 * 
	 * @param nuevoFiltro
	 *            La nueva condicion
	 */
	public void actualizarFiltroDeEntrada(final Condicion nuevoFiltro);

	/**
	 * Devuelve el identificador local de esta pata
	 * 
	 * @return El identificador con el que esta pata se discrimina en este nodo
	 */
	public Long getIdLocal();

	/**
	 * Devuelve el identificador remoto de esta pata
	 * 
	 * @return El identificador que el otro nodo le asigno a esta pata
	 */
	public Long getIdRemoto();

	/**
	 * El nodo local a esta pata
	 * 
	 * @return El componente creador de esta pata
	 */
	public NodoBidireccional getNodoLocal();
}
