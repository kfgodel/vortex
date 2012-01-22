/**
 * 10/12/2011 14:07:27 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ControlDeConsumoDeEnvio;
import net.gaia.vortex.lowlevel.impl.EsperaDeAccion;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.ruteos.MensajesEnEspera;

/**
 * Esta clase representa la acción realizada por el nodo cuando se vence el tiempo de espera de la
 * confirmación de consumo
 * 
 * @author D. García
 */
public class ConfirmarRecepcionDeAcuseConsumoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	public static ConfirmarRecepcionDeAcuseConsumoWorkUnit create(final ContextoDeEnvio contexto) {
		final ConfirmarRecepcionDeAcuseConsumoWorkUnit confirmar = new ConfirmarRecepcionDeAcuseConsumoWorkUnit();
		confirmar.contexto = contexto;
		return confirmar;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Verificamos que el mensaje esté en espera de acuse todavía
		final MemoriaDeMensajes memoria = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoAcuse = memoria.getEsperandoAcuseDeConsumo();
		final IdentificadorDeEnvio idDeEnvio = this.contexto.getIdDeEnvio();
		if (!esperandoAcuse.incluyeA(idDeEnvio)) {
			// Si el envío no está en espera de acuse, es porque ya la recibimos. Nada que hacer acá
			return;
		}

		// Verificamos si aún queda tiempo para esperar el acuse
		final EsperaDeAccion esperaDeConfirmacion = this.contexto.getEsperaDeAcuseConsumo();
		final long millisDeEsperaDisponible = esperaDeConfirmacion.getMillisRestantes();
		if (millisDeEsperaDisponible > 0) {
			// Todavía tiene tiempo para llegar el acuse, lo esperamos
			this.contexto.getProcesador().processDelayed(
					TimeMagnitude.of(millisDeEsperaDisponible, TimeUnit.MILLISECONDS), this);
			return;
		}

		// No llegó el mensaje y se acabó el tiempo de espera. Vemos si corresponde enviar solicitud
		final ControlDeConsumoDeEnvio controlDeConsumo = contexto.getControlDeConsumo();
		if (controlDeConsumo.correspondeSolicitud()) {
			// Enviamos la solicitud y esperamos nuevamente
			final EnviarSolicitudDeAcuseDeConsumoWorkUnit enviarSolicitud = EnviarSolicitudDeAcuseDeConsumoWorkUnit
					.create(this.contexto);
			this.contexto.getProcesador().process(enviarSolicitud);

			final EsperarAcuseConsumoWorkUnit esperaDeAcuse = EsperarAcuseConsumoWorkUnit.create(contexto);
			contexto.getProcesador().process(esperaDeAcuse);
			return;
		}

		// Nos apropiamos del mensaje si aún está en espera de acuse
		final ContextoDeEnvio estabaEsperando = esperandoAcuse.quitar(idDeEnvio);
		if (estabaEsperando == null) {
			// En el medio recibimos el acuse, no hacemos nada más acá
			return;
		}
		// Consideramos que nunca llegó el acuse, lo damos por perdido
		final RegistrarMensajePerdidoWorkUnit registrarPerdido = RegistrarMensajePerdidoWorkUnit.create(contexto);
		this.contexto.getProcesador().process(registrarPerdido);
	}
}
