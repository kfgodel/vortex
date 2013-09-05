/**
 * 04/09/2013 23:19:19 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.MonoConectableSupport;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;

/**
 * Esta clase representa el componente vortex que convierte todo los objetos indicados en mensajes y
 * los envia por el conector de salida
 * 
 * @author D. García
 */
public class Desobjetivizador extends MonoConectableSupport {

	private ConversorDeMensajesVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	/**
	 * Convierte el objeto recibido en un mensaje vortex y si la conversion es exitosa lo introduce
	 * en la red a partir de esta instancia
	 * 
	 * @param mensaje
	 *            El objeto que representa un mensaje
	 * @throws ErrorDeMapeoVortexException
	 *             Si se produce un error en la conversión
	 */
	public void vortificar(final Object mensaje) throws ErrorDeMapeoVortexException {
		final MensajeVortex vortificado = mapeador.convertirAVortex(mensaje);
		this.getConectorUnico().recibir(vortificado);
	}

	public static Desobjetivizador create(final ConversorDeMensajesVortex mapeador) {
		final Desobjetivizador desobjetivizador = new Desobjetivizador();
		desobjetivizador.mapeador = mapeador;
		return desobjetivizador;
	}
}
