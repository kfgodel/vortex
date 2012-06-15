/**
 * 13/06/2012 10:58:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.transformador;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.atomos.transformacion.Transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la tarea realizada en background que transforma la tarea recibida y luego
 * lo delega a otro receptor
 * 
 * @author D. García
 */
public class TransformarElMensajeYDelegar implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(TransformarElMensajeYDelegar.class);

	private Transformacion transformacion;
	public static final String transformacion_FIELD = "transformacion";

	private EntregarMensajeADelegado delegacion;
	public static final String delegacion_FIELD = "delegacion";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final MensajeVortex mensajeOriginal = delegacion.getMensaje();
		MensajeVortex mensajeTransformado;
		try {
			mensajeTransformado = transformacion.transformar(mensajeOriginal);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la transformacion[" + transformacion + "] del mensaje[" + mensajeOriginal
					+ "] antes de delegarlo al delegado[" + delegacion.getDelegado() + "]. Descartando mensaje", e);
			return;
		}
		delegacion.setMensaje(mensajeTransformado);
		delegacion.doWork();
	}

	public static TransformarElMensajeYDelegar create(final Transformacion transformacion,
			final EntregarMensajeADelegado delegacion) {
		final TransformarElMensajeYDelegar transformar = new TransformarElMensajeYDelegar();
		transformar.transformacion = transformacion;
		transformar.delegacion = delegacion;
		return transformar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(transformacion_FIELD, transformacion).add(delegacion_FIELD, delegacion)
				.toString();
	}
}
