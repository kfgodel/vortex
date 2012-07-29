/**
 * 25/07/2012 12:52:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server.comandos;

import net.gaia.vortex.http.external.jetty.ComandoHttp;
import net.gaia.vortex.http.external.jetty.RespuestaHttp;
import net.gaia.vortex.http.impl.server.respuestas.RespuestaDeTexto;
import net.gaia.vortex.http.sesiones.AdministradorDeSesiones;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comando http que intenta crear una nueva sesión http con el servidor
 * 
 * @author D. García
 */
public class CrearSesionVortexHttp implements ComandoHttp {

	private AdministradorDeSesiones administradorDeSesiones;

	/**
	 * @see net.gaia.vortex.http.external.jetty.ComandoHttp#ejecutar()
	 */
	@Override
	public RespuestaHttp ejecutar() {
		final SesionVortexHttp sesion = administradorDeSesiones.crearNuevaSesion();
		final String idDeSesion = sesion.getIdDeSesion();
		return RespuestaDeTexto.create(idDeSesion);
	}

	public static CrearSesionVortexHttp create(final AdministradorDeSesiones administrador) {
		final CrearSesionVortexHttp comando = new CrearSesionVortexHttp();
		comando.administradorDeSesiones = administrador;
		return comando;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

}
