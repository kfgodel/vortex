/**
 * 17/06/2012 20:01:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.atomos;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.deprecated.DelegarMensajeViejo;
import net.gaia.vortex.deprecated.NexoSupport;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que convierte los objetos recibidos en mensajes que
 * luego envía a un tercero.<br>
 * A través de instancias de esta clase el portal puede enviar los objetos del usuario como mensajes
 * normales de vortex.<br>
 * Este componente no usa threads propios para la conversión de manera que si falla el invocante
 * reciba la excepción en su thread. Sí se usa un thread para delegar al receptor destino.
 * 
 * @author D. García
 */
@Atomo
@Deprecated
public class DesobjetivizadorViejo extends NexoSupport {

	private ConversorDeMensajesVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	/**
	 * @see net.gaia.vortex.deprecated.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final DelegarMensajeViejo delegacion = DelegarMensajeViejo.create(mensaje, getDestino());
		return delegacion;
	}

	/**
	 * Convierte el objeto recibido en un mensaje vortex y si la conversion es exitosa lo introduce
	 * en la red a partir de esta instancia
	 * 
	 * @param mensaje
	 *            El objeto que representa un mensaje
	 * @throws ErrorDeMapeoVortexException
	 *             Si se produce un error en la conversión
	 */
	public void vortificar(final Object mensaje) throws ErrorDeMapeoVortexException {
		final MensajeVortex vortificado = mapeador.convertirAVortex(mensaje);
		this.recibir(vortificado);
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(mapeador_FIELD, mapeador)
				.add("destino", getDestino()).toString();
	}

	public static DesobjetivizadorViejo create(final TaskProcessor processor, final ConversorDeMensajesVortex mapeador,
			final Receptor delegado) {
		final DesobjetivizadorViejo vortificador = new DesobjetivizadorViejo();
		vortificador.mapeador = mapeador;
		vortificador.initializeWith(processor, delegado);
		return vortificador;
	}
}
