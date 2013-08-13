/**
 * 13/08/2013 13:44:56 Copyright (C) 2013 Darío L. García
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
package ar.com.dgarcia.testing;

import java.io.IOException;
import java.net.ServerSocket;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase permite buscar un puerto libre para tests
 * 
 * @author D. García
 */
public class FreePortFinder {

	/**
	 * Intenta obtener un puerto libre para utilizar
	 * 
	 * @return El numero de puerto
	 */
	public static int getFreePort() {
		try {
			return findFreePort();
		} catch (final IOException e) {
			throw new UnhandledConditionException("No fue posible obtener un puerto libre", e);
		}
	}

	/**
	 * Intenta obtener un puerto disponible creando un socket sin puerto
	 * 
	 * @return El número de puerto disponible
	 * @throws IOException
	 *             Si se prooduce una excepción en el proceso
	 */
	private static int findFreePort() throws IOException {
		final ServerSocket server = new ServerSocket(0);
		try {
			server.setReuseAddress(true);
			final int port = server.getLocalPort();
			return port;
		} finally {
			server.close();
		}
	}

}
