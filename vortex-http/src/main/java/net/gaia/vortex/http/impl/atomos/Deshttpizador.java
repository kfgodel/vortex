/**
 * 27/07/2012 21:43:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.atomos;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.deprecated.DelegarMensajeViejo;
import net.gaia.vortex.deprecated.NexoSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que al recibir un mensaje desde http lo introduce en
 * la red a través del receptor que conoce
 * 
 * @author D. García
 */
public class Deshttpizador extends NexoSupport {

	public void onMensajeDesdeHttp(final MensajeVortex recibido) {
		// Insertamos el mensaje en la rede desde nosotros como inicio
		this.recibir(recibido);
	}

	/**
	 * @see net.gaia.vortex.deprecated.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final DelegarMensajeViejo delegacion = DelegarMensajeViejo.create(mensaje, getDestino());
		return delegacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(destino_FIELD, getDestino())
				.toString();
	}

	public static Deshttpizador create(final TaskProcessor processor, final Receptor destino) {
		final Deshttpizador desocketizador = new Deshttpizador();
		desocketizador.initializeWith(processor, destino);
		return desocketizador;
	}

}
