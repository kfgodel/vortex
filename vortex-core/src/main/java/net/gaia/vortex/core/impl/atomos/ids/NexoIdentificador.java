/**
 * 27/06/2012 15:15:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.ids;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.tasks.IdentificarODescartarMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el nexo que descarta los mensajes propios recibidos y pone una marca
 * identificatoria a los salientes.<br>
 * Este átomo más complejo sirve para las moléculas con ID que requieren evitar sus propios mensajes
 * (como un hub)
 * 
 * @author D. García
 */
@Atomo
public class NexoIdentificador extends NexoSupport {

	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";

	public static NexoIdentificador create(final TaskProcessor processor, final IdentificadorVortex identificador,
			final Receptor delegado) {
		final NexoIdentificador nexo = new NexoIdentificador();
		nexo.initializeWith(processor, identificador, delegado);
		return nexo;
	}

	/**
	 * Inicializa esta instancia con los argumentos pasados
	 * 
	 * @param processor
	 *            El procesador a utilizar en los componentes internos
	 * @param identificador
	 *            El identificador que será discriminado por este nexo
	 * @param delegado
	 */
	protected void initializeWith(final TaskProcessor processor, final IdentificadorVortex identificador,
			final Receptor delegado) {
		super.initializeWith(processor, delegado);
		this.identificador = identificador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeComponente_FIELD, getNumeroDeComponente())
				.con(identificador_FIELD, identificador).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return IdentificarODescartarMensaje.create(mensaje, identificador, getDestino());
	}

}
