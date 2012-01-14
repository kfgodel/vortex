/**
 * 10/12/2011 14:17:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import java.util.concurrent.TimeUnit;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.EsperaDeAccion;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.meta.Decision;

/**
 * Esta clase representa la acción realizada por el nodo al terminar el tiempo de espera de la
 * solicitud de consumo
 * 
 * @author D. García
 */
public class ConfirmarConsumoODarPorPerdidoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	public static ConfirmarConsumoODarPorPerdidoWorkUnit create(final ContextoDeEnvio contexto) {
		final ConfirmarConsumoODarPorPerdidoWorkUnit confirmar = new ConfirmarConsumoODarPorPerdidoWorkUnit();
		confirmar.contexto = contexto;
		return confirmar;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn(Decision.EL_TIMEOUT_DE_CONSUMO_QUITA_EL_MENSAJE_DE_ESPERA)
	public void doWork() throws InterruptedException {
		final EsperaDeAccion esperaDeConfirmacion = this.contexto.getEsperaDeAcuseConsumo();
		final long prorroga = esperaDeConfirmacion.getMillisRestantes();
		if (prorroga > 0) {
			// Todavía no es momento de tomar la decisión, esperamos
			this.contexto.getProcesador().processDelayed(TimeMagnitude.of(prorroga, TimeUnit.MILLISECONDS), this);
			return;
		}

		// Se acabó el tiempo de espera. Es hora de hacer algo con el mensaje
		final MemoriaDeMensajes memoria = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoria.getEsperandoAcuseDeConsumo();
		final IdentificadorDeEnvio idDeEnvio = this.contexto.getIdDeEnvio();

		// Verificamos si aún estaba esperando confirmación
		final ContextoDeEnvio estabaEsperando = esperandoConfirmacion.quitar(idDeEnvio);
		if (estabaEsperando == null) {
			// No estaba esperando porque recibimos la confirmación. Nada que hacer
			return;
		}
		// Se acabó el tiempo y no hay confirmación, lo damos por perdido
		final RegistrarMensajePerdidoWorkUnit registrarPerdido = RegistrarMensajePerdidoWorkUnit.create(contexto);
		this.contexto.getProcesador().process(registrarPerdido);

	}

}
