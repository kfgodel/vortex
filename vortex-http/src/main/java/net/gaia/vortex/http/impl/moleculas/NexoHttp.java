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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.deprecated.ComponenteConMemoriaViejo;
import net.gaia.vortex.deprecated.FlujoInmutableViejo;
import net.gaia.vortex.deprecated.FlujoVortexViejo;
import net.gaia.vortex.deprecated.NexoSinDuplicadosViejo;
import net.gaia.vortex.deprecated.NexoViejo;
import net.gaia.vortex.deprecated.NodoMoleculaSupportViejo;
import net.gaia.vortex.http.impl.atomos.Deshttpizador;
import net.gaia.vortex.http.impl.atomos.Httpizador;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaDeMensajes;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaLimitadaDeMensajes;
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
public class NexoHttp extends NodoMoleculaSupportViejo implements ComponenteConMemoriaViejo, NexoViejo {

	private SesionVortexHttp sesion;
	public static final String sesion_FIELD = "sesion";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private Deshttpizador procesoDesdeHttp;
	public static final String procesoDesdeHttp_FIELD = "procesoDesdeHttp";

	private MemoriaDeMensajes memoriaDeMensajes;
	private NexoSinDuplicadosViejo nodoDeSalidaAVortex;

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final SesionVortexHttp sesion) {
		// Guardamos la referencia para saber cual es nuestra sesión
		this.sesion = sesion;

		// Esta memoria nos permite descartar los mensajes que mandamos al nodo y vienen de vuelta
		this.memoriaDeMensajes = MemoriaLimitadaDeMensajes.create(NexoSinDuplicadosViejo.CANTIDAD_MENSAJES_RECORDADOS);

		// Al recibir de vortex, descartamos duplicados y mandamos por http
		procesoDesdeVortex = NexoSinDuplicadosViejo.create(processor, memoriaDeMensajes,
				Httpizador.create(processor, sesion));

		// Al recibir de http, descartamos duplicados y mandamos por vortex
		nodoDeSalidaAVortex = NexoSinDuplicadosViejo.create(processor, memoriaDeMensajes, delegado);
		procesoDesdeHttp = Deshttpizador.create(processor, nodoDeSalidaAVortex);

		final FlujoVortexViejo flujoInterno = FlujoInmutableViejo.create(procesoDesdeVortex, nodoDeSalidaAVortex);
		initializeWith(flujoInterno);

	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#setDestino(net.gaia.vortex.api.basic.Receptor)
	 */
	
	public void setDestino(final Receptor destino) {
		nodoDeSalidaAVortex.setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#getDestino()
	 */
	
	public Receptor getDestino() {
		return nodoDeSalidaAVortex.getDestino();
	}

	public static NexoHttp create(final TaskProcessor processor, final SesionVortexHttp sesion, final Receptor delegado) {
		final NexoHttp nexo = new NexoHttp();
		nexo.initializeWith(processor, delegado, sesion);
		return nexo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(sesion_FIELD, sesion)
				.add("destino", getDestino()).toString();
	}

	public void onMensajeDesdeHttp(final MensajeVortex received) {
		procesoDesdeHttp.onMensajeDesdeHttp(received);
	}

	/**
	 * @see net.gaia.vortex.deprecated.ComponenteConMemoriaViejo#yaRecibio(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	public boolean yaRecibio(final MensajeVortex mensaje) {
		final boolean yaRecibido = memoriaDeMensajes.tieneRegistroDe(mensaje);
		return yaRecibido;
	}

}
