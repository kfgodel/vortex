/**
 * 25/02/2012 19:18:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.http.sessions;

import com.google.common.base.Objects;

/**
 * Esta clase representa la configuracion disponible para modificar las opciones de una sesión http
 * 
 * @author D. García
 */
public class ConfiguracionDeSesionHttp {

	private Long maximaCantidadDeSegundosSinActividad;
	public static final String maximaCantidadDeSegundosSinActividad_FIELD = "maximaCantidadDeSegundosSinActividad";

	public Long getMaximaCantidadDeSegundosSinActividad() {
		return maximaCantidadDeSegundosSinActividad;
	}

	public void setMaximaCantidadDeSegundosSinActividad(final Long maximaCantidadDeSegundosSinActividad) {
		this.maximaCantidadDeSegundosSinActividad = maximaCantidadDeSegundosSinActividad;
	}

	public static ConfiguracionDeSesionHttp create(final Long maxSegundos) {
		final ConfiguracionDeSesionHttp configuracion = new ConfiguracionDeSesionHttp();
		configuracion.maximaCantidadDeSegundosSinActividad = maxSegundos;
		return configuracion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add(maximaCantidadDeSegundosSinActividad_FIELD, maximaCantidadDeSegundosSinActividad).toString();
	}
}
