/**
 * 28/08/2011 11:28:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests;

import java.util.List;

import junit.framework.Assert;
import net.gaia.vortex.persistibles.MensajeVortexPersistible;
import net.gaia.vortex.persistibles.ReceptorHttp;
import net.gaia.vortex.services.ServicioRuteo;

import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tenpines.commons.persistence.repositories.GenericRepository;

/**
 * Esta clase verifica el comportamiento del servicio de ruteo
 * 
 * @author D. García
 */
public class ServicioRuteoTests extends VortexTest {

	@Autowired
	private ServicioRuteo servicioRuteo;

	@Autowired
	private EscenarioDeTest escenarioDeTest;

	@Autowired
	private GenericRepository genericRepository;

	@Test
	public void deberiaLimpiarMensajesViejosSinReceptores() {
		// No deberían existir mensajes anteriores
		final List<MensajeVortexPersistible> preexistentes = genericRepository
				.findAllOf(MensajeVortexPersistible.class);
		Assert.assertTrue(preexistentes.isEmpty());

		// Creo varios mensajes, unos viejos y otros no
		final MensajeVortexPersistible mensajeNuevo = escenarioDeTest.crearMensajePersistible();
		final DateTime fechaDelNuevo = new DateTime();
		mensajeNuevo.setCreationMoment(fechaDelNuevo);
		genericRepository.save(mensajeNuevo);

		final MensajeVortexPersistible mensajeViejo = escenarioDeTest.crearMensajePersistible();
		mensajeViejo.setCreationMoment(new DateTime(1980, 1, 1, 12, 0, 59, 0));
		genericRepository.save(mensajeViejo);

		final List<MensajeVortexPersistible> creados = genericRepository.findAllOf(MensajeVortexPersistible.class);
		Assert.assertEquals(2, creados.size());

		// Ejecutamos la limpieza
		servicioRuteo.limpiarMensajesViejos();

		// Verifico que esté borrado el viejo
		final List<MensajeVortexPersistible> preservados = genericRepository.findAllOf(MensajeVortexPersistible.class);
		Assert.assertEquals(1, preservados.size());

		final MensajeVortexPersistible mensajePreservado = preservados.get(0);
		Assert.assertEquals(fechaDelNuevo, mensajePreservado.getCreationMoment());
	}

	@Test
	public void deberiaLimpiarReceptoresEnDesuso() {
		// No deberían existir mensajes anteriores
		final List<ReceptorHttp> preexistentes = genericRepository.findAllOf(ReceptorHttp.class);
		Assert.assertTrue(preexistentes.isEmpty());

		// Creo varios receptores, unos viejos y otros no
		final ReceptorHttp receptorNuevo = ReceptorHttp.create();
		final DateTime momentoDelNuevo = new DateTime();
		receptorNuevo.setMomentoUltimaComunicacion(momentoDelNuevo);
		genericRepository.save(receptorNuevo);

		final ReceptorHttp receptorViejo = ReceptorHttp.create();
		receptorViejo.setMomentoUltimaComunicacion(new DateTime(1980, 1, 1, 12, 0, 59, 0));
		genericRepository.save(receptorViejo);

		final List<ReceptorHttp> creados = genericRepository.findAllOf(ReceptorHttp.class);
		Assert.assertEquals(2, creados.size());

		// Limpiamos la base de receptores viejos
		servicioRuteo.limpiarReceptoresViejos();

		// Verifico que estén borrados
		final List<ReceptorHttp> preservados = genericRepository.findAllOf(ReceptorHttp.class);
		Assert.assertEquals(1, preservados.size());

		final ReceptorHttp receptorPreservado = preservados.get(0);
		Assert.assertEquals(momentoDelNuevo, receptorPreservado.getMomentoUltimaComunicacion());
	}

	@Ignore("Este test falla porque no se persiste en la base realmente. Debería funcionar bien")
	@Test
	public void noDeberiaBorrarMensajeViejoSiEstaVinculadoAUnReceptorActual() {
		// Creo el mensaje viejo asociado a un receptor nuevo
		final MensajeVortexPersistible mensajeViejo = escenarioDeTest.crearMensajePersistible();
		final DateTime fechaMensajeViejo = new DateTime(1980, 1, 1, 12, 0, 59, 0);
		mensajeViejo.setCreationMoment(fechaMensajeViejo);

		final ReceptorHttp receptorNuevo = ReceptorHttp.create();
		final DateTime momentoDelNuevo = new DateTime();
		receptorNuevo.setMomentoUltimaComunicacion(momentoDelNuevo);
		receptorNuevo.getPendientes().add(mensajeViejo);
		genericRepository.save(receptorNuevo);

		// Verificamos que se hayan guardado cada uno
		final List<MensajeVortexPersistible> mensajesCreados = genericRepository
				.findAllOf(MensajeVortexPersistible.class);
		Assert.assertEquals(1, mensajesCreados.size());
		final List<ReceptorHttp> receptoresCreados = genericRepository.findAllOf(ReceptorHttp.class);
		Assert.assertEquals(1, receptoresCreados.size());

		// Limpiamos mensajes viejos
		servicioRuteo.limpiarMensajesViejos();

		// Debería conservarse el mensaje por estar asociado a un receptor
		final List<MensajeVortexPersistible> preservados = genericRepository.findAllOf(MensajeVortexPersistible.class);
		Assert.assertEquals(1, preservados.size());

		final MensajeVortexPersistible mensajePreservado = preservados.get(0);
		Assert.assertEquals(fechaMensajeViejo, mensajePreservado.getCreationMoment());
	}
}
