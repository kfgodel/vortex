/**
 * 13/06/2012 11:27:27 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.tasks.condicional;

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
 * Esta clase representa la tarea realizada en thread propio por un componente vortex para evaluar
 * una condición y en base al resultado descartar el mensaje o enviarlo a otro delegado
 * 
 * @author D. García
 */
public class FiltrarMensaje implements WorkUnit {

	private static final Logger LOG = LoggerFactory.getLogger(FiltrarMensaje.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	private Receptor delegadoPorTrue;
	public static final String delegadoPorTrue_FIELD = "delegadoPorTrue";

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

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	
	public WorkUnit doWork() throws InterruptedException {
		Loggers.ATOMOS.trace("Evaluando condicion[{}] en mensaje[{}] para decidir delegado", condicion, mensaje);
		ResultadoDeCondicion resultadoDeCondicion;
		try {
			resultadoDeCondicion = condicion.esCumplidaPor(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] para bifurcar. Descartando mensaje", e);
			return null;
		}
		if (resultadoDeCondicion.esBooleano() && resultadoDeCondicion.esFalse()) {
			Loggers.ATOMOS.debug("Evaluo[{}] la condición[{}] descartando mensaje[{}]. No llegará a nodo[{}]",
					new Object[] { resultadoDeCondicion, condicion, mensaje, delegadoPorTrue.toShortString() });
			return null;
		}
		Loggers.ATOMOS.debug("Evaluo[{}] la condición[{}] delegando mensaje[{}] a nodo[{}]", new Object[] {
				resultadoDeCondicion, condicion, mensaje, delegadoPorTrue.toShortString() });
		return DelegarMensaje.create(mensaje, delegadoPorTrue);
	}

	/**
	 * Inicializa esta instancia con los valores minimos
	 */
	protected void initializeWith(final MensajeVortex mensaje, final Condicion condicion, final Receptor delegadoPorTrue) {
		this.condicion = condicion;
		this.delegadoPorTrue = delegadoPorTrue;
		this.mensaje = mensaje;
	}

	public static FiltrarMensaje create(final MensajeVortex mensaje, final Condicion condicion, final Receptor delegado) {
		final FiltrarMensaje evaluacion = new FiltrarMensaje();
		evaluacion.initializeWith(mensaje, condicion, delegado);
		return evaluacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).add(condicion_FIELD, getCondicion()).add(delegadoPorTrue_FIELD, getDelegadoPorTrue())
				.add(mensaje_FIELD, getMensaje()).toString();
	}
}