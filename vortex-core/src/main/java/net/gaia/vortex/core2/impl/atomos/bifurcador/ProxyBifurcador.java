/**
 * 13/06/2012 16:56:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.bifurcador;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.annon.Atomo;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.conditional.Condicion;
import net.gaia.vortex.core2.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core2.impl.atomos.ComponenteNulo;

/**
 * Esta clase representa un componente de la red vortex que puede delegarle el mensaje recibido a
 * otros dos componentes, dependiendo de una condicion eligira uno u otro en forma excluyente para
 * delegar el mensaje
 * 
 * 
 * @author D. García
 */
@Atomo
public class ProxyBifurcador extends ComponenteConProcesadorSupport {

	private Condicion condicion;
	private ComponenteVortex delegadoPorTrue;
	private ComponenteVortex delegadoPorFalse;

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion pasada al bifurcador no puede ser null");
		}
		this.condicion = condicion;
	}

	public ComponenteVortex getDelegadoPorTrue() {
		return delegadoPorTrue;
	}

	public void setDelegadoPorTrue(final ComponenteVortex delegadoPorTrue) {
		if (delegadoPorTrue == null) {
			throw new IllegalArgumentException("El delegado por true no puede ser null en el bifurcador. Usar el "
					+ ComponenteNulo.class);
		}
		this.delegadoPorTrue = delegadoPorTrue;
	}

	public ComponenteVortex getDelegadoPorFalse() {
		return delegadoPorFalse;
	}

	public void setDelegadoPorFalse(final ComponenteVortex delegadoPorFalse) {
		if (delegadoPorFalse == null) {
			throw new IllegalArgumentException("El delegado por false no puede ser null en el bifurcador. Usar el "
					+ ComponenteNulo.class);
		}
		this.delegadoPorFalse = delegadoPorFalse;
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core2.api.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		final ElegirDelegadoConCondicion elegirDelegado = ElegirDelegadoConCondicion.create(mensaje, condicion,
				delegadoPorTrue, delegadoPorFalse);
		procesarEnThreadPropio(elegirDelegado);
	}

	public static ProxyBifurcador create(final TaskProcessor processor, final Condicion condicion,
			final ComponenteVortex delegadoPorTrue, final ComponenteVortex delegadoPorFalse) {
		final ProxyBifurcador proxy = new ProxyBifurcador();
		proxy.initializeWith(processor);
		proxy.setCondicion(condicion);
		proxy.setDelegadoPorFalse(delegadoPorFalse);
		proxy.setDelegadoPorTrue(delegadoPorTrue);
		return proxy;
	}
}
