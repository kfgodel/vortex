/**
 * 07/12/2012 17:57:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.impl;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.simulador.ComponenteNombreable;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.ConectarBidi;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.ConectarUni;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.DesconectarBidi;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.DesconectarUni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la base de comportamiento ya implementado en vortex respecto de la conexión
 * a nodos y el registro de mensajes enviados y recibidos.<br>
 * Los componentes que representen nodos heredarán de esta clase para tener la base de vortex ya
 * definida
 * 
 * @author D. García
 */
public abstract class ComponenteNodo extends ComponenteNombreable implements NodoSimulacion {
	private static final Logger LOG = LoggerFactory.getLogger(ComponenteNodo.class);

	private List<Mensaje> recibidos;
	public static final String recibidos_FIELD = "recibidos";

	public List<Mensaje> getRecibidos() {
		if (recibidos == null) {
			recibidos = new ArrayList<Mensaje>();
		}
		return recibidos;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#simularConexionCon(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void simularConexionCon(final NodoSimulacion otro) {
		procesar(ConectarUni.create(this, otro));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#simularConexionBidi(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void simularConexionBidi(final NodoSimulacion otro) {
		procesar(ConectarBidi.create(this, otro));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#simularDesconexionUniDe(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void simularDesconexionUniDe(final NodoSimulacion otro) {
		procesar(DesconectarUni.create(this, otro));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#simularDesconexionBidiDe(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void simularDesconexionBidiDe(final NodoSimulacion otroConectado) {
		procesar(DesconectarBidi.create(this, otroConectado));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#recibirMensaje(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	public void recibirMensaje(final Mensaje mensaje) {
		if (getRecibidos().contains(mensaje)) {
			LOG.debug("  Rechazando mensaje[{}] recibido en [{}] por figurar como ya recibido",
					mensaje.getIdDeMensaje(), this.getNombre());
			return;
		}
		// Es un mensaje nuevo
		getRecibidos().add(mensaje);
		evento_recibirMensajeEnNodo(mensaje);
	}

	/**
	 * Invocado al recibir un mensaje en este nodo
	 */
	protected abstract void evento_recibirMensajeEnNodo(Mensaje mensaje);

}
