/**
 * 25/02/2012 19:18:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.http.sessions;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.http.api.ConfiguracionDeNodoRemotoHttp;

import com.google.common.base.Objects;

/**
 * Esta clase representa la configuracion disponible para modificar las opciones de una sesión http
 * 
 * @author D. García
 */
public class ConfiguracionDeSesionHttp {

	private AtomicLong maximaCantidadDeSegundosSinActividad;
	public static final String maximaCantidadDeSegundosSinActividad_FIELD = "maximaCantidadDeSegundosSinActividad";

	private AtomicLong periodoDePollingEnSegundos;
	public static final String periodoDePollingEnSegundos_FIELD = "periodoDePollingEnSegundos";

	public Long getMaximaCantidadDeSegundosSinActividad() {
		return maximaCantidadDeSegundosSinActividad.get();
	}

	public void setMaximaCantidadDeSegundosSinActividad(final Long maximaCantidadDeSegundosSinActividad) {
		this.maximaCantidadDeSegundosSinActividad.set(maximaCantidadDeSegundosSinActividad);
	}

	public static ConfiguracionDeSesionHttp create(final ConfiguracionDeNodoRemotoHttp configuracionDeNodo) {
		final ConfiguracionDeSesionHttp configuracion = new ConfiguracionDeSesionHttp();
		final Long maximaCantidadDeSegundosSinActividadDelNodo = configuracionDeNodo
				.getMaximaCantidadDeSegundosSinActividad();
		configuracion.maximaCantidadDeSegundosSinActividad = new AtomicLong(maximaCantidadDeSegundosSinActividadDelNodo);
		final Long periodoDePollingDelNodo = configuracionDeNodo.getPeriodoDePollingEnSegundos();
		configuracion.periodoDePollingEnSegundos = new AtomicLong(periodoDePollingDelNodo);
		return configuracion;
	}

	public Long getPeriodoDePollingEnSegundos() {
		return periodoDePollingEnSegundos.get();
	}

	public void setPeriodoDePollingEnSegundos(final Long periodoDePollingEnSegundos) {
		this.periodoDePollingEnSegundos.set(periodoDePollingEnSegundos);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add(maximaCantidadDeSegundosSinActividad_FIELD, maximaCantidadDeSegundosSinActividad.get())
				.add(periodoDePollingEnSegundos_FIELD, periodoDePollingEnSegundos).toString();
	}
}
