/**
 * 19/08/2013 20:31:17 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.proto.ConectorBloqueante;
import net.gaia.vortex.impl.support.EmisorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la implementación básica del bifurcador de mensajes basado en una condición
 * para elegir destino.<br>
 * Esta clase procesa los mensajes recibidos en el mismo thread que se los entrega, no teniendo
 * thread propio
 * 
 * @author D. García
 */
@Atomo
public class AtomoBifurcador extends EmisorSupport implements Bifurcador {
	private static final Logger LOG = LoggerFactory.getLogger(AtomoBifurcador.class);

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	private Conector conectorPorTrue;
	public static final String conectorPorTrue_FIELD = "conectorPorTrue";

	private Conector conectorPorFalse;
	public static final String conectorPorFalse_FIELD = "conectorPorFalse";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		if (Loggers.ATOMOS.isTraceEnabled()) {
			Loggers.ATOMOS.trace("Evaluando condicion[{}] en mensaje[{}] para decidir delegado", condicion, mensaje);
		}
		Conector conectorElegido;
		try {
			final ResultadoDeCondicion resultadoDeCondicion = condicion.esCumplidaPor(mensaje);
			if (!resultadoDeCondicion.esBooleano()) {
				// Si no es true o false, descartaremos el mensaje
				conectorElegido = getConectorParaDescartes();
			}
			else {
				if (resultadoDeCondicion.esTrue()) {
					conectorElegido = getConectorPorTrue();
				}
				else {
					conectorElegido = getConectorPorFalse();
				}
			}
			if (Loggers.ATOMOS.isDebugEnabled()) {
				Loggers.ATOMOS.debug("Evaluo[{}] la condición[{}] delegando mensaje[{}] a conector[{}]", new Object[] {
						resultadoDeCondicion, condicion, mensaje, conectorElegido });
			}
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] para bifurcar. Descartando mensaje", e);
			conectorElegido = getConectorParaDescartes();
		}

		// Enviamos mensaje a destino
		conectorElegido.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#getConectorPorTrue()
	 */
	public Conector getConectorPorTrue() {
		return conectorPorTrue;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#getConectorPorFalse()
	 */
	public Conector getConectorPorFalse() {
		return conectorPorFalse;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#getCondicion()
	 */
	public Condicion getCondicion() {
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#setCondicion(net.gaia.vortex.api.condiciones.Condicion)
	 */
	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion pasada al bifurcador no puede ser null");
		}
		this.condicion = condicion;
	}

	public static AtomoBifurcador create(final Condicion condicion) {
		final AtomoBifurcador bifurcador = new AtomoBifurcador();
		bifurcador.setCondicion(condicion);
		bifurcador.conectorPorTrue = ConectorBloqueante.create();
		bifurcador.conectorPorFalse = ConectorBloqueante.create();
		return bifurcador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(condicion_FIELD, condicion)
				.con(conectorPorTrue_FIELD, conectorPorTrue).con(conectorPorFalse_FIELD, conectorPorFalse).toString();
	}

}
