/**
 * 22/08/2013 01:12:59 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.builder;

import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.core.api.transformaciones.Transformacion;

/**
 * Esta interfaz define el contrato esperable de un builder de nodos vortex, el cual permite obtener
 * las instancias de componentes comunes para armar la red de un sistema, sin requerir demasiado
 * código de setup
 * 
 * @author D. García
 */
public interface VortexCore {

	/**
	 * Crea un atomo secuenciador para los componentes indicados, de manera que el receptor
	 * delegado, reciba el mensaje primero y una vez que terminó se le envía al receptor salida.<br>
	 * 
	 * @param delegado
	 *            El receptor que recibirá el mensaje primero
	 * @param salida
	 *            El receptor que se conectará a la salida y recibirá el mensaje después
	 * @return El secuenciador creado y conectado
	 */
	Secuenciador secuenciar(Receptor delegado, Receptor salida);

	/**
	 * Crea un atomo secuenciador que entregará los mensajes recibidos primero al receptor indicado
	 * y luego al que se conecte a la salida
	 * 
	 * @param delegado
	 *            El receptor que recibirá los mensajes primero
	 * @return El secuenciador creado
	 */
	Secuenciador secuenciadorDe(Receptor delegado);

	/**
	 * Crea un atomo multiplexor con los receptores indicados ya conectados como salidas.<br>
	 * Si no se indican receptores, el multiplexor deberá ser conectado después
	 * 
	 * @param receptores
	 *            Los receptores a conectar como salidas
	 * @return El multiplexor creado y conectado
	 */
	Multiplexor multiplexar(Receptor... receptores);

	/**
	 * Crea un atomo bifurcador de mensajes que utiliza la condición pasada para entregar a un
	 * receptor o al otro, según el resultado de evaluación de cada mensaje recibido
	 * 
	 * @param condicion
	 *            La condición utilizada para elegir el camino
	 * @param receptorPorTrue
	 *            El receptor para los mensajes que devuelven true
	 * @param receptorPorFalse
	 *            El receptor para los mensajes que devuelven false
	 * @return El bifurcador creado y conectado a las salidas
	 */
	Bifurcador bifurcarSi(Condicion condicion, Receptor receptorPorTrue, Receptor receptorPorFalse);

	/**
	 * Crea un atomo bifurcador configurado para filtrar los mensajes de manera que al receptor
	 * indicado, sólo lleguen los mensajes que evalúan a true en la condicion pasada.<br>
	 * Dicho de otra manera, descarta todos los mensajes que devuelvan false en la condición.
	 * 
	 * @param condicion
	 *            La condición a evaluar en cada mensaje
	 * @param receptor
	 *            El receptor que recibirá los mensajes que pasaron con true
	 * @return El atomo creado y conectado
	 */
	Bifurcador filtrarCon(Condicion condicion, Receptor receptor);

	/**
	 * Crea un atomo bifurcador configurado con la condicion pasada como decisor del receptor
	 * destino de salida.<br>
	 * Se deberá conectar receptores en los conectores que correspondan
	 * 
	 * @param condicion
	 *            La condicion para bifurcar el camino de los mensajes
	 * @return El atomo creado
	 */
	Bifurcador filtroDe(Condicion condicion);

	/**
	 * Crea un atomo transformador que utilizará la transformación indicada para modificar los
	 * mensajes antes de entregarlos al receptor pasado
	 * 
	 * @param transformacion
	 *            La transformación a utilizar para alterar los mensajes
	 * @param receptor
	 *            El receptor de los mensajes modificados
	 * @return El atomo transformador creado y conectado
	 */
	Transformador transformarCon(Transformacion transformacion, Receptor receptor);

	/**
	 * Crea un atomo bifurcador que descartará los mensajes duplicados, enviando sólo una vez los
	 * mensajes al receptor indicado.<br>
	 * Para discriminar los mensajes se utiliza el ID de cada uno, y una memoria que registra cierta
	 * cantidad
	 * 
	 * @param receptor
	 *            El receptor que recibirá los mensajes una sola vez
	 * @return El atomo creado y conectado
	 */
	Bifurcador sinDuplicadosPara(Receptor receptor);

	/**
	 * Crea el bifurcador que descarta los mensajes duplicados, permitiendo recibir los mensajes
	 * sólo una vez en el conector de salida por true
	 * 
	 * @return El atomo creado
	 */
	Bifurcador filtroSinDuplicados();

	/**
	 * Crea un atomo transformador que modificará los mensajes recibidos con la transformación
	 * pasada antes de entregarselos al receptor que se indique en su conector
	 * 
	 * @param transformacion
	 *            La transformación para el transformador creado
	 * @return El atomo creado
	 */
	Transformador transformadorPara(Transformacion transformacion);

}
