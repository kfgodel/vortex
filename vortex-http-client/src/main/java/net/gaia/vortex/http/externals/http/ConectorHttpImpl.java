/**
 * 02/02/2012 23:59:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.externals.http;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.protocol.http.VortexWrapper;

import com.google.common.base.Objects;

/**
 * Esta clase es la implementación del conector http para un nodo vortex
 * 
 * @author D. García
 */
public class ConectorHttpImpl implements ConectorHttp {

	private String serverUrl;
	public static final String serverUrl_FIELD = "serverUrl";

	public static ConectorHttpImpl create(final String utl, final InterpreteJson interprete) {
		final ConectorHttpImpl conector = new ConectorHttpImpl();
		return conector;
	}

	/**
	 * @see net.gaia.vortex.http.externals.http.ConectorHttp#enviarYRecibir(net.gaia.vortex.protocol.http.VortexWrapper)
	 */
	@Override
	public VortexWrapper enviarYRecibir(final VortexWrapper enviado) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(serverUrl_FIELD, serverUrl).toString();
	}
}
