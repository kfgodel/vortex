/**
 * 14/01/2012 19:57:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa el reporte de como se desempeño cada receptor al recibir un mensaje
 * 
 * @author D. García
 */
public class ReportePerformanceRuteo {

	private MensajeVortex mensaje;
	private ControlDeRuteo controlDeRuteo;

	public static ReportePerformanceRuteo create(final ControlDeRuteo controlDeRuteo, final MensajeVortex mensaje) {
		final ReportePerformanceRuteo reporte = new ReportePerformanceRuteo();
		reporte.mensaje = mensaje;
		reporte.controlDeRuteo = controlDeRuteo;
		return reporte;
	}

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	public ControlDeRuteo getControlDeRuteo() {
		return controlDeRuteo;
	}

	public void setControlDeRuteo(final ControlDeRuteo controlDeRuteo) {
		this.controlDeRuteo = controlDeRuteo;
	}

}
