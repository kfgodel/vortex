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
import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.api.moleculas.ids.ReceptorIdentificable;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.atomos.ids.NexoIdentificador;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorVariable;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;
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
public class NexoHttp extends NexoSupport implements ReceptorIdentificable {

	private SesionVortexHttp sesion;
	public static final String sesion_FIELD = "sesion";

	private Receptor procesoDesdeVortex;
	public static final String procesoDesdeVortex_FIELD = "procesoDesdeVortex";

	private Deshttpizador procesoDesdeHttp;
	public static final String procesoDesdeHttp_FIELD = "procesoDesdeHttp";

	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";

	private ReceptorVariable<Receptor> destinoDesdeHttp;

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ids.VortexIdentificable#getIdentificador()
	 */
	@Override
	public IdentificadorVortex getIdentificador() {
		return identificador;
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, procesoDesdeVortex);
	}

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final SesionVortexHttp sesion) {
		identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		// Creamos el receptor variable antes que nada
		destinoDesdeHttp = ReceptorVariable.create(delegado);
		super.initializeWith(processor, delegado);
		// Guardamos la referencia para saber cual es nuestra sesion
		this.sesion = sesion;

		// No envíamos por el socket los mensajes propios
		procesoDesdeVortex = NexoIdentificador.create(processor, identificador, Httpizador.create(processor, sesion));
		// Indicamos que los mensajes recibidos desde el socket son nuestros
		procesoDesdeHttp = Deshttpizador.create(processor,
				NexoIdentificador.create(processor, identificador, destinoDesdeHttp));
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
		return ToString.de(this).con(numeroDeComponente_FIELD, getNumeroDeComponente()).add(sesion_FIELD, sesion)
				.add(destino_FIELD, getDestino()).toString();
	}

	public void onObjectReceived(final MensajeVortex received, final SesionVortexHttp sesion) {
		procesoDesdeHttp.onMensajeDesdeHttp(received, sesion);
	}

}