/**
 * 26/11/2011 14:52:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel;

import java.util.List;

import net.gaia.vortex.protocol.ContenidoVortex;
import net.gaia.vortex.protocol.IdVortex;
import net.gaia.vortex.protocol.MensajeVortex;

import com.google.common.collect.Lists;

/**
 * Esta clase presenta varios métodos utiles para crear escenarios para los tests
 * 
 * @author D. García
 */
public class EscenarioDeTest {

	/**
	 * Crea un mensaje dummy para testear
	 * 
	 * @return El mensaje creado
	 */
	public MensajeVortex crearMensajeDeTest() {
		final ContenidoVortex contenido = ContenidoVortex.create("tipo", "valor");
		final IdVortex identificacion = IdVortex.create("1", "1", 1L, 1L);
		final List<String> tags = Lists.newArrayList("TAG1");
		final MensajeVortex mensajeVortex = MensajeVortex.create(contenido, identificacion, tags);
		return mensajeVortex;
	}

	/**
	 * Crea un mensaje que está mal armado
	 * 
	 * @return El mensaje creado pero incompleto
	 */
	public MensajeVortex crearMensajeDeTestSinHash() {
		final MensajeVortex mensajeDeTest = crearMensajeDeTest();
		// Le quitamos el hash
		mensajeDeTest.getIdentificacion().setHashDelContenido(null);
		return mensajeDeTest;
	}

}
