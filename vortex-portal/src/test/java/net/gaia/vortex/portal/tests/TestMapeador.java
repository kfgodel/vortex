/**
 * 17/06/2012 19:04:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.tests;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorDefault;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase prueba el mapeador de objeto a mensajes vortex
 * 
 * @author D. García
 */
public class TestMapeador {

	private MapeadorVortex mapeador;

	@Before
	public void crearMapeador() {
		mapeador = MapeadorDefault.create();
	}

	@Test
	public void deberiaConvertirElMensajeCronometroEnUnMensajeVortex() {
		// Creamos un mensaje con un valor en un atributo
		final MensajeCronometro cronoMensaje = MensajeCronometro.create();
		cronoMensaje.marcarInicio();

		final MensajeVortex mensajeVortex = mapeador.convertirAVortex(cronoMensaje);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia de mensaje vortex", mensajeVortex);
		final Object nanosEnElMensaje = mensajeVortex.getContenido().get(MensajeCronometro.nanosDeInicio_FIELD);
		Assert.assertEquals("El valor de nanos debería ser igual en el mensaje vortex",
				cronoMensaje.getNanosDeInicio(), nanosEnElMensaje);
	}

	@Test
	public void deberiaPermitirObtenerElValorDeUnAtributoEnElMensajeComoCaseInsensitive() {
		// Creamos un mensaje con un valor en un atributo
		final MensajeCronometro cronoMensaje = MensajeCronometro.create();
		cronoMensaje.marcarInicio();

		final MensajeVortex mensajeVortex = mapeador.convertirAVortex(cronoMensaje);
		final Object nanosEnElMensaje = mensajeVortex.getContenido().get(
				MensajeCronometro.nanosDeInicio_FIELD.toUpperCase());
		Assert.assertEquals("El valor de nanos debería ser igual en el mensaje vortex",
				cronoMensaje.getNanosDeInicio(), nanosEnElMensaje);
	}

	@Test
	public void deberiaPermitirConvertirDesdeMensajeAObjetoConCaseInsensitive() {
		final MensajeConContenido mensajeMapa = MensajeConContenido.create();
		final long nanosDelMensaje = 20;
		mensajeMapa.getContenido().put(MensajeCronometro.nanosDeInicio_FIELD.toUpperCase(), nanosDelMensaje);

		final MensajeCronometro cronoMensaje = mapeador.convertirDesdeVortex(mensajeMapa, MensajeCronometro.class);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia del mensaje crono", cronoMensaje);
		Assert.assertEquals("El valor de nanos debería ser igual en el mensaje vortex", nanosDelMensaje,
				cronoMensaje.getNanosDeInicio());
	}

	@Test
	public void deberiaConvertirElMensajeVortexEnUnMensajeCronometro() {
		final MensajeConContenido mensajeMapa = MensajeConContenido.create();
		final long nanosDelMensaje = 20;
		mensajeMapa.getContenido().put(MensajeCronometro.nanosDeInicio_FIELD, nanosDelMensaje);

		final MensajeCronometro cronoMensaje = mapeador.convertirDesdeVortex(mensajeMapa, MensajeCronometro.class);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia del mensaje crono", cronoMensaje);
		Assert.assertEquals("El valor de nanos debería ser igual en el mensaje vortex", nanosDelMensaje,
				cronoMensaje.getNanosDeInicio());
	}

	/**
	 * Esta caso prueba que las primitivas sean transformables también a mensajes vortex, porque en
	 * el caso de JSON no pueden ser interpretadas como mapas, y por lo tanto requieren una
	 * excepción en el código
	 */
	@Test
	public void deberiaPermitirConvertirStringEnMensajeVortex() {
		final String mensajeOriginal = "texto primitivo";
		final MensajeVortex convertido = mapeador.convertirAVortex(mensajeOriginal);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia de mensaje vortex", convertido);
		Assert.assertEquals("El valor del mensaje como primitiva debería ser igual al otro", mensajeOriginal,
				convertido.getValorComoPrimitiva());
	}

	@Test
	public void deberiaPermitirConvertirLongEnMensajeVortex() {
		final Long mensajeOriginal = 1L;
		final MensajeVortex convertido = mapeador.convertirAVortex(mensajeOriginal);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia de mensaje vortex", convertido);
		Assert.assertEquals("El valor del mensaje como primitiva debería ser igual al otro", mensajeOriginal,
				convertido.getValorComoPrimitiva());
	}

	@Test
	public void deberiaPermitirConvertirDoubleEnMensajeVortex() {
		final Double mensajeOriginal = 1.0;
		final MensajeVortex convertido = mapeador.convertirAVortex(mensajeOriginal);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia de mensaje vortex", convertido);
		Assert.assertEquals("El valor del mensaje como primitiva debería ser igual al otro", mensajeOriginal,
				convertido.getValorComoPrimitiva());
	}

	@Test
	public void deberiaPermitirConvertirUnArrayEnMensajeVortex() {
		final int[] mensajeOriginal = new int[] { 1, 2, 3 };
		final MensajeVortex convertido = mapeador.convertirAVortex(mensajeOriginal);
		Assert.assertNotNull("Deberíamos haber obtenido una instancia de mensaje vortex", convertido);
		Assert.assertEquals("El valor del mensaje como primitiva debería ser igual al otro", mensajeOriginal,
				convertido.getValorComoPrimitiva());
	}

	@Test
	public void noDeberiaPermitirConvertirNullEnMensajeVortex() {
		try {
			mapeador.convertirAVortex(null);
			Assert.fail("Debería haber rechazado a null en la conversion");
		} catch (final ErrorDeMapeoVortexException e) {
			// Es el error que esperábamos
		}
	}

	@Test
	public void deberiaPermitirObtenerNullComoValorDeUnaPropiedadNulaDelObjeto() {
		final MensajeModeloParaTests mensaje = MensajeModeloParaTests.create();
		mensaje.setEstadoAdicionalAlMensaje(null);
		final MensajeVortex convertido = mapeador.convertirAVortex(mensaje);
		final Object valorDeAtrinuto = convertido.getContenido().get("estadoAdicionalAlMensaje");
		Assert.assertNull("El valor del atributo debería ser null", valorDeAtrinuto);
	}

}
