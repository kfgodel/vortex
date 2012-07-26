/**
 * 25/07/2012 22:36:44 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa una sesión vortex http mantenida en memoria
 * 
 * @author D. García
 */
public class SesionEnMemoria implements SesionVortexHttp {

	private String id;

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#recibirDelCliente(java.lang.String)
	 */
	@Override
	public void recibirDelCliente(final String mensajesComoJson) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#obtenerParaElCliente()
	 */
	@Override
	public String obtenerParaElCliente() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	public static SesionEnMemoria create(final String idDeSesion) {
		final SesionEnMemoria sesion = new SesionEnMemoria();
		sesion.id = idDeSesion;
		return sesion;
	}
}
