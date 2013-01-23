/**
 * 23/01/2013 11:00:08 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.mensajes;


/**
 * Esta clase representa el mensaje de reconfirmacion que cierra la conexion bidid
 * 
 * @author D. García
 */
public class ReconfirmacionDeIdRemoto extends MensajeBidiSuppor {

	public static ReconfirmacionDeIdRemoto create(final Long idRemoto, final Long idLocal) {
		final ReconfirmacionDeIdRemoto reconfirmacion = new ReconfirmacionDeIdRemoto();
		reconfirmacion.setIdLocalAlEmisor(idLocal);
		reconfirmacion.setIdLocalAlReceptor(idRemoto);
		return reconfirmacion;
	}

}
