/**
 * 20/08/2011 13:49:51 Copyright (C) 2011 Darío L. García
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenpines.commons.persistence.repositories.GenericRepository;

/**
 * Esta clase representa un localizador que sabe buscar en la base por ID
 * 
 * @author D. García
 */
public class ReferenciaPorId implements ReferenciaAReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(ReferenciaPorId.class);

	private GenericRepository repository;
	private Long idReceptor;
	private ReferenciaAReceptor delegate;

	/**
	 * @see net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor#localizar()
	 */
	@Override
	public ReceptorHttp localizar() {
		if (this.delegate != null) {
			return delegate.localizar();
		}
		final ReceptorHttp receptorHttp = repository.findById(idReceptor, ReceptorHttp.class);
		if (receptorHttp == null) {
			LOG.error("El receptor[{}] devolvio null al buscarlo en la base", idReceptor);
		}
		receptorHttp.setGenericRepository(repository);
		return receptorHttp;
	}

	public static ReferenciaPorId create(final GenericRepository repository, final Long idReceptor) {
		final ReferenciaPorId localizador = new ReferenciaPorId();
		localizador.idReceptor = idReceptor;
		localizador.repository = repository;
		return localizador;
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
		return this.idReceptor != null;
	}
}
