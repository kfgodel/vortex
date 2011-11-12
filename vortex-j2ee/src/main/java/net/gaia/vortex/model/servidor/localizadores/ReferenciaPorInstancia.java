/**
 * 20/08/2011 13:53:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.servidor.localizadores;

import net.gaia.vortex.persistibles.ReceptorHttp;

/**
 * Esta clase es un localizador muy tonto que devuelve la instancia indicada
 * 
 * @author D. García
 */
public class ReferenciaPorInstancia implements ReferenciaAReceptor {

	private ReceptorHttp receptor;

	private ReferenciaAReceptor delegate;

	public static ReferenciaPorInstancia create(final ReceptorHttp receptor) {
		final ReferenciaPorInstancia localizador = new ReferenciaPorInstancia();
		localizador.receptor = receptor;
		return localizador;
	}

	/**
	 * @see net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor#localizar()
	 */
	@Override
	public ReceptorHttp localizar() {
		if (this.delegate != null) {
			return delegate.localizar();
		}
		return receptor;
	}

	/**
	 * @see net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor#reemplazarPor(net.gaia.vortex.model.servidor.localizadores.ReferenciaPorId)
	 */
	@Override
	public void reemplazarPor(final ReferenciaAReceptor delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor#esIdentificableEnLaBase()
	 */
	@Override
	public boolean esIdentificableEnLaBase() {
		if (this.delegate != null) {
			return this.delegate.esIdentificableEnLaBase();
		}
		if (this.receptor == null) {
			return false;
		}
		return this.receptor.getId() != null;
	}
}
