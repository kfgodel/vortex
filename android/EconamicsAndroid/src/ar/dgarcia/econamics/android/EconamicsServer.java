/**
 * 24/09/2012 17:01:43 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.econamics.android;

/**
 * Esta clase reune algunos datos para la conexion al servidor
 * 
 * @author D. García
 */
public class EconamicsServer {

	public static final String IP_DEL_SERVIDOR = "192.168.1.130";
	public static final int PUERTO_SOCKET_VORTEX = 60220;
	public static final int PUERTO_HTTP = 63636;
	public static final String PREFIJO_URL_ARCHIVOS = "http://" + IP_DEL_SERVIDOR + ":" + PUERTO_HTTP
			+ "/agent/archivo/";

}
