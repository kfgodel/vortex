/**
 * Created on: 30/10/2010 23:55:32 by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Agents</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="http://sourceforge.net/projects/agents/" property="cc:attributionName"
 * rel="cc:attributionURL">Dario Garcia</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/3.0/">Creative Commons Attribution 3.0 Unported
 * License</a>.<br />
 * Based on a work at <a xmlns:dct="http://purl.org/dc/terms/"
 * href="https://agents.svn.sourceforge.net/svnroot/agents"
 * rel="dct:source">agents.svn.sourceforge.net</a>.
 * 
 * Copyright (C) 2010 Dario L. Garcia
 */

package ar.dgarcia.objectsockets.external.mina.filters;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.textualizer.api.ObjectTextualizer;

/**
 * Esta clase abstrae la capa de conversion de objeto a Strings y viceversa, antes de pasarlo a la
 * capa de transporte
 * 
 * @author D. Garcia
 */
public class String2ObjectCodecFilter extends IoFilterAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(String2ObjectCodecFilter.class);

	private ObjectTextualizer textualizer;

	/**
	 * @see org.apache.mina.core.filterchain.IoFilterAdapter#messageReceived(org.apache.mina.core.filterchain.IoFilter.NextFilter,
	 *      org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	
	public void messageReceived(final NextFilter nextFilter, final IoSession session, final Object message)
			throws Exception {
		LOG.trace("Interpretando mensaje recibido como string: [{}]", message);
		if (!(message instanceof String)) {
			LOG.trace("El mensaje[{}] no es un string. Omitiendo conversion", message);
			super.messageReceived(nextFilter, session, message);
			return;
		}
		final String textMessage = (String) message;
		final ObjectTextualizer serializer = safeGetSerializerFrom(session);
		LOG.debug("Des-serializando mensaje String[{}] a Object usando el textualizer[{}]", textMessage, serializer);
		final Object originalMessage = serializer.convertFromString(textMessage);
		super.messageReceived(nextFilter, session, originalMessage);
	}

	/**
	 * @see org.apache.mina.core.filterchain.IoFilterAdapter#filterWrite(org.apache.mina.core.filterchain.IoFilter.NextFilter,
	 *      org.apache.mina.core.session.IoSession, org.apache.mina.core.write.WriteRequest)
	 */
	
	public void filterWrite(final NextFilter nextFilter, final IoSession session, final WriteRequest writeRequest)
			throws Exception {
		final Object originalMessage = writeRequest.getMessage();
		final ObjectTextualizer serializer = safeGetSerializerFrom(session);
		LOG.debug("Serializando mensaje[{}] a String usando el textualizer[{}]", originalMessage, serializer);
		final String textMessage = serializer.convertToString(originalMessage);
		final DefaultWriteRequest transformedWriteRequest = new DefaultWriteRequest(textMessage,
				writeRequest.getFuture(), writeRequest.getDestination());
		super.filterWrite(nextFilter, session, transformedWriteRequest);
	}

	/**
	 * Obtiene el serializador de objetos para la sesion indicada
	 * 
	 * @param session
	 *            Sesion en la que se almacena el serealizador
	 * @return
	 */
	private ObjectTextualizer safeGetSerializerFrom(final IoSession session) {
		return textualizer;
	}

	public static String2ObjectCodecFilter create(final ObjectTextualizer textualizer) {
		final String2ObjectCodecFilter filter = new String2ObjectCodecFilter();
		filter.textualizer = textualizer;
		return filter;
	}

}
