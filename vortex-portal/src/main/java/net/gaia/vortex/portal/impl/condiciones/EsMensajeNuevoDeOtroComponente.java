/**
 * 04/09/2013 21:07:29 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.portal.impl.condiciones;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.condiciones.EsMensajeNuevo;

/**
 * Esta clase representa la condición que evalua si los mensajes recibidos son de otros
 * 
 * @author D. García
 */
public class EsMensajeNuevoDeOtroComponente implements Condicion {

	private EsMensajeDeOtroComponente esDeOtro;
	private EsMensajeNuevo esNuevo;

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		// Primero vemos si es un mensaje externo
		final ResultadoDeCondicion mensajeDeOtroComponente = esDeOtro.esCumplidaPor(mensaje);
		if (mensajeDeOtroComponente.esFalse()) {
			// Es un mensaje propio, ya sabemos que no se cumple esta condicion
			return ResultadoDeCondicion.paraBooleano(false);
		}
		// Vemos si aunque sea de otro, ya lo recibimos antes
		final ResultadoDeCondicion esNuevoYDeOtro = esNuevo.esCumplidaPor(mensaje);
		return esNuevoYDeOtro;
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	public List<Condicion> getSubCondiciones() {
		final ArrayList<Condicion> subCondiciones = new ArrayList<Condicion>();
		subCondiciones.add(esDeOtro);
		subCondiciones.add(esNuevo);
		return subCondiciones;
	}
}
