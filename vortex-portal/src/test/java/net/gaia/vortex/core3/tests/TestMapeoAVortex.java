/**
 * 24/06/2012 23:51:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.tests;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorJson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.SystemChronometer;

/**
 * Esta clase prueba las conversiones de objetos a mensajes vortex y viceversa
 * 
 * @author D. García
 */
public class TestMapeoAVortex {
	private static final Logger LOG = LoggerFactory.getLogger(TestMapeoAVortex.class);

	private MapeadorVortex mapeadorVortex;

	@Before
	public void crearDependencias() {
		mapeadorVortex = MapeadorJson.create();
	}

	@Test
	public void deberiaTardadMenosDe1msEnConvertirUnObjetoSimpleIdaYVueltaAObjeto() {
		final Object objetoSimple = "Hola manola";
		checkPerformance(objetoSimple);
	}

	@Test
	public void deberiaTardadMenosDe1msEnConvertirUnObjetoComplejoIdaYVueltaAObjeto() {
		final ClaseParaProbarMapeo complejo = new ClaseParaProbarMapeo();
		complejo.setSubInstancia(new ClaseParaProbarMapeo());
		complejo.setEntero(0);
		complejo.setTexto("0");
		for (int i = 0; i < 100; i++) {
			final ClaseParaProbarMapeo subI = new ClaseParaProbarMapeo();
			subI.setSubInstancia(new ClaseParaProbarMapeo());
			subI.setEntero(i);
			subI.setTexto(String.valueOf(i));
			complejo.getSubInstancias().add(subI);
		}
		checkPerformance(complejo);
	}

	/**
	 * Verifica la performance de convertir el objeto pasado
	 * 
	 * @param objeto
	 *            El objeto a convertir ida y vuelta
	 */
	private void checkPerformance(final Object objeto) {
		final Class<? extends Object> tipoOriginal = objeto.getClass();
		final int cantidadDeConversiones = 100000;
		final SystemChronometer crono = SystemChronometer.create();
		for (int i = 0; i < cantidadDeConversiones; i++) {
			final MensajeVortex mensajeVortex = mapeadorVortex.convertirAVortex(objeto);
			mapeadorVortex.convertirDesdeVortex(mensajeVortex, tipoOriginal);
		}
		final double millisTranscurridos = crono.getElapsedMillis();
		LOG.debug("Se convirtieron ida y vuelta {} objetos[{}] en {} ms, a razón de {} objetos/ms", new Object[] {
				cantidadDeConversiones, objeto, millisTranscurridos, cantidadDeConversiones / millisTranscurridos });
		Assert.assertTrue("Si es más rápida que 1 ms, deberían haber más conversiones que milis",
				cantidadDeConversiones > millisTranscurridos);
	}

}
