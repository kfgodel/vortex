/**
 * 12/02/2012 20:38:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.inter;

import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la interconexión de dos nodos.<br>
 * La interconexión se produce desde uno hacia otro, con lo que existe una noción de localidad. El
 * nodo que inicia la interconexión es local y es el que sabe que se conecta a otro nodo. El nodo
 * remoto es ignorante si la conexión fue abierta por un nodo o un cliente
 * 
 * @author D. García
 */
public class InterconexionDeNodos {
	private static final Logger LOG = LoggerFactory.getLogger(InterconexionDeNodos.class);

	private NodoVortex nodoLocal;
	public static final String nodoLocal_FIELD = "nodoLocal";

	private SesionVortex sesionLocal;

	private NodoVortex nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private SesionVortex sesionRemota;

	public static InterconexionDeNodos create(final NodoVortex nodoOrigen, final NodoVortex nodoDestino) {
		final InterconexionDeNodos interconexion = new InterconexionDeNodos();
		interconexion.nodoRemoto = nodoDestino;
		interconexion.nodoLocal = nodoOrigen;
		return interconexion;
	}

	/**
	 * Realiza la interconexión de los nodos creando una sesión en cada uno que forwardeará los
	 * mensajes al otro
	 */
	public void interconectar() {
		LOG.debug("Creando interconexión desde nodo[{}] hacia nodo[{}]", nodoLocal, nodoRemoto);
		final HandlerTemporal handlerTemporalParaSesionRemota = HandlerTemporal.create();
		sesionRemota = nodoRemoto.crearNuevaSesion(handlerTemporalParaSesionRemota);
		LOG.debug("Sesión remota[{}] creada con handler temporal hacia nodo[{}]", sesionRemota, nodoRemoto);

		final HandlerDeInterconexion handlerParaSesionLocal = HandlerDeInterconexion.create(sesionRemota);
		sesionLocal = nodoLocal.crearNuevaSesion(handlerParaSesionLocal);
		LOG.debug("Sesión local[{}] creada con handler definitivo desde nodo[{}]", sesionLocal, nodoLocal);

		final HandlerDeInterconexion handlerParaSesionRemota = HandlerDeInterconexion.create(sesionLocal);
		handlerTemporalParaSesionRemota.setHandlerReemplazo(handlerParaSesionRemota);
		LOG.debug("Sesión remota[{}] interconectada con handler definitivo", sesionRemota);
	}

	/**
	 * Desconecta las sesiones de los nodos de manera que no sea posible la circulación de mensajes
	 * entre nodos
	 */
	public void desconectar() {
		LOG.debug("Eliminando interconexión desde nodo[{}] hacia nodo[{}]", nodoLocal, nodoRemoto);
		sesionLocal.cerrar();
		sesionRemota.cerrar();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(nodoLocal_FIELD, nodoLocal).add(nodoRemoto_FIELD, nodoRemoto)
				.toString();
	}
}
