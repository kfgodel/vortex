/**
 * 13/06/2012 17:20:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.tasks.condicional;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.tasks.forward.DelegarMensaje;
import net.gaia.vortex.core.prog.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada en thread propio que evalua una condición para elegir el
 * delegado al que envia el mensaje
 * 
 * @author D. García
 */
public class BifurcarMensaje implements WorkUnit {

	private static final Logger LOG = LoggerFactory.getLogger(BifurcarMensaje.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	private Receptor delegadoPorTrue;
	public static final String delegadoPorTrue_FIELD = "delegadoPorTrue";

	private Receptor delegadoPorFalse;
	public static final String delegadoPorFalse_FIELD = "delegadoPorFalse";

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		this.condicion = condicion;
	}

	public Receptor getDelegadoPorTrue() {
		return delegadoPorTrue;
	}

	public void setDelegadoPorTrue(final Receptor delegadoPorTrue) {
		this.delegadoPorTrue = delegadoPorTrue;
	}

	public Receptor getDelegadoPorFalse() {
		return delegadoPorFalse;
	}

	public void setDelegadoPorFalse(final Receptor delegadoPorFalse) {
		this.delegadoPorFalse = delegadoPorFalse;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		Loggers.ATOMOS.trace("Evaluando condicion[{}] en mensaje[{}] para decidir delegado", condicion, mensaje);
		ResultadoDeCondicion resultadoDeCondicion;
		try {
			resultadoDeCondicion = condicion.esCumplidaPor(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] al bifurcar. Descartando mensaje", e);
			return;
		}
		if (!resultadoDeCondicion.esBooleano()) {
			LOG.error("No es posible bifurcar el mensaje porque la condicion[" + condicion + "] sobre el mensaje["
					+ mensaje + "] es indecidible. Descartando mensaje");
			return;
		}
		Receptor delegadoElegido;
		if (resultadoDeCondicion.esTrue()) {
			delegadoElegido = delegadoPorTrue;
		} else {
			delegadoElegido = delegadoPorFalse;
		}
		Loggers.ATOMOS.debug("Evaluo[{}] la condición[{}] delegando mensaje[{}] a nodo[{}]", new Object[] {
				resultadoDeCondicion, condicion, mensaje, delegadoElegido.toShortString() });
		final DelegarMensaje delegacion = DelegarMensaje.create(mensaje, delegadoElegido);
		parallelizer.submitAndForget(delegacion);
	}

	/**
	 * Inicializa esta instancia con los valores minimos
	 */
	protected void initializeWith(final MensajeVortex mensaje, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		this.condicion = condicion;
		this.delegadoPorFalse = delegadoPorFalse;
		this.delegadoPorTrue = delegadoPorTrue;
		this.mensaje = mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(condicion_FIELD, condicion).add(delegadoPorTrue_FIELD, delegadoPorTrue)
				.add(delegadoPorFalse_FIELD, delegadoPorFalse).add(mensaje_FIELD, mensaje).toString();
	}

	public static BifurcarMensaje create(final MensajeVortex mensaje, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		final BifurcarMensaje bifurcar = new BifurcarMensaje();
		bifurcar.initializeWith(mensaje, condicion, delegadoPorTrue, delegadoPorFalse);
		return bifurcar;
	}
}
