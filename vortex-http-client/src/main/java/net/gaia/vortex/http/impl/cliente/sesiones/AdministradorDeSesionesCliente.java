/**
 * 29/07/2012 15:34:11 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente;

/**
 * Esta interfaz define el contrato esperado del administrador de sesiones del lado del cliente
 * 
 * @author D. García
 */
public interface AdministradorDeSesionesCliente {

	/**
	 * Agrega la sesión pasada como parte de las sesiones administradas por esta instancia.<br>
	 * Al agregar la sesión se creará un nexoHttp asociado y comenzará la comunicación con el
	 * servidor
	 * 
	 * @param sesionCliente
	 *            La sesión creada y lista para iniciar comunicaciones con el servidor
	 */
	void abrirSesion(SesionVortexHttpEnCliente sesionCliente);

	/**
	 * Cierra la sesión pasada y desconecta el nexo http asociado
	 * 
	 * @param sesionAbierta
	 *            La sesión a cerrar con el servidor
	 */
	void cerrarSesion(SesionVortexHttpEnCliente sesionAbierta);

}
