/**
 * 15/01/2013 16:54:04 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.reflection.types;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Esta clase realiza algunos tests sobre los tipos con reflection y métodos adicionales a la api
 * base de reflection
 * 
 * @author D. García
 */
public class TestTiposReflexivos {

	public static class SuperA<T> {

	}

	public static class SubB extends SuperA<String> {

	}

	public static class MiddleC<M> extends SuperA<M> {

	}

	public static class SubD extends MiddleC<Integer> {

	}

	private List<String>[] atributoArrayGenerico;
	public static final String atributoArrayGenerico_FIELD = "atributoArrayGenerico";

	@Test
	public void deberiaObtenerUnTipoConcretoSiNoUsaGenerics() {
		@SuppressWarnings("rawtypes")
		final Type tipoObtenido = new TypeRef<List>() {
		}.getType();
		Assert.assertTrue("Debería ser un tipo concreto", tipoObtenido instanceof Class);
	}

	@Test
	public void deberiaObtenerUnTipoParametrizadoSiUsaGenerics() {
		final Type tipoObtenido = new TypeRef<List<String>>() {
		}.getType();
		Assert.assertFalse("Debería ser un tipo parametrico", tipoObtenido instanceof Class);
		Assert.assertTrue("Debería ser un tipo parametrico", tipoObtenido instanceof ParameterizedType);
	}

	@Test
	public void deberiaPermitirConocerElTipoConElQueSeParametrizaUnaSubclaseDirecta() {
		final Tipo tipoSubB = Tipos.desdeType(SubB.class);
		final Linaje linajeDeB = tipoSubB.getLinaje();
		final Tipo tipoDeA = linajeDeB.getTipoDe(SuperA.class);
		Assert.assertNotNull("El tipo A deberia estar en el linaje", tipoDeA);

		final List<Tipo> parametros = tipoDeA.getParametrosGenerics();
		Assert.assertEquals("Deberia tener un solo parametro", 1, parametros.size());

		final Tipo tipoParametro = parametros.get(0);
		Assert.assertEquals("Deberia estar parametrizado con String", String.class, tipoParametro.getClassInstance());
	}

	@Test
	public void deberiaPermitirConocerElTipoConElQueSeParametrizaUnaSubclaseIndirecta() {
		final Tipo tipoSubD = Tipos.desdeType(SubD.class);
		final Linaje linajeDeD = tipoSubD.getLinaje();
		final Tipo tipoDeA = linajeDeD.getTipoDe(SuperA.class);
		Assert.assertNotNull("El tipo A deberia estar en el linaje", tipoDeA);

		final List<Tipo> parametros = tipoDeA.getParametrosGenerics();
		Assert.assertEquals("Deberia tener un solo parametro", 1, parametros.size());

		final Tipo tipoParametro = parametros.get(0);
		Assert.assertEquals("Deberia estar parametrizado con Integer", Integer.class, tipoParametro.getClassInstance());
	}

	@Test
	public void deberiaPermitirConocerElTipoConElQueSeParametrizaUnaSubclaseAnonima() {
		final Tipo subTipoAnonimo = Tipos.desdeType(new SuperA<Long>() {
		}.getClass());
		final Linaje linajeDeAnonimo = subTipoAnonimo.getLinaje();
		final Tipo tipoDeA = linajeDeAnonimo.getTipoDe(SuperA.class);
		Assert.assertNotNull("El tipo A deberia estar en el linaje", tipoDeA);

		final List<Tipo> parametros = tipoDeA.getParametrosGenerics();
		Assert.assertEquals("Deberia tener un solo parametro", 1, parametros.size());

		final Tipo tipoParametro = parametros.get(0);
		Assert.assertEquals("Deberia estar parametrizado con Long", Long.class, tipoParametro.getClassInstance());
	}

	@Test
	public void deberiaPermitirRepresentarUnArrayGenerico() throws SecurityException, NoSuchFieldException {
		final Field field = getClass().getDeclaredField(atributoArrayGenerico_FIELD);
		final Tipo tipoDelAtributo = Tipos.desdeField(field);

		final Tipo tipoDelArray = tipoDelAtributo.getTipoDelArray();

		final List<Tipo> parametros = tipoDelArray.getParametrosGenerics();
		Assert.assertEquals("Deberia tener un solo parametro", 1, parametros.size());

		final Tipo tipoParametro = parametros.get(0);
		Assert.assertEquals("Deberia estar parametrizado con String", String.class, tipoParametro.getClassInstance());
	}

	@Test
	public void deberiaPermitirRepresentarUnTipoPrimitivo() {
		final Tipo tipoInt = Tipos.desdeType(int.class);
		Assert.assertNull("No deberia tener supertipos", tipoInt.getSuperTipo());

		final Linaje linaje = tipoInt.getLinaje();
		final List<Tipo> tiposDelLinaje = linaje.getTiposDesdeLaSubclase();
		Assert.assertEquals("El linaje de int solo lo incluye a si mismo", 1, tiposDelLinaje.size());
	}

}
