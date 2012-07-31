/**
 * 29/07/2012 14:52:01 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.cliente.server.ServerVortexHttpRemoto;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

/**
 * Esta clase representa un sesión http del lado del cliente
 * 
 * @author D. García
 */
public class SesionClienteEnMemoria implements SesionVortexHttp {

	private ServerVortexHttpRemoto serverRemoto;
	private VortexHttpTextualizer textualizer;

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#recibirDesdeHttp(java.lang.String)
	 */
	@Override
	public void recibirDesdeHttp(final String mensajesComoJson) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#obtenerParaHttp()
	 */
	@Override
	public String obtenerParaHttp() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getIdDeSesion()
	 */
	@Override
	public String getIdDeSesion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#acumularParaCliente(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void acumularParaCliente(final MensajeVortex mensaje) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getNexoAsociado()
	 */
	@Override
	public NexoHttp getNexoAsociado() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#setNexoAsociado(net.gaia.vortex.http.impl.moleculas.NexoHttp)
	 */
	@Override
	public void setNexoAsociado(final NexoHttp nexoAsociado) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#esVieja()
	 */
	@Override
	public boolean esVieja() {
		// TODO Auto-generated method stub
		return false;
	}

}
