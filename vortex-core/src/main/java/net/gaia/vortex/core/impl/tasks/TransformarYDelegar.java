/**
 * 13/06/2012 10:58:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada en un thread propio por componentes vortex para
 * transformar un mensaje antes de delegarlo a otro componente
 * 
 * @author D. García
 */
public class TransformarYDelegar implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(TransformarYDelegar.class);

	private MensajeVortex mensajeOriginal;
	public static final String mensaje_FIELD = "mensajeOriginal";

	private Transformacion transformacion;
	public static final String transformacion_FIELD = "transformacion";

	private Receptor delegado;
	public static final String delegado_FIELD = "delegado";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		MensajeVortex mensajeTransformado;
		try {
			mensajeTransformado = transformacion.transformar(mensajeOriginal);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la transformacion[" + transformacion + "] del mensajeOriginal["
					+ mensajeOriginal + "] antes de delegarlo al delegado[" + delegado
					+ "]. Descartando mensajeOriginal", e);
			// Nada más para hacer
			return null;
		}
		// La delegacion es una tarea posterior
		return DelegarMensaje.create(mensajeTransformado, delegado);
	}

	public static TransformarYDelegar create(final MensajeVortex mensajeOriginal, final Transformacion transformacion,
			final Receptor delegado) {
		final TransformarYDelegar transformar = new TransformarYDelegar();
		transformar.transformacion = transformacion;
		transformar.delegado = delegado;
		transformar.mensajeOriginal = mensajeOriginal;
		return transformar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(transformacion_FIELD, transformacion).add(delegado_FIELD, delegado)
				.add(mensaje_FIELD, mensajeOriginal).toString();
	}
}
