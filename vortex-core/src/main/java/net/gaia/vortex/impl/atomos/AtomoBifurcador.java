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
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
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

	private Receptor receptorPorTrue;
	public static final String receptorPorTrue_FIELD = "receptorPorTrue";

	private Receptor receptorPorFalse;
	public static final String receptorPorFalse_FIELD = "receptorPorFalse";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		if (Loggers.ATOMOS.isTraceEnabled()) {
			Loggers.ATOMOS.trace("Evaluando condicion[{}] en mensaje[{}] para decidir delegado", condicion, mensaje);
		}
		Receptor receptorElegido;
		try {
			final ResultadoDeCondicion resultadoDeCondicion = condicion.esCumplidaPor(mensaje);
			if (!resultadoDeCondicion.esBooleano()) {
				// Si no es true o false, descartaremos el mensaje
				receptorElegido = getReceptorParaDescartes();
			}
			else {
				if (resultadoDeCondicion.esTrue()) {
					receptorElegido = getReceptorPorTrue();
				}
				else {
					receptorElegido = getReceptorPorFalse();
				}
			}
			if (Loggers.ATOMOS.isDebugEnabled()) {
				Loggers.ATOMOS.debug("Evaluo[{}] la condición[{}] delegando mensaje[{}] a conector[{}]", new Object[] {
						resultadoDeCondicion, condicion, mensaje, receptorElegido });
			}
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] para bifurcar. Descartando mensaje", e);
			receptorElegido = getReceptorParaDescartes();
		}

		// Enviamos mensaje a destino
		receptorElegido.recibir(mensaje);
	}

	public Receptor getReceptorPorTrue() {
		return receptorPorTrue;
	}

	public void setReceptorPorTrue(final Receptor receptorPorTrue) {
		this.receptorPorTrue = receptorPorTrue;
	}

	public Receptor getReceptorPorFalse() {
		return receptorPorFalse;
	}

	public void setReceptorPorFalse(final Receptor receptorPorFalse) {
		this.receptorPorFalse = receptorPorFalse;
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

	/**
	 * Crea un bifurcador sin parametros asumiendo la condicion {@link SiempreTrue} para bifurcar
	 * los mensajes
	 * 
	 * @return El bifurcador creado
	 */
	public static AtomoBifurcador create() {
		return create(SiempreTrue.getInstancia());
	}

	public static AtomoBifurcador create(final Condicion condicion) {
		final AtomoBifurcador bifurcador = new AtomoBifurcador();
		bifurcador.setCondicion(condicion);
		bifurcador.receptorPorTrue = ReceptorNulo.getInstancia();
		bifurcador.receptorPorFalse = ReceptorNulo.getInstancia();
		return bifurcador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(condicion_FIELD, condicion)
				.con(receptorPorTrue_FIELD, receptorPorTrue).con(receptorPorFalse_FIELD, receptorPorFalse).toString();
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Filtro#conectarPorTrueCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarPorTrueCon(final Receptor receptorPorTrue) {
		if (receptorPorTrue == null) {
			throw new IllegalArgumentException("El receptorPorTrue destino no puede ser null. Usar el "
					+ ReceptorNulo.class);
		}
		this.receptorPorTrue = receptorPorTrue;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Filtro#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		conectarPorTrueCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar() {
		desconectarPorTrue();
		desconectarPorFalse();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		if (this.receptorPorTrue.equals(destino)) {
			desconectarPorTrue();
		}
		if (this.receptorPorFalse.equals(destino)) {
			desconectarPorFalse();
		}
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#conectarPorFalseCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarPorFalseCon(final Receptor receptorPorFalse) {
		if (receptorPorFalse == null) {
			throw new IllegalArgumentException("El receptorPorFalse destino no puede ser null. Usar el "
					+ ReceptorNulo.class);
		}
		this.receptorPorFalse = receptorPorFalse;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Filtro#desconectarPorTrue()
	 */
	public void desconectarPorTrue() {
		this.conectarPorTrueCon(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Bifurcador#desconectarPorFalse()
	 */
	public void desconectarPorFalse() {
		this.conectarPorFalseCon(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MonoEmisor#getConectado()
	 */
	public Receptor getConectado() {
		return getReceptorPorTrue();
	}
}
