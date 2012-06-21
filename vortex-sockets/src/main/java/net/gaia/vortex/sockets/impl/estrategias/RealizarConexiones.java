/**
 * 19/06/2012 09:28:40 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.sockets.api.EstrategiaDeConexionDeNexos;
import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una estrategia de conexión de {@link NexoSocket} a la red vortex.
 * Utilizando un nodo conocido, esta clase conecta los nuevos nexos al nodo, y los desconecta al
 * cerrarse
 * 
 * @author D. García
 */
public class RealizarConexiones implements EstrategiaDeConexionDeNexos {

	private Nodo nodoConocido;
	public static final String hubConocido_FIELD = "nodoConocido";

	/**
	 * @see net.gaia.vortex.sockets.api.EstrategiaDeConexionDeNexos#onNexoSocketCreado(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */
	@Override
	public void onNexoSocketCreado(final NexoSocket nuevoNexo) {
		Loggers.RUTEO.debug("Conectando nuevo nexo[{}] con el nodo[{}]", nuevoNexo, nodoConocido);
		nuevoNexo.conectarCon(nodoConocido);
		nodoConocido.conectarCon(nuevoNexo);
	}

	/**
	 * @see net.gaia.vortex.sockets.api.EstrategiaDeConexionDeNexos#onNexoSocketCerrado(net.gaia.vortex.sockets.impl.moleculas.NexoSocket)
	 */
	@Override
	public void onNexoSocketCerrado(final NexoSocket nexoCerrado) {
		Loggers.RUTEO.debug("Des-Conectando nexo[{}] del nodo[{}]", nexoCerrado, nodoConocido);
		nodoConocido.desconectarDe(nexoCerrado);
		nexoCerrado.desconectarDe(nodoConocido);
	}

	public static RealizarConexiones con(final Nodo nodo) {
		final RealizarConexiones conector = new RealizarConexiones();
		conector.nodoConocido = nodo;
		return conector;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(hubConocido_FIELD, nodoConocido).toString();
	}
}
