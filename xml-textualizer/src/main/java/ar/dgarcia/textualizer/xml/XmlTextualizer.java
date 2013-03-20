/**
 * Created on: 29/08/2010 20:17:12 by: Dario L. Garcia
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

package ar.dgarcia.textualizer.xml;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.ConversionException;

/**
 * Esta clase encapsula la dependencia y uso de Json
 * 
 * @author D. García
 */
public class XmlTextualizer implements ObjectTextualizer {

	private XStream internalConverter;

	public static XmlTextualizer create() {
		final XmlTextualizer serializer = new XmlTextualizer();
		serializer.internalConverter = new XStream();
		return serializer;
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertToString(java.lang.Object)
	 */
	
	public String convertToString(final Object value) throws CannotTextSerializeException {
		String represented;
		try {
			represented = internalConverter.toXML(value);
		} catch (final XStreamException e) {
			throw new CannotTextSerializeException("Se produjo un error al intentar serializar el objeto: " + value, e);
		}
		return represented;
	}

	/**
	 * @see ar.dgarcia.textualizer.api.ObjectTextualizer#convertFromString(java.lang.String)
	 */
	
	public Object convertFromString(final String value) throws CannotTextUnserializeException {
		Object object;
		try {
			object = internalConverter.fromXML(value);
		} catch (final ConversionException e) {
			throw new CannotTextUnserializeException(
					"Cannot deserialize: "
							+ value
							+ ".\nIf caused by CannotResolveClassException ensure you have all classes used for the message on both planets",
					e);
		} catch (final XStreamException e) {
			throw new CannotTextUnserializeException("Se produjo un error al intentar deserealizar un objeto desde: " + value, e);
		}
		return object;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

}
