/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router2.mensajes;

/**
 * Esta clase representa el ultimo mensaje del handshake para cerrar la comunicacion bidireccional
 * entre patas de nodos distintos.<br>
 * El id de pata conductora corresponde al ID local
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MensajeBidiSuppor {

	public static ConfirmacionDeIdRemoto create(final Long idRemoto, final Long idLocal) {
		final ConfirmacionDeIdRemoto confirmacion = new ConfirmacionDeIdRemoto();
		confirmacion.setIdLocalAlEmisor(idLocal);
		confirmacion.setIdLocalAlReceptor(idRemoto);
		return confirmacion;
	}

}
