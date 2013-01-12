/**
 * 25/07/2012 18:58:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.atomos.memoria.NexoFiltroDuplicados;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorVariable;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.core.impl.tasks.DelegarMensaje;
import net.gaia.vortex.http.impl.atomos.Deshttpizador;
import net.gaia.vortex.http.impl.atomos.Httpizador;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que une la red vortex con una sesión http de manera
 * que los mensajes circulen por la web representados como parte de la misma red vortex.<br>
 * Este tipo de nexos une el mundo http con el mundo vortex.<br>
 * <br>
 * Este nexo tiene la capacidad de identificar los mensajes que recibe pudiendo descartar
 * duplicados.<br>
 * 
 * @author D. García
 */
@Molecula
public class NexoHttp extends NexoSupport implements ComponenteConMemoria {

	private SesionVortexHttp sesion;
	public static final String sesion_FIELD = "sesion";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private Deshttpizador procesoDesdeHttp;
	public static final String procesoDesdeHttp_FIELD = "procesoDesdeHttp";

	private ReceptorVariable<Receptor> destinoDesdeHttp;

	private MemoriaDeMensajes memoriaDeMensajes;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, procesoDesdeVortex);
	}

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final SesionVortexHttp sesion) {
		// Creamos el receptor variable antes que nada
		destinoDesdeHttp = ReceptorVariable.create(delegado);
		super.initializeWith(processor, delegado);
		// Guardamos la referencia para saber cual es nuestra sesion
		this.sesion = sesion;

		// Creamos una memoria compartida entre el filtro de entrada y de salida de duplicados
		this.memoriaDeMensajes = MemoriaLimitadaDeMensajes.create(NexoFiltroDuplicados.CANTIDAD_MENSAJES_RECORDADOS);

		// No envíamos a http los mensajes recibidos desde http (por la memoria compartida)
		procesoDesdeVortex = NexoFiltroDuplicados.create(processor, memoriaDeMensajes,
				Httpizador.create(processor, sesion));
		// No enviamos a la red los mensajes recibidos desde la red (por la memoria compartida)
		procesoDesdeHttp = Deshttpizador.create(processor,
				NexoFiltroDuplicados.create(processor, memoriaDeMensajes, destinoDesdeHttp));
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		super.setDestino(destino);
		this.destinoDesdeHttp.setReceptorActual(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		return destinoDesdeHttp.getReceptorActual();
	}

	public static NexoHttp create(final TaskProcessor processor, final SesionVortexHttp sesion, final Receptor delegado) {
		final NexoHttp nexo = new NexoHttp();
		nexo.initializeWith(processor, delegado, sesion);
		return nexo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(sesion_FIELD, sesion)
				.add(destino_FIELD, getDestino()).toString();
	}

	public void onMensajeDesdeHttp(final MensajeVortex received) {
		procesoDesdeHttp.onMensajeDesdeHttp(received);
	}

	/**
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = memoriaDeMensajes.tieneRegistroDe(mensaje);
		return yaRecibido;
	}

}
