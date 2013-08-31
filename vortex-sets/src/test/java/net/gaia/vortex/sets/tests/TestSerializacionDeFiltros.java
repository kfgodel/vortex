/**
 * 21/01/2013 11:39:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoEmpieza;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ColeccionContiene;
import net.gaia.vortex.sets.impl.condiciones.CondicionDesconocida;
import net.gaia.vortex.sets.impl.condiciones.Negacion;
import net.gaia.vortex.sets.impl.condiciones.OrCompuesto;
import net.gaia.vortex.sets.impl.condiciones.TextoRegexMatchea;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones;
import net.gaia.vortex.sets.impl.serializacion.impl.SerializadorDeCondicionesImpl;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase prueba la serialización de filtros a mapas enviables por red
 * 
 * @author D. García
 */
public class TestSerializacionDeFiltros {

	private SerializadorDeCondiciones serializador;

	@Before
	public void crearSerializador() {
		serializador = SerializadorDeCondicionesImpl.create();
	}

	@Test
	public void deberiaSerializarLaCondicionTrue() {
		final Map<String, Object> serializada = serializador.serializar(SiempreTrue.getInstancia());
		Assert.assertEquals(MetadataDeSerializacion.TIPO_TRUE, serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
	}

	@Test
	public void deberiaDeserializarLaCondicionTrue() {
		final Map<String, Object> mapaTrue = new HashMap<String, Object>();
		mapaTrue.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_TRUE);
		final Condicion condicion = serializador.deserializar(mapaTrue);
		Assert.assertTrue(condicion instanceof SiempreTrue);
	}

	@Test
	public void deberiaSerializarLaCondicionFalse() {
		final Map<String, Object> serializada = serializador.serializar(SiempreFalse.getInstancia());
		Assert.assertEquals(MetadataDeSerializacion.TIPO_FALSE, serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
	}

	@Test
	public void deberiaDeserializarLaCondicionFalse() {
		final Map<String, Object> mapaFalse = new HashMap<String, Object>();
		mapaFalse.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_FALSE);
		final Condicion condicion = serializador.deserializar(mapaFalse);
		Assert.assertTrue(condicion instanceof SiempreFalse);
	}

	@Test
	public void deberiaRepresentarComoAnonimaUnaCondicionSinConfiguracion() {
		final Condicion condicionAnonima = new Condicion() {
			
			public List<Condicion> getSubCondiciones() {
				return null;
			}

			
			public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
				return null;
			}
		};
		final Map<String, Object> serializada = serializador.serializar(condicionAnonima);

		Assert.assertEquals(MetadataDeSerializacion.TIPO_ANONIMO,
				serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
		Assert.assertEquals(condicionAnonima.getClass().getName(),
				serializada.get(MetadataDeSerializacion.TIPO_ANONIMO_CLASE));
	}

	@Test
	public void deberiaRepresentarComoMapaUnaCondicionClaveValor() {
		final ValorEsperadoEn condicion = ValorEsperadoEn.elAtributo("atributo", "texto");
		final Map<String, Object> serializada = serializador.serializar(condicion);
		checkSerializadoComoEq(serializada, "atributo", "texto");
	}

	@Test
	public void deberiaRepresentarComoMapaUnaCondicionPorNot() {
		final Condicion condicion = Negacion.de(ValorEsperadoEn.elAtributo("atributo", "valor"));
		final Map<String, Object> serializada = serializador.serializar(condicion);

		Assert.assertEquals(MetadataDeSerializacion.TIPO_NOT, serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
		final Object object = serializada.get(MetadataDeSerializacion.TIPO_NOT_FILTRO);

		checkSerializadoComoEq(object, "atributo", "valor");
	}

	/**
	 * Verifica que el ojeto pasado esté correctamente serializado como condicion EQ
	 */
	private void checkSerializadoComoEq(final Object object, final String atributoEsperado, final String valorEsperado) {
		final Map<String, Object> estadoEsperado = new HashMap<String, Object>();
		estadoEsperado.put(MetadataDeSerializacion.TIPO_EQUALS_CLAVE, atributoEsperado);
		estadoEsperado.put(MetadataDeSerializacion.TIPO_EQUALS_VALOR, valorEsperado);

		checkSerializado(object, MetadataDeSerializacion.TIPO_EQUALS, estadoEsperado);
	}

	/**
	 * @param object
	 * @param tipoEsperado
	 * @param estadoEsperado
	 */
	private void checkSerializado(final Object object, final String tipoEsperado,
			final Map<String, Object> estadoEsperado) {
		Assert.assertTrue(object instanceof Map);
		@SuppressWarnings("unchecked")
		final Map<String, Object> mapaDeCondicionEq = (Map<String, Object>) object;

		Assert.assertEquals(tipoEsperado, mapaDeCondicionEq.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
		final Set<Entry<String, Object>> entriesEsperadas = estadoEsperado.entrySet();
		for (final Entry<String, Object> entryEsperada : entriesEsperadas) {
			final String clave = entryEsperada.getKey();
			final Object valueEsperado = entryEsperada.getValue();
			Assert.assertEquals(valueEsperado, mapaDeCondicionEq.get(clave));
		}
	}

	@Test
	public void deberiaRepresentarComoMapaUnaCondicionPorAnd() {
		final Condicion condicion = AndCompuesto.de(ValorEsperadoEn.elAtributo("atributo1", "valor1"),
				ValorEsperadoEn.elAtributo("atributo2", "valor2"));
		final Map<String, Object> serializada = serializador.serializar(condicion);

		Assert.assertEquals(MetadataDeSerializacion.TIPO_AND, serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
		final Object object = serializada.get(MetadataDeSerializacion.TIPO_AND_FILTROS);
		Assert.assertTrue(object instanceof List);
		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> subCondiciones = (List<Map<String, Object>>) object;

		Assert.assertEquals(2, subCondiciones.size());

		checkSerializadoComoEq(subCondiciones.get(0), "atributo1", "valor1");
		checkSerializadoComoEq(subCondiciones.get(1), "atributo2", "valor2");
	}

	@Test
	public void deberiaRepresentarComoMapaUnaCondicionPorOr() {
		final Condicion condicion = OrCompuesto.de(ValorEsperadoEn.elAtributo("atributo1", "valor1"),
				ValorEsperadoEn.elAtributo("atributo2", "valor2"));
		final Map<String, Object> serializada = serializador.serializar(condicion);

		Assert.assertEquals(MetadataDeSerializacion.TIPO_OR, serializada.get(MetadataDeSerializacion.ATRIBUTO_TIPO));
		final Object object = serializada.get(MetadataDeSerializacion.TIPO_OR_FILTROS);
		Assert.assertTrue(object instanceof List);
		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> subCondiciones = (List<Map<String, Object>>) object;

		Assert.assertEquals(2, subCondiciones.size());

		checkSerializadoComoEq(subCondiciones.get(0), "atributo1", "valor1");
		checkSerializadoComoEq(subCondiciones.get(1), "atributo2", "valor2");
	}

	@Test
	public void deberiaReconstruirComoDesconocidaUnaCondicionConTipoNoEstandar() {
		final Map<String, Object> mapaCondicion = new HashMap<String, Object>();
		mapaCondicion.put(MetadataDeSerializacion.ATRIBUTO_TIPO, "tipoReloco");
		mapaCondicion.put("atributo1", "valor1");

		final Condicion condicion = serializador.deserializar(mapaCondicion);
		Assert.assertTrue(condicion instanceof CondicionDesconocida);
		final CondicionDesconocida desconocida = (CondicionDesconocida) condicion;

		final Map<String, Object> formaOriginal = desconocida.getFormaOriginal();
		Assert.assertNotNull(formaOriginal);
		Assert.assertSame("Debería preservar el mapa original como dato para reconstruir", mapaCondicion, formaOriginal);
	}

	@Test
	public void deberiaReconstruirDesdeMapaUnaCondicionClaveValor() {
		final String atributoEsperado = "atributo";
		final String valorEsperado = "valor";
		final Map<String, Object> mapaEq = crearMapaEq(atributoEsperado, valorEsperado);

		final Condicion condicion = serializador.deserializar(mapaEq);

		checkDeserializadoComoEq(condicion, atributoEsperado, valorEsperado);
	}

	/**
	 * Verifica que la condicion pasada representa una condicion por igualdad con el atributo y
	 * valor pasados
	 */
	private void checkDeserializadoComoEq(final Condicion condicion, final String atributoEsperado,
			final String valorEsperado) {
		Assert.assertTrue(condicion instanceof ValorEsperadoEn);
		final ValorEsperadoEn condicionEq = (ValorEsperadoEn) condicion;

		Assert.assertEquals(atributoEsperado, condicionEq.getValueAccessor().getPropertyPath());
		Assert.assertEquals(valorEsperado, condicionEq.getValorEsperado());
	}

	/**
	 * @param atributoEsperado
	 * @param valorEsperado
	 * @return
	 */
	private Map<String, Object> crearMapaEq(final String atributoEsperado, final String valorEsperado) {
		final Map<String, Object> mapaEq = new HashMap<String, Object>();
		mapaEq.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_EQUALS);
		mapaEq.put(MetadataDeSerializacion.TIPO_EQUALS_CLAVE, atributoEsperado);
		mapaEq.put(MetadataDeSerializacion.TIPO_EQUALS_VALOR, valorEsperado);
		return mapaEq;
	}

	@Test
	public void deberiaReconstruirDesdeMapaUnaCondicionPorNot() {
		final String atributoEsperado = "atributo";
		final String valorEsperado = "valor";
		final Map<String, Object> mapaEq = crearMapaEq(atributoEsperado, valorEsperado);

		final HashMap<String, Object> mapaNot = new HashMap<String, Object>();
		mapaNot.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_NOT);
		mapaNot.put(MetadataDeSerializacion.TIPO_NOT_FILTRO, mapaEq);

		final Condicion condicion = serializador.deserializar(mapaNot);
		Assert.assertTrue(condicion instanceof Negacion);
		final Negacion condicionNot = (Negacion) condicion;
		final Condicion condicionEq = condicionNot.getCondicionNegada();
		checkDeserializadoComoEq(condicionEq, atributoEsperado, valorEsperado);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deberiaReconstruirDesdeMapaUnaCondicionPorAnd() {
		final HashMap<String, Object> mapaAnd = new HashMap<String, Object>();
		mapaAnd.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_AND);
		mapaAnd.put(MetadataDeSerializacion.TIPO_AND_FILTROS,
				Arrays.asList(crearMapaEq("atributo1", "valor1"), crearMapaEq("atributo2", "valor2")));

		final Condicion condicion = serializador.deserializar(mapaAnd);
		Assert.assertTrue(condicion instanceof AndCompuesto);
		final AndCompuesto condicionAnd = (AndCompuesto) condicion;

		final List<Condicion> subCondiciones = condicionAnd.getCondiciones();
		Assert.assertEquals(2, subCondiciones.size());

		checkDeserializadoComoEq(subCondiciones.get(0), "atributo1", "valor1");
		checkDeserializadoComoEq(subCondiciones.get(1), "atributo2", "valor2");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deberiaReconstruirDesdeMapaUnaCondicionPorOr() {
		final HashMap<String, Object> mapaOr = new HashMap<String, Object>();
		mapaOr.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_OR);
		mapaOr.put(MetadataDeSerializacion.TIPO_OR_FILTROS,
				Arrays.asList(crearMapaEq("atributo1", "valor1"), crearMapaEq("atributo2", "valor2")));

		final Condicion condicion = serializador.deserializar(mapaOr);
		Assert.assertTrue(condicion instanceof OrCompuesto);
		final OrCompuesto condicionOr = (OrCompuesto) condicion;

		final List<Condicion> subCondiciones = condicionOr.getCondiciones();
		Assert.assertEquals(2, subCondiciones.size());

		checkDeserializadoComoEq(subCondiciones.get(0), "atributo1", "valor1");
		checkDeserializadoComoEq(subCondiciones.get(1), "atributo2", "valor2");
	}

	@Test
	public void deberiaPreservarLaFormaDeUnaCondicionDesconocida() {
		final Map<String, Object> mapaDeFormaDesconocida = new HashMap<String, Object>();
		mapaDeFormaDesconocida.put(MetadataDeSerializacion.ATRIBUTO_TIPO, "tipoReloco");
		mapaDeFormaDesconocida.put("atributo1", "valor1");

		final Condicion condicion = serializador.deserializar(mapaDeFormaDesconocida);
		final Map<String, Object> serializado = serializador.serializar(condicion);

		Assert.assertSame("Al pasar como desconocida no debería alterar la forma original", mapaDeFormaDesconocida,
				serializado);
	}

	@Test
	public void deberiaSerializarUnFiltroPorEmpieza() {
		final Map<String, Object> serializado = serializador.serializar(AtributoEmpieza.conPrefijo("prefijo",
				"atributo"));
		final Map<String, Object> estadoEsperado = new HashMap<String, Object>();
		estadoEsperado.put(MetadataDeSerializacion.TIPO_EMPIEZA_CLAVE, "atributo");
		estadoEsperado.put(MetadataDeSerializacion.TIPO_EMPIEZA_PREFIJO, "prefijo");

		checkSerializado(serializado, MetadataDeSerializacion.TIPO_EMPIEZA, estadoEsperado);
	}

	@Test
	public void deberiaReconstruirUnFiltroPorEmpieza() {
		final Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_EMPIEZA);
		mapa.put(MetadataDeSerializacion.TIPO_EMPIEZA_CLAVE, "atributo");
		mapa.put(MetadataDeSerializacion.TIPO_EMPIEZA_PREFIJO, "prefijo");

		final Condicion deserializado = serializador.deserializar(mapa);
		Assert.assertTrue(deserializado instanceof AtributoEmpieza);
		final AtributoEmpieza empieza = (AtributoEmpieza) deserializado;
		Assert.assertEquals("atributo", empieza.getValueAccessor().getPropertyPath());
		Assert.assertEquals("prefijo", empieza.getPrefijoEsperado());
	}

	@Test
	public void deberiaSerializarUnFiltroPorPresente() {
		final Map<String, Object> serializado = serializador.serializar(AtributoPresente.conNombre("atributo"));
		final Map<String, Object> estadoEsperado = new HashMap<String, Object>();
		estadoEsperado.put(MetadataDeSerializacion.TIPO_PRESENTE_CLAVE, "atributo");

		checkSerializado(serializado, MetadataDeSerializacion.TIPO_PRESENTE, estadoEsperado);
	}

	@Test
	public void deberiaReconstruirUnFiltroPorPresente() {
		final Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_PRESENTE);
		mapa.put(MetadataDeSerializacion.TIPO_PRESENTE_CLAVE, "atributo");

		final Condicion deserializado = serializador.deserializar(mapa);
		Assert.assertTrue(deserializado instanceof AtributoPresente);
		final AtributoPresente presente = (AtributoPresente) deserializado;
		Assert.assertEquals("atributo", presente.getValueAccessor().getPropertyPath());
	}

	@Test
	public void deberiaSerializarUnFiltroPorContiene() {
		final Map<String, Object> serializado = serializador.serializar(ColeccionContiene.alValor("valor", "atributo"));
		final Map<String, Object> estadoEsperado = new HashMap<String, Object>();
		estadoEsperado.put(MetadataDeSerializacion.TIPO_CONTIENE_CLAVE, "atributo");
		estadoEsperado.put(MetadataDeSerializacion.TIPO_CONTIENE_VALOR, "valor");

		checkSerializado(serializado, MetadataDeSerializacion.TIPO_CONTIENE, estadoEsperado);

	}

	@Test
	public void deberiaReconstruirUnFiltroPorContiene() {
		final Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_CONTIENE);
		mapa.put(MetadataDeSerializacion.TIPO_CONTIENE_CLAVE, "atributo");
		mapa.put(MetadataDeSerializacion.TIPO_CONTIENE_VALOR, "valor");

		final Condicion deserializado = serializador.deserializar(mapa);
		Assert.assertTrue(deserializado instanceof ColeccionContiene);
		final ColeccionContiene contiene = (ColeccionContiene) deserializado;
		Assert.assertEquals("atributo", contiene.getValueAccessor().getPropertyPath());
		Assert.assertEquals("valor", contiene.getValorEsperado());
	}

	@Test
	public void deberiaSerializarUnFiltroPorRegex() {
		final Map<String, Object> serializado = serializador.serializar(TextoRegexMatchea.laExpresion("expresion",
				"atributo"));
		final Map<String, Object> estadoEsperado = new HashMap<String, Object>();
		estadoEsperado.put(MetadataDeSerializacion.TIPO_REGEX_CLAVE, "atributo");
		estadoEsperado.put(MetadataDeSerializacion.TIPO_REGEX_EXPRESION, "expresion");

		checkSerializado(serializado, MetadataDeSerializacion.TIPO_REGEX, estadoEsperado);
	}

	@Test
	public void deberiaReconstruirUnFiltroPorRegex() {
		final Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_REGEX);
		mapa.put(MetadataDeSerializacion.TIPO_REGEX_CLAVE, "atributo");
		mapa.put(MetadataDeSerializacion.TIPO_REGEX_EXPRESION, "expresion");

		final Condicion deserializado = serializador.deserializar(mapa);
		Assert.assertTrue(deserializado instanceof TextoRegexMatchea);
		final TextoRegexMatchea regex = (TextoRegexMatchea) deserializado;
		Assert.assertEquals("atributo", regex.getValueAccessor().getPropertyPath());
		Assert.assertEquals("expresion", regex.getExpresion().toString());
	}

}
