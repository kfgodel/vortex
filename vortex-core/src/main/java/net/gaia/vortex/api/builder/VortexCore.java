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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Emisor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Identificador;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaDeMensajes;

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
	 * Crea un molecula que funciona como multiplexor pero a la entrada tiene un filtro que le
	 * permite descartar los mensajes duplicados
	 * 
	 * @param receptores
	 *            Los receptores a conectar como salidas de la molecula creada
	 * @return La molecula creada y conectada a los receptores indicados
	 */
	Compuesto<Multiplexor> multiplexarSinDuplicados(Receptor... receptores);

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
	Filtro filtrarEntradaCon(Condicion condicion, Receptor receptor);

	/**
	 * Crea un {@link Bifurcador} conectado al conector pasado y configurado para filtrar los
	 * mensajes que recibe de manera que al conector devuelto por este método sólo lleguen los
	 * mensajes que evalúan a true en la condición pasada.<br>
	 * Dicho de otra manera, devuelve un conector que filtrará los mensajes con la condicion pasada
	 * 
	 * @param conector
	 *            El conector que sera tomado como entrada para el filtro
	 * @param condicion
	 *            La condicion para filtrar los mensajes
	 * @return El conector
	 */
	Filtro filtrarSalidaDe(Conector conector, Condicion condicion);

	/**
	 * Crea un atomo bifurcador configurado con la condicion pasada como decisor del receptor
	 * destino de salida.<br>
	 * Se deberá conectar receptores en los conectores que correspondan
	 * 
	 * @param condicion
	 *            La condicion para bifurcar el camino de los mensajes
	 * @return El atomo creado
	 */
	Filtro filtroDe(Condicion condicion);

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
	Filtro filtrarMensajesDuplicadosA(Receptor receptor);

	/**
	 * Crea el bifurcador que descarta los mensajes duplicados, permitiendo recibir los mensajes
	 * sólo una vez en el conector de salida por true
	 * 
	 * @return El atomo creado
	 */
	Filtro filtroSinMensajesDuplicados();

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
	 * Crea un atomo transformador que modificará los mensajes recibidos con la transformación
	 * pasada antes de entregarselos al receptor que se indique en su conector
	 * 
	 * @param transformacion
	 *            La transformación para el transformador creado
	 * @return El atomo creado
	 */
	Transformador transformadorPara(Transformacion transformacion);

	/**
	 * Crea un {@link Selector} que permite entregar mensajes a conjuntos de receptores segun las
	 * condiciones usadas al conectarlos
	 * 
	 * @return El selector creado
	 */
	Selector selector();

	/**
	 * Crea un {@link Compuesto} con la composición de los componentes pasados.<br>
	 * Los componentes deben ser conectados entre sí (antes o despues de esta creación) para que la
	 * molecula se comporte correctamente al recibir mensajes.<br>
	 * La molecula representara a los componentes pasados como una unidad, derivandole a la entrada
	 * los mensajes recibidos y conectando a la salida los componentes conectados
	 * 
	 * @param entrada
	 *            El componente al que se enviarán todos los mensajes recibids
	 * @param salida
	 *            El componente utilizado para conectar todas las salidas
	 * @return La molecula creada
	 */
	<E extends Emisor> Compuesto<E> componer(final Receptor entrada, final E salida);

	/**
	 * Crea una conexión unidireccional desde el origen al destino. Creando un conector en origen y
	 * conectandolo al destino
	 * 
	 * @param origen
	 *            El componente desde el cual parten los mensajes
	 * @param destino
	 *            El componente al que deben llegar
	 */
	void conectarDesde(Compuesto<? extends Conectable> origen, Receptor destino);

	/**
	 * Crea un conector asincrono, a través del cual se pueden entregar mensajes al receptor
	 * indicado sin utilizar el thread actual.<br>
	 * A través de este conector se pueden cortar los bucles en redes circulares y sincronas, o
	 * paralelizar la entrega de mensajes en variso threads.<br>
	 * Para el procesamiento de los mensajes de utilizara el procesador de tareas de este builder
	 * que tiene un pool de threads propios
	 * 
	 * @param receptor
	 *            El receptor que será conectado como destino de los mensajes enviados desde el
	 *            conector creado
	 * @return El conector asincronico creado y conectado al receptor
	 */
	Conector asincronizar(Receptor receptor);

	/**
	 * El procesador interno de tareas que representa un pool de threads utilizado para procesar las
	 * entregas de mensaje asíncronas.<br>
	 * 
	 * @return El procesador utilizado por este builder
	 */
	TaskProcessor getProcessor();

	/**
	 * Crea el componente identificador de mensajes que permite discriminar duplicados y mensajes
	 * propios.<br>
	 * Este componente es necesario en topologías desconocidas o que tienen bucles en las conexiones
	 * 
	 * @return El componente creado con ID propio
	 */
	Identificador identificador();

	/**
	 * Crea un identificador de componente para utilizar en un nuevo componente creado
	 * 
	 * @return El identificador para utilizar con mensajes identificados
	 */
	IdDeComponenteVortex crearIdDeComponente();

	/**
	 * Crea una memoria de mensajes para utilizar en un componente creado
	 * 
	 * @return La memoria que registra los mensajes recibidos
	 */
	MemoriaDeMensajes crearMemoriaDeMensajes();

	/**
	 * Crea un nuevo generador de identificadores para mensajes que se basa en un identificador de
	 * componente como origen
	 * 
	 * @param idAsignado
	 *            El id del componente generador
	 * @return El generador creado
	 */
	GeneradorDeIdsDeMensajes crearGeneradorDeIdsParaMensajes(IdDeComponenteVortex idAsignado);

	/**
	 * Crea un componente distribuidor de mensajes que a traves de sus terminales permite conectar
	 * varios componentes sin tener duplicados
	 * 
	 * @return El componente creado
	 */
	Distribuidor distribuidor();

}
