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
import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Identificador;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaDeMensajes;

/**
 * Esta interfaz define el contrato un builder de nodos vortex core, el cual permite obtener las
 * instancias de componentes basico para armar la red de un sistema.<br>
 * A través de instancias de esta clase se pueden crear componentes inicializados correctamente sin
 * requerir todos los parámetros
 * 
 * @author D. García
 */
public interface VortexCore {

	/**
	 * Crea un atomo secuenciador para los componentes indicados, de manera que el receptor medio,
	 * reciba el mensaje primero y una vez que terminó se le envía al receptor final.<br>
	 * 
	 * @param receptorMedio
	 *            El receptor que recibirá el mensaje primero
	 * @param receptorFinal
	 *            El receptor que se conectará a la salida y recibirá el mensaje después
	 * @return El secuenciador creado y conectado
	 */
	Secuenciador secuenciar(Receptor receptorMedio, Receptor receptorFinal);

	/**
	 * Crea un atomo secuenciador sin indicar el receptor final que entregará los mensajes recibidos
	 * primero al receptor indicado y luego al que se conecte a la salida
	 * 
	 * @param receptorMedio
	 *            El receptor que recibirá los mensajes primero
	 * @return El secuenciador creado
	 */
	Secuenciador secuenciadorDe(Receptor receptorMedio);

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
	 * Crea una molecula que funciona como multiplexor pero a la entrada tiene un filtro que le
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
	 * Crea un atomo filtro configurado para filtrar los mensajes de manera que al receptor indicado
	 * sólo lleguen los mensajes que evalúan a true en la condicion pasada.<br>
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
	 * Crea un atomo filtro conectado como salida del conector pasado y configurado para filtrar los
	 * mensajes que recibe de manera que en su salida sólo envie los mensajes que evalúan a true en
	 * la condición pasada.<br>
	 * Dicho de otra manera, conecta y devuelve un filtro que filtrará los mensajes con la condicion
	 * pasada
	 * 
	 * @param conector
	 *            El conector que sera tomado como entrada para el filtro
	 * @param condicion
	 *            La condicion para filtrar los mensajes
	 * @return El atomo filtro creado
	 */
	Filtro filtrarSalidaDe(Conectable conector, Condicion condicion);

	/**
	 * Crea un atomo filtro configurado con la condicion pasada como filtro de mensajes.<br>
	 * 
	 * @param condicion
	 *            La condicion para bifurcar el camino de los mensajes
	 * @return El atomo creado
	 */
	Filtro filtroDe(Condicion condicion);

	/**
	 * Crea un atomo filtro que descartará los mensajes duplicados, enviando sólo una vez los
	 * mensajes al receptor indicado.<br>
	 * Para discriminar los mensajes se utiliza el ID de cada uno, y una memoria que registra cierta
	 * cantidad
	 * 
	 * @param receptor
	 *            El receptor que recibirá los mensajes una sola vez
	 * @return El atomo filtro creado con memoria y conectado al receptor indicado
	 */
	Filtro filtrarMensajesDuplicadosA(Receptor receptor);

	/**
	 * Crea el filtro que descarta los mensajes duplicados, permitiendo recibir los mensajes sólo
	 * una vez en el conector de salida por true
	 * 
	 * @return El atomo creado con memoria de mensajes por id
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
	 * pasada antes de entregarselos al receptor que se conecte
	 * 
	 * @param transformacion
	 *            La transformación para el transformador creado
	 * @return El atomo creado
	 */
	Transformador transformadorPara(Transformacion transformacion);

	/**
	 * Crea un {@link Selector} que permite entregar mensajes a conjuntos de receptores segun las
	 * condiciones usadas para cada uno
	 * 
	 * @return El selector creado
	 */
	Selector selector();

	/**
	 * Crea un componente {@link Compuesto} con los componentes pasados.<br>
	 * Este componente permite agrupar una red de componentes en un sólo elemento. Se utilizará el
	 * componente entrada para recibir los mensajes, y el componente salida para las conexiones a
	 * otros receptores
	 * 
	 * @param entrada
	 *            El componente al que se enviarán todos los mensajes recibids
	 * @param salida
	 *            El componente utilizado para conectar todas las salidas
	 * @return La molecula creada
	 */
	<E extends Conectable> Compuesto<E> componer(final Receptor entrada, final E salida);

	/**
	 * Crea una conexión unidireccional desde el origen al destino. Utilizando la salida del origen
	 * para la conexión al destino
	 * 
	 * @param origen
	 *            El componente desde el cual parten los mensajes
	 * @param destino
	 *            El componente al que deben llegar
	 */
	void conectarDesde(Compuesto<? extends Conectable> origen, Receptor destino);

	/**
	 * Crea un conector asincrono, que independiza el thread que entrega el mensaje del que lo
	 * recibe. A través de este conector se pueden entregar mensajes al receptor indicado sin
	 * utilizar el thread actual.<br>
	 * Este conector permite cortar los bucles en redes circulares y sincronas, o paralelizar la
	 * entrega de mensajes en varios threads.<br>
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
	 * Este componente es necesario en topologías desconocidas o que tienen bucles en las
	 * conexiones.<br>
	 * El componente creado utiliza una transformación para asignar IDs a los mensajes salientes, y
	 * un filtro interno para descartar duplicados
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
	 * Crea una memoria de mensajes para utilizar en componentes que requieran reconocer mensajes
	 * duplicados por ID
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
	 * Crea un componente distribuidor de mensajes que a traves de sus terminales permite
	 * interconectar varios componentes entre sí, sin entregar el mensaje recibido a su emisor.<br>
	 * A través de este componente se puede armar una topología sin duplciados y sin Ids en mensajes
	 * 
	 * @return El componente creado
	 */
	Distribuidor distribuidor();

	/**
	 * Crea un componente conector sincrono que permite unir emisor con receptor.<br>
	 * Este conector utiliza el thread actual para entregar el mensaje recibido
	 * 
	 * @return El componente creado
	 */
	Conector conector();

	/**
	 * Crea un conector asíncrono que al recibir los mensajes utiliza un thread en background para
	 * entregarselo a su conectado.<br>
	 * Este conector permite cortar los bucles en redes circulares y sincronas, o paralelizar la
	 * entrega de mensajes en varios threads.<br>
	 * Para el procesamiento de los mensajes de utilizara el procesador de tareas de este builder
	 * que tiene un pool de threads propios
	 * 
	 * @return El conector asicrono creado
	 */
	Conector conectorAsicrono();

	/**
	 * Crea un componente terminal que permite abstraer una parte de la red como una entidad de
	 * entrada y salida de mensajes, y que puede ser conectada a otras terminales
	 * 
	 * @return La terminal creada
	 */
	Terminal terminal();

}
