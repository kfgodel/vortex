/**
 * 27/07/2012 22:08:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

/**
 * Esta interfaz define los métodos que debe implementar un listener que es notificado de los
 * eventos de creación y destrucción de sesiones http
 * 
 * @author D. García
 */
public interface ListenerDeSesionesHttp {

	/**
	 * Notifica la creación de la sesión pasada
	 * 
	 * @param sesionCreada
	 *            La sesión creada que se utilizará a futuro para comunicaciones por un cliente
	 */
	public void onSesionCreada(SesionVortexHttp sesionCreada);

	/**
	 * La sesión a eliminar del administrador
	 * 
	 * @param sesionDestruida
	 *            La sesión que ya no se usará para comunicaciones
	 */
	public void onSesionDestruida(SesionVortexHttp sesionDestruida);

}
