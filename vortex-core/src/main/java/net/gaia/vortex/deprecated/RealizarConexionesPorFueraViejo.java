/**
 * 20/06/2012 18:42:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase representa la estrategia de conexión utilizable por clientes socket para conectar el
 * nexo creado por fuera. <br>
 * Al utilizar esta estrategia la conexión y desconexión debe realizar con cuidado tomando en cuenta
 * que el ciclo de vida del socket no acompaña el de las conexiones a la red vortex
 * 
 * @author D. García
 */
@Deprecated
public class RealizarConexionesPorFueraViejo extends WeakSingletonSupport implements EstrategiaDeConexionDeNexosViejo {
	private static final Logger LOG = LoggerFactory.getLogger(RealizarConexionesPorFueraViejo.class);

	private static final WeakSingleton<RealizarConexionesPorFueraViejo> ultimaReferencia = new WeakSingleton<RealizarConexionesPorFueraViejo>(
			DefaultInstantiator.create(RealizarConexionesPorFueraViejo.class));

	public static RealizarConexionesPorFueraViejo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo#conectarNodo(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */

	public void onNexoCreado(final NexoViejo nuevoNexo) {
		LOG.debug("No se realizara accion interna de conexion del nexo creado[{}]", nuevoNexo);
	}

	/**
	 * @see net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo#onNexoCerrado(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */

	public void onNexoCerrado(final NexoViejo nexoCerrado) {
		LOG.debug("No se realizara accion interna de desconexion del nexo cerrado[{}]", nexoCerrado);
	}

}
