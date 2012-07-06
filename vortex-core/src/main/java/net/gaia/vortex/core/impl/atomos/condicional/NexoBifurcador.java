/**
 * 13/06/2012 16:56:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.condicional;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.condicional.Bifurcador;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.tasks.BifurcarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un componente de la red vortex que puede delegarle el mensaje recibido a
 * otros dos componentes, dependiendo de una condicion eligira uno u otro en forma excluyente para
 * delegar el mensaje
 * 
 * 
 * @author D. García
 */
@Atomo
public class NexoBifurcador extends ComponenteConProcesadorSupport implements Bifurcador {
	private static final Logger LOG = LoggerFactory.getLogger(NexoBifurcador.class);

	private Condicion condicion;
	private Receptor delegadoPorTrue;
	private Receptor delegadoPorFalse;

	@Override
	public Condicion getCondicion() {
		return condicion;
	}

	@Override
	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion pasada al bifurcador no puede ser null");
		}
		this.condicion = condicion;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		final BifurcarMensaje elegirDelegado = BifurcarMensaje.create(mensaje, condicion, delegadoPorTrue,
				delegadoPorFalse);
		procesarEnThreadPropio(elegirDelegado);
	}

	public static NexoBifurcador create(final TaskProcessor processor, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		final NexoBifurcador nexo = new NexoBifurcador();
		nexo.initializeWith(processor);
		nexo.setCondicion(condicion);
		nexo.setReceptorPorFalse(delegadoPorFalse);
		nexo.setReceptorPorTrue(delegadoPorTrue);
		return nexo;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		LOG.debug("Se intento conectar el destino[{}] al bifurcador [{}] ignorando", destino, this);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		LOG.debug("Se intento desconectar el destino[{}] al bifurcador [{}] ignorando", destino, this);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.Bifurcador#setReceptorPorTrue(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setReceptorPorTrue(final Receptor receptorPorTrue) {
		if (receptorPorTrue == null) {
			throw new IllegalArgumentException("El delegado por true no puede ser null en el bifurcador. Usar el "
					+ ReceptorNulo.class);
		}
		this.delegadoPorTrue = receptorPorTrue;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.Bifurcador#getReceptorPorTrue()
	 */
	@Override
	public Receptor getReceptorPorTrue() {
		return delegadoPorTrue;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.Bifurcador#setReceptorPorFalse(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setReceptorPorFalse(final Receptor receptorPorFalse) {
		if (receptorPorFalse == null) {
			throw new IllegalArgumentException("El delegado por false no puede ser null en el bifurcador. Usar el "
					+ ReceptorNulo.class);
		}
		this.delegadoPorFalse = receptorPorFalse;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.condicional.Bifurcador#getReceptorPorFalse()
	 */
	@Override
	public Receptor getReceptorPorFalse() {
		return delegadoPorFalse;
	}
}
