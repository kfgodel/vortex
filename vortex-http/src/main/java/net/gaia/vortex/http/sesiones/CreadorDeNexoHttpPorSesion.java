/**
 * 06/08/2012 19:48:25 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el listener de sesiones http que crea un {@link NexoHttp} por cada sesion y
 * lo vincula a la red vortex con la estrategia indicada
 * 
 * @author D. García
 */
public class CreadorDeNexoHttpPorSesion implements ListenerDeSesionesHttp {
	private static final Logger LOG = LoggerFactory.getLogger(CreadorDeNexoHttpPorSesion.class);

	private TaskProcessor processor;
	private EstrategiaDeConexionDeNexos estrategia;
	public static final String estrategia_FIELD = "estrategia";

	/**
	 * @see net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp#onSesionCreada(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	
	public void onSesionCreada(final SesionVortexHttp sesionCreada) {
		LOG.debug("Creando nexo para la sesion http[{}]", sesionCreada);
		final NexoHttp nuevoNexo = NexoHttp.create(processor, sesionCreada, ReceptorNulo.getInstancia());
		sesionCreada.setNexoAsociado(nuevoNexo);
		try {
			estrategia.onNexoCreado(nuevoNexo);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de conexion[" + estrategia + "] al pasarle el nexo["
					+ nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp#onSesionDestruida(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	
	public void onSesionDestruida(final SesionVortexHttp sesionDestruida) {
		LOG.debug("Cerrando nexo para la sesion http[{}]", sesionDestruida);
		final NexoHttp nexoCerrado = sesionDestruida.getNexoAsociado();
		if (nexoCerrado == null) {
			LOG.error("Se cerró una sesion[{}] que no tiene nexo asociado?", sesionDestruida);
			return;
		}
		try {
			estrategia.onNexoCerrado(nexoCerrado);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de desconexion[" + estrategia + "] al pasarle el nexo["
					+ nexoCerrado + "]. Ignorando error", e);
		}
	}

	public static CreadorDeNexoHttpPorSesion create(final TaskProcessor processor,
			final EstrategiaDeConexionDeNexos estrategia) {
		final CreadorDeNexoHttpPorSesion creador = new CreadorDeNexoHttpPorSesion();
		creador.estrategia = estrategia;
		creador.processor = processor;
		return creador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(estrategia_FIELD, estrategia).toString();
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(final TaskProcessor processor) {
		this.processor = processor;
	}

	public EstrategiaDeConexionDeNexos getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(final EstrategiaDeConexionDeNexos estrategia) {
		this.estrategia = estrategia;
	}

}
