/**
 * 13/06/2012 16:56:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.condicional;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.api.atomos.condicional.BifurcadorViejo;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;
import net.gaia.vortex.core.impl.tasks.condicional.BifurcarMensajeViejo;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente de la red vortex que puede delegarle el mensaje recibido a
 * otros dos componentes, dependiendo de una condicion eligira uno u otro en forma excluyente para
 * delegar el mensaje
 * 
 * 
 * @author D. García
 */
@Atomo
@Deprecated
public class NexoBifurcadorViejo extends ReceptorConProcesador implements BifurcadorViejo {
	private static final Logger LOG = LoggerFactory.getLogger(NexoBifurcadorViejo.class);

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";
	private Receptor delegadoPorTrue;
	public static final String delegadoPorTrue_FIELD = "delegadoPorTrue";
	private Receptor delegadoPorFalse;
	public static final String delegadoPorFalse_FIELD = "delegadoPorFalse";

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion pasada al bifurcador no puede ser null");
		}
		this.condicion = condicion;
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final BifurcarMensajeViejo elegirDelegado = BifurcarMensajeViejo.create(mensaje, condicion, delegadoPorTrue,
				delegadoPorFalse);
		return elegirDelegado;
	}

	public static NexoBifurcadorViejo create(final TaskProcessor processor, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		final NexoBifurcadorViejo nexo = new NexoBifurcadorViejo();
		nexo.initializeWith(processor);
		nexo.setCondicion(condicion);
		nexo.setReceptorPorFalse(delegadoPorFalse);
		nexo.setReceptorPorTrue(delegadoPorTrue);
		return nexo;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.EmisorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	public void conectarCon(final Receptor destino) {
		LOG.debug("Se intento conectar el destino[{}] al bifurcador [{}] ignorando", destino, this);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.EmisorViejo#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	public void desconectarDe(final Receptor destino) {
		LOG.debug("Se intento desconectar el destino[{}] al bifurcador [{}] ignorando", destino, this);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.BifurcadorViejo#setReceptorPorTrue(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setReceptorPorTrue(final Receptor receptorPorTrue) {
		if (receptorPorTrue == null) {
			throw new IllegalArgumentException("El delegado por true no puede ser null en el bifurcador. Usar el "
					+ ReceptorNulo.class);
		}
		this.delegadoPorTrue = receptorPorTrue;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.BifurcadorViejo#getReceptorPorTrue()
	 */

	public Receptor getReceptorPorTrue() {
		return delegadoPorTrue;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.BifurcadorViejo#setReceptorPorFalse(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setReceptorPorFalse(final Receptor receptorPorFalse) {
		if (receptorPorFalse == null) {
			throw new IllegalArgumentException("El delegado por false no puede ser null en el bifurcador. Usar el "
					+ ReceptorNulo.class);
		}
		this.delegadoPorFalse = receptorPorFalse;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.BifurcadorViejo#getReceptorPorFalse()
	 */

	public Receptor getReceptorPorFalse() {
		return delegadoPorFalse;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(condicion_FIELD, condicion).con(delegadoPorTrue_FIELD, delegadoPorTrue)
				.con(delegadoPorFalse_FIELD, delegadoPorFalse).toString();
	}

}
