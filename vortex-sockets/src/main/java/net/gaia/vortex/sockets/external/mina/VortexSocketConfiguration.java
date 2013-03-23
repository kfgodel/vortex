/**
 * 04/08/2012 00:59:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.external.mina;

import java.net.SocketAddress;

import net.gaia.vortex.textualizer.VortexTextualizer;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;

import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.dgarcia.objectsockets.external.mina.filters.BinaryMetricsFilter;
import ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration;

/**
 * Esta clase representa la configuración de sockets para vortex
 * 
 * @author D. García
 */
public class VortexSocketConfiguration extends ObjectSocketConfiguration {

	private ListenerDeMetricas metricas;

	public static VortexSocketConfiguration crear(final SocketAddress address, final ListenerDeMetricas metricas) {
		final VortexSocketConfiguration config = new VortexSocketConfiguration();
		config.initialize(address, null, VortexTextualizer.create());
		config.metricas = metricas;
		return config;
	}

	/**
	 * @see ar.dgarcia.objectsockets.impl.ObjectSocketConfiguration#configureFilterChain(org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder)
	 */
	
	protected void configureFilterChain(final DefaultIoFilterChainBuilder filterChain) {
		if (metricas != null) {
			filterChain.addLast("metricasBinario", BinaryMetricsFilter.create(metricas));
		}
		super.configureFilterChain(filterChain);
	}

}
