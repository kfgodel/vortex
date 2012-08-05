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
import net.gaia.vortex.http.impl.server.respuestas.RespuestaDeErrorDeCliente;
import net.gaia.vortex.http.impl.server.respuestas.RespuestaDeTexto;
import net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta clase representa el comando http que intenta crear una nueva sesión http con el servidor
 * 
 * @author D. García
 */
public class CrearSesionVortexHttp implements ComandoHttp {
	private static final Logger LOG = LoggerFactory.getLogger(CrearSesionVortexHttp.class);

	private AdministradorDeSesionesServer administradorDeSesiones;

	private String parametrosJson;
	public static final String parametrosJson_FIELD = "parametrosJson";

	/**
	 * @see net.gaia.vortex.http.external.jetty.ComandoHttp#ejecutar()
	 */
	@Override
	public RespuestaHttp ejecutar() {
		final SesionVortexHttp sesion = administradorDeSesiones.crearNuevaSesion();
		try {
			sesion.tomarParametrosInicialesDe(parametrosJson);
		} catch (final CannotTextUnserializeException e) {
			LOG.error("No se pudo interpretar el JSON para inicializar la sesion[" + sesion + "]: " + parametrosJson, e);
			return RespuestaDeErrorDeCliente.create("No es posible interpretar el JSON del mensaje: " + e.getMessage());
		}
		final String idDeSesion = sesion.getIdDeSesion();
		return RespuestaDeTexto.create(idDeSesion);
	}

	public static CrearSesionVortexHttp create(final AdministradorDeSesionesServer administrador,
			final String parametrosComoJson) {
		final CrearSesionVortexHttp comando = new CrearSesionVortexHttp();
		comando.administradorDeSesiones = administrador;
		comando.parametrosJson = parametrosComoJson;
		return comando;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(parametrosJson_FIELD, parametrosJson).toString();
	}

}
