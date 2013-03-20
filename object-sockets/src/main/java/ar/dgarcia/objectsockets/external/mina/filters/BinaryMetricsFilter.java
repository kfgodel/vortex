/**
 * 04/08/2012 01:09:54 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.objectsockets.external.mina.filters;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;

/**
 * Esta clase implementa el filtro que permite realizar métricas sobre los bytes transferidos.<br>
 * Las métricas miden los bytes recibidos y los bytes enviados
 * 
 * @author D. García
 */
public class BinaryMetricsFilter extends IoFilterAdapter {

	private ListenerDeMetricas metricas;

	public ListenerDeMetricas getMetricas() {
		return metricas;
	}

	public static BinaryMetricsFilter create(final ListenerDeMetricas metricas) {
		final BinaryMetricsFilter filter = new BinaryMetricsFilter();
		filter.metricas = metricas;
		return filter;
	}

	/**
	 * @see org.apache.mina.core.filterchain.IoFilterAdapter#messageReceived(org.apache.mina.core.filterchain.IoFilter.NextFilter,
	 *      org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	
	public void messageReceived(final NextFilter nextFilter, final IoSession session, final Object message)
			throws Exception {
		final IoBuffer buffer = (IoBuffer) message;
		final int cantidadDeBytesEntrantes = buffer.remaining();
		metricas.registrarInput(cantidadDeBytesEntrantes);
		super.messageReceived(nextFilter, session, message);
	}

	/**
	 * @see org.apache.mina.core.filterchain.IoFilterAdapter#filterWrite(org.apache.mina.core.filterchain.IoFilter.NextFilter,
	 *      org.apache.mina.core.session.IoSession, org.apache.mina.core.write.WriteRequest)
	 */
	
	public void filterWrite(final NextFilter nextFilter, final IoSession session, final WriteRequest writeRequest)
			throws Exception {
		final Object message = writeRequest.getMessage();
		final IoBuffer outputBuffer = (IoBuffer) message;
		final int cantidadDeBytesSalientes = outputBuffer.limit();
		metricas.registrarOutput(cantidadDeBytesSalientes);
		super.filterWrite(nextFilter, session, writeRequest);
	}
}
