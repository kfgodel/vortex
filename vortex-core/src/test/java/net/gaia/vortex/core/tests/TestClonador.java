/**
 * 19/01/2013 11:32:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.ContenidoVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.impl.mensaje.copia.ClonadorDeMensajes;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;

import org.junit.Assert;
import org.junit.Test;

/**
 * Esta clase prueba el clonador de mensajes
 * 
 * @author D. García
 */
public class TestClonador {

	/**
	 * Verifico que el contenido del mensaje sea igual pero no el mismo
	 */
	@Test
	public void laCopiaDelMensajeDeberiaTenerUnaCopiaIgualDelContenido() {

		final ContenidoVortex contenidoOriginal = ContenidoMapa.create();
		contenidoOriginal.put("texto", "Hola");
		contenidoOriginal.put("numero", 20L);
		final HashMap<String, Object> mapa = new HashMap<String, Object>();
		contenidoOriginal.put("mapa", mapa);
		mapa.put("textoAnidado", "textoAnidado");
		mapa.put("numero", 40L);

		final MensajeConContenido mensajeOriginal = MensajeConContenido.create(contenidoOriginal);

		// Le asignamos un ID
		final IdDeComponenteVortex idDeComponente = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final IdDeMensaje idDeMensaje = GeneradorSecuencialDeIdDeMensaje.create(idDeComponente).generarId();
		mensajeOriginal.asignarId(idDeMensaje);

		final ClonadorDeMensajes clonador = ClonadorDeMensajes.create(mensajeOriginal);

		final MensajeVortex mensajeCopia = clonador.clonar();
		Assert.assertNotSame("Debería ser otra instancia de mensaje", mensajeOriginal, mensajeCopia);

		final ContenidoVortex contenidoCopia = mensajeCopia.getContenido();
		Assert.assertNotSame("Debería ser otra instancia de contenido", contenidoOriginal, contenidoCopia);

		Assert.assertEquals("Deberian tener igual cantidad de claves", contenidoOriginal.size(), contenidoCopia.size());
		final Set<Entry<String, Object>> entriesOriginal = contenidoOriginal.entrySet();
		for (final Entry<String, Object> entryOriginal : entriesOriginal) {
			final String claveOriginal = entryOriginal.getKey();
			Assert.assertTrue("Debería existir la clave en la copia", contenidoCopia.containsKey(claveOriginal));

			final Object valorOriginal = entryOriginal.getValue();
			final Object valorCopia = contenidoCopia.get(claveOriginal);
			Assert.assertEquals("Deberían ser valores equivalentes", valorOriginal, valorCopia);

			if (valorOriginal instanceof Map) {
				Assert.assertNotSame("Si el valor es un mapa debería ser una copia", valorOriginal, valorCopia);
			}
		}
	}

	@Test
	public void laCopiaDeberiaMantenerElMismoID() {
		final MensajeConContenido mensajeOriginal = MensajeConContenido.create(ContenidoMapa.create());
		// Le asignamos un ID
		final IdDeComponenteVortex idDeComponente = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final IdDeMensaje idDeMensaje = GeneradorSecuencialDeIdDeMensaje.create(idDeComponente).generarId();
		mensajeOriginal.asignarId(idDeMensaje);

		final ClonadorDeMensajes clonador = ClonadorDeMensajes.create(mensajeOriginal);

		final MensajeVortex mensajeCopia = clonador.clonar();
		final IdDeMensaje idDeLaCopia = mensajeCopia.getIdDeMensaje();
		Assert.assertEquals("Debería tener el mismo valor de ID", idDeMensaje, idDeLaCopia);
	}

}
