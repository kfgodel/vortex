/**
 * 29/07/2012 15:48:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.sesiones;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el administrador de sesiones del cliente en memoria
 * 
 * @author D. García
 */
public class AdministradorClienteEnMemoria implements AdministradorDeSesionesCliente {

	private ConcurrentLinkedQueue<SesionVortexHttp> sesiones;
	public static final String sesiones_FIELD = "sesiones";

	private ListenerDeSesionesHttp listener;

	public static AdministradorClienteEnMemoria create(final ListenerDeSesionesHttp listener) {
		final AdministradorClienteEnMemoria administrador = new AdministradorClienteEnMemoria();
		administrador.sesiones = new ConcurrentLinkedQueue<SesionVortexHttp>();
		administrador.listener = listener;
		return administrador;
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente#abrirSesion(net.gaia.vortex.http.sesiones.SesionClienteEnMemoria)
	 */
	@Override
	public void abrirSesion(final SesionVortexHttpEnCliente sesionCliente) {
		sesionCliente.iniciarComunicacion();
		sesiones.add(sesionCliente);
		listener.onSesionCreada(sesionCliente);
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente#cerrarSesion(net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente)
	 */
	@Override
	public void cerrarSesion(final SesionVortexHttpEnCliente sesionCerrada) {
		listener.onSesionDestruida(sesionCerrada);
		sesiones.remove(sesionCerrada);
		sesionCerrada.detenerComunicacion();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(sesiones_FIELD, sesiones).toString();
	}

}
