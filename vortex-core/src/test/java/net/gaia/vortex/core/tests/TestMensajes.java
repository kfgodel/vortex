/**
 * 19/01/2013 13:22:11 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.core.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.helpers.VortexMap;

import org.junit.Assert;
import org.junit.Test;

/**
 * Esta clase prueba algunas expectativas sobre los mensajes
 * 
 * @author D. García
 */
public class TestMensajes {

	/**
	 * Verifico que el mapa que tiene la parte de ID es C.I. (Case insensitive)
	 */
	@Test
	public void elIdDelMensajeComoParteDelContenidoDeberiaSerCaseInsensitive() {
		final MensajeConContenido mensajeOriginal = MensajeConContenido.create(ContenidoMapa.create());
		// Le asignamos un ID
		final IdDeComponenteVortex idDeComponente = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final IdDeMensaje idDeMensaje = GeneradorSecuencialDeIdDeMensaje.create(idDeComponente).generarId();
		mensajeOriginal.asignarId(idDeMensaje);

		final ContenidoVortex contenido = mensajeOriginal.getContenido();
		final Object mapaDelId = contenido.get(ContenidoVortex.ID_DE_MENSAJE_KEY);
		Assert.assertTrue("Deberia ser un mapa insensitive", mapaDelId instanceof VortexMap);
	}

	/**
	 * Verifico que si me pasan un mapa que no es case sensitive en la raiz igual lo convierto a CI
	 */
	@Test
	public void siMetoUnMapaNoInsensitiveEnElContenidoDeberiaSerReemplazadoPorUnoQueSi() {
		final ContenidoMapa contenido = ContenidoMapa.create();
		final Map<String, Object> mapaNoCi = new HashMap<String, Object>();
		mapaNoCi.put("Hola", "manola");
		contenido.put("mapa", mapaNoCi);

		@SuppressWarnings("unchecked")
		final Map<String, Object> object = (Map<String, Object>) contenido.get("mapa");
		Assert.assertTrue("Deberia ser un mapa insensitive", object instanceof VortexMap);

		Assert.assertEquals("Deberia obtener el valor aunque la clave no coincide", "manola", object.get("hOlA"));
	}

	/**
	 * Verifico que si me pasan un mapa anidado en la raiz puedo detectarlo y convertirlo
	 */
	@Test
	public void siMetoUnMapaAnidadoDentroDeOtroEnElContenidoDeberiaSerReemplazadoPorUnCaseInsensitive() {
		// Creamos primero los mapas
		final Map<String, Object> mapaNoCiL1 = new HashMap<String, Object>();
		final Map<String, Object> mapaNoCiL2 = new HashMap<String, Object>();
		mapaNoCiL1.put("level", "1");
		mapaNoCiL1.put("mapa2", mapaNoCiL2);
		mapaNoCiL2.put("level", "2");

		final ContenidoMapa contenido = ContenidoMapa.create();
		contenido.put("mapa1", mapaNoCiL1);

		@SuppressWarnings("unchecked")
		final Map<String, Object> level1 = (Map<String, Object>) contenido.get("mapa1");
		@SuppressWarnings("unchecked")
		final Map<String, Object> level2 = (Map<String, Object>) level1.get("mapa2");
		Assert.assertTrue("Deberia ser un mapa insensitive", level2 instanceof VortexMap);

		Assert.assertEquals("Deberia obtener el valor aunque la clave no coincide", "2", level2.get("level"));

	}

	/**
	 * Verifico que si a mano modifico un submapa, no es posible convertirlo
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void siMetoUnMapaNoCaseInsensitiveDirectoEnUnSubMapaNoDeberiaConvertirlo() {
		// Metemos primero el mapa 1
		Map<String, Object> mapaNoCiL1 = new HashMap<String, Object>();
		mapaNoCiL1.put("level", "1");
		final ContenidoMapa contenido = ContenidoMapa.create();
		contenido.put("mapa1", mapaNoCiL1);

		// Una vez metido, modificamos el submapa convertido
		mapaNoCiL1 = (Map<String, Object>) contenido.get("mapa1");
		final Map<String, Object> mapaNoCiL2 = new HashMap<String, Object>();
		mapaNoCiL2.put("level", "2");
		mapaNoCiL1.put("mapa2", mapaNoCiL2);

		@SuppressWarnings("unchecked")
		final Map<String, Object> level1 = (Map<String, Object>) contenido.get("mapa1");
		@SuppressWarnings("unchecked")
		final Map<String, Object> level2 = (Map<String, Object>) level1.get("mapa2");
		Assert.assertFalse("No podemos detectar la modificacion y deberia ser un mapa comun",
				level2 instanceof VortexMap);

	}
}
