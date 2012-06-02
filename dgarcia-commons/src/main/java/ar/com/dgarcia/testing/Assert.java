/**
 * Created on 09/01/2007 23:09:45 Copyright (C) 2007 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.dgarcia.testing;

import java.util.Arrays;

/**
 * Esta clase modifica los metodos de asercion comunes de JUnit, incorporando algunos otros que
 * faltan
 * 
 * @version 1.0
 * @since 09/01/2007
 * @author D. Garcia
 */
public class Assert {

	/**
	 * Verifica que los numeros pasados sean iguales
	 * 
	 * @param expected
	 *            Valor esperado
	 * @param tested
	 *            Valor a comprobar
	 */
	public static void equals(final double expected, final double tested) {
		junit.framework.Assert.assertEquals(expected, tested);
	}

	/**
	 * Verifica que los numeros pasados sean iguales
	 * 
	 * @param expected
	 *            Valor esperado
	 * @param tested
	 *            Valor a comprobar
	 */
	public static void equals(final int expected, final int tested) {
		junit.framework.Assert.assertEquals(expected, tested);
	}

	/**
	 * Verifica que los dos objetos pasados sean iguales
	 * 
	 * @param expected
	 *            Objeto modelo
	 * @param tested
	 *            Objeto a comprobar
	 */
	public static void equals(final Object expected, final Object tested) {
		if (expected != null) {
			if (expected.getClass().isArray()) {
				equalsArrays(expected, tested);
				return;
			}
		}
		junit.framework.Assert.assertEquals(expected, tested);
	}

	/**
	 * Verifica que el objeto testeado sea un array igual al pasado. La definicion de igualdad
	 * utilizada es la misma que la de {@link Arrays}. Para los tipos primitivos dos arrays son
	 * considerados iguales si tienen el mismo tipo de componente (a diferencia de los objetos).
	 * 
	 * @param expected
	 *            Array esperado
	 * @param tested
	 *            Objeto a testear por igualdad
	 */
	private static void equalsArrays(final Object expected, final Object tested) {
		if (expected == null) {
			Null(tested);
		}
		final Class<?> expectedClass = expected.getClass();
		if (!expectedClass.isArray()) {
			fail("Se debe pasar un array como valor esperado:" + expected);
		}
		final Class<?> testedClass = tested.getClass();
		if (!testedClass.isArray()) {
			fail("El valor testeado no es un array expected:<" + expected + "> passed:<" + tested + ">");
		}
		final Class<?> expectedComponent = expectedClass.getComponentType();
		final Class<?> testedComponent = testedClass.getComponentType();
		if (expectedComponent.isPrimitive()) {
			True(testedComponent.isPrimitive());
			equals(expectedComponent, testedComponent);
			if (expectedComponent.equals(Boolean.TYPE)) {
				final boolean[] expectedArray = (boolean[]) expected;
				final boolean[] testedArray = (boolean[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Byte.TYPE)) {
				final byte[] expectedArray = (byte[]) expected;
				final byte[] testedArray = (byte[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Character.TYPE)) {
				final char[] expectedArray = (char[]) expected;
				final char[] testedArray = (char[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Short.TYPE)) {
				final short[] expectedArray = (short[]) expected;
				final short[] testedArray = (short[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Integer.TYPE)) {
				final int[] expectedArray = (int[]) expected;
				final int[] testedArray = (int[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Float.TYPE)) {
				final float[] expectedArray = (float[]) expected;
				final float[] testedArray = (float[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Double.TYPE)) {
				final double[] expectedArray = (double[]) expected;
				final double[] testedArray = (double[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else if (expectedComponent.equals(Long.TYPE)) {
				final long[] expectedArray = (long[]) expected;
				final long[] testedArray = (long[]) tested;
				True(Arrays.equals(expectedArray, testedArray));
			} else {
				fail("No hay un array de este tipo!");
			}
		} else {
			final Object[] expectedArray = (Object[]) expected;
			final Object[] testedArray = (Object[]) tested;
			True(Arrays.equals(expectedArray, testedArray));
		}
	}

	/**
	 * Verifica que la ejecucion de un bloque de codigo falle segun el tipo de excepcion indicada.
	 * Si no sucede asi, este metodo falla
	 * 
	 * @param <E>
	 *            Tipo de excepcion esperada
	 * @param code
	 *            Codigo a ejecutar
	 * @param exceptionType
	 *            Tipo de excepcion esperada
	 * @return La excepcion ocurrida
	 */
	@SuppressWarnings("unchecked")
	public static <E extends RuntimeException> E exceptionOn(final CodeThatShouldFail code, final Class<E> exceptionType) {
		try {
			code.doTheFaultyThing();
			junit.framework.Assert.fail("Se esperaba una excepcion: " + exceptionType);
		} catch (final RuntimeException e) {
			if (e.getClass().equals(exceptionType)) {
				return (E) e;
			}
			throw e;
		}
		// Nunca se llega realmente a este punto
		return null;
	}

	/**
	 * Falla la comprobacion con el mensaje indicado
	 * 
	 * @param message
	 *            Mensaje para mostrar como error
	 */
	public static void fail(final String message) {
		junit.framework.Assert.fail(message);
	}

	/**
	 * Verifica que el valor pasado sea igual a false La F mayuscula es necesaria ya que false es
	 * palabra reservada
	 * 
	 * @param tested
	 *            Valor a comprobar
	 */
	public static void False(final boolean tested) {
		junit.framework.Assert.assertFalse(tested);
	}

	/**
	 * Verifica que el valor pasado no sea nulo
	 * 
	 * @param value
	 *            Valor a comprobar
	 */
	public static void notNull(final Object value) {
		junit.framework.Assert.assertNotNull(value);
	}

	/**
	 * Verifica que el valor pasado sea null. La N mayuscula es necesaria ya que null es palabra
	 * reservada
	 * 
	 * @param value
	 *            Valor a comprobar
	 */
	public static void Null(final Object value) {
		junit.framework.Assert.assertNull(value);
	}

	/**
	 * Verifica que las instancias pasadas sean el mismo objeto
	 * 
	 * @param expected
	 * @param tested
	 */
	public static void same(final Object expected, final Object tested) {
		junit.framework.Assert.assertSame(expected, tested);
	}

	/**
	 * Verifica que el valor pasado sea true La T mayuscula es necesaria ya que true es palabra
	 * reservada
	 * 
	 * @param tested
	 *            Valor a comprobar
	 */
	public static void True(final boolean tested) {
		junit.framework.Assert.assertTrue(tested);
	}

	/**
	 * Verifica que el valor pasado sea true, mostrando un mensaje en caso contrario
	 * 
	 * @param descripcion
	 *            Descripcion a mostrar
	 * @param value
	 *            Valor a comprobar
	 */
	public static void True(final String descripcion, final boolean value) {
		junit.framework.Assert.assertTrue(descripcion, value);
	}
}
