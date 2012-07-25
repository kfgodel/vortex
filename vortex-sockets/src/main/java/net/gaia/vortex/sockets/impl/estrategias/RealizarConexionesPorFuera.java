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
package net.gaia.vortex.sockets.impl.estrategias;

import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa la estrategia de conexión utilizable por clientes socket para conectar el
 * nexo creado por fuera. <br>
 * Al utilizar esta estrategia la conexión y desconexión debe realizar con cuidado tomando en cuenta
 * que el ciclo de vida del socket no acompaña el de las conexiones a la red vortex
 * 
 * @author D. García
 */
public class RealizarConexionesPorFuera implements EstrategiaDeConexionDeNexos {
	private static final Logger LOG = LoggerFactory.getLogger(RealizarConexionesPorFuera.class);

	private static final WeakSingleton<RealizarConexionesPorFuera> ultimaReferencia = new WeakSingleton<RealizarConexionesPorFuera>(
			DefaultInstantiator.create(RealizarConexionesPorFuera.class));

	public static RealizarConexionesPorFuera getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos#onNexoCreado(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */
	@Override
	public void onNexoCreado(final Nexo nuevoNexo) {
		LOG.debug("No se realizara accion interna de conexion del nexo creado[{}]", nuevoNexo);
	}

	/**
	 * @see net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos#onNexoCerrado(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */
	@Override
	public void onNexoCerrado(final Nexo nexoCerrado) {
		LOG.debug("No se realizara accion interna de desconexion del nexo cerrado[{}]", nexoCerrado);
	}

}
