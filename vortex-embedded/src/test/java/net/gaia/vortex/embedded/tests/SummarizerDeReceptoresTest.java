/**
 * 16/01/2012 19:41:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.embedded.tests;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesDelReceptor;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.tags.NotificacionDeCambioDeTags;
import net.gaia.vortex.lowlevel.impl.tags.ReporteCambioDeTags;
import net.gaia.vortex.lowlevel.impl.tags.SummarizerDeReceptores;
import net.gaia.vortex.lowlevel.impl.tags.TagChangeListener;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba el comportamiento esperado del sumarizador de tags del nodo.
 * 
 * @author D. García
 */
public class SummarizerDeReceptoresTest {

	private SummarizerDeReceptores summarizer;

	private TestTagListener listener;

	private final ReceptorVortex receptor1 = new TestReceptor();
	private final ReceptorVortex receptor2 = new TestReceptor();
	private final ReceptorVortex receptor3 = new TestReceptor();

	/**
	 * Clase de listener para estos tests
	 */
	public class TestTagListener implements TagChangeListener {
		public ReporteCambioDeTags reporte;

		@Override
		public void onTagChanges(final ReporteCambioDeTags reporte) {
			this.reporte = reporte;
		}
	}

	/**
	 * Receptor para estos tests
	 */
	public class TestReceptor implements ReceptorVortex {
		@Override
		public void recibir(final MensajeVortex mensaje) {
		}

		@Override
		public Set<String> getTagsNotificados() {
			return null;
		}

		@Override
		public void agregarTagsNotificados(final Set<String> tagsAgregados) {
		}

		@Override
		public void quitarTagsNotificados(final Set<String> tagsQuitados) {

		}

		@Override
		public ColaDeMensajesDelReceptor getColaDeMensajes() {
			return null;
		}
	}

	@Before
	public void createSummarizer() {
		listener = new TestTagListener();
		summarizer = SummarizerDeReceptores.create(listener);
	}

	@Test
	public void elListenerDeberiaEstarEnCeroSiNoSeModificarElSummarizer() {
		Assert.assertNull(listener.reporte);
	}

	@Test
	public void elSummarizerDeberiaTenerUnaListaVaciaDeTagsInicialmente() {
		final Set<String> globalTags = summarizer.getAllTags();
		Assert.assertTrue(globalTags.isEmpty());
	}

	@Test
	public void noDeberiaNotificarTagsAlPrimerClienteDelNodo() {
		final HashSet<String> tagsAgregados = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.agregarTagsPara(receptor1, tagsAgregados);

		Assert.assertNull(listener.reporte);
		Assert.assertEquals(tagsAgregados, summarizer.getAllTags());
	}

	@Test
	public void alConectarUnSegundoDeberiaNotificarLosTagsDelPrimero() {
		final HashSet<String> tagsDelReceptor1 = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.agregarTagsPara(receptor1, tagsDelReceptor1);
		summarizer.agregar(receptor2);

		checkNotificacionAgregados(receptor2, tagsDelReceptor1);
	}

	@Test
	public void alPublicarUnSegundoDeberiaNotificarAlPrimero() {
		final HashSet<String> tagsDelReceptor1 = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.agregarTagsPara(receptor1, tagsDelReceptor1);

		final HashSet<String> tagsDelReceptor2 = Sets.newHashSet("Tag2", "Tag3", "Tag4", "Tag5");
		summarizer.agregarTagsPara(receptor2, tagsDelReceptor2);

		checkNotificacionAgregados(receptor1, tagsDelReceptor2);
	}

	@Test
	public void deberiaReunirGlobalmenteLosTagsDeSusClientes() {
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", "Tag2", "Tag3"));

		final String tagNuevo1 = "Tag4";
		final String tagNuevo2 = "Tag5";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet("Tag2", "Tag3", tagNuevo1, tagNuevo2));

		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3", "Tag4", "Tag5"), summarizer.getAllTags());
	}

	@Test
	public void noDeberiaNotificarNadaSiSoloAgregamosUnTerceroConTagsCompartidos() {
		final String tagCompartido1 = "Tag2";
		final String tagCompartido2 = "Tag3";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido1, tagCompartido2));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido1, tagCompartido2));
		// Limpiamos antes del ultimo paso
		listener.reporte = null;
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido1, tagCompartido2));

		Assert.assertNull(listener.reporte);
	}

	@Test
	public void deberiaNotificarGlobalmenteSiAgregamosUnTerceroConTagsExclusivos() {
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", "Tag2", "Tag3"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet("Tag2", "Tag3", "Tag4", "Tag5"));
		final String tagExclusivo = "Tag6";
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet("Tag2", "Tag3", tagExclusivo));

		checkNotificacionAgregados(receptor1, Sets.newHashSet(tagExclusivo));
		checkNotificacionAgregados(receptor2, Sets.newHashSet(tagExclusivo));
	}

	@Test
	public void deberiaNotificarGlobalmenteSiSeQuitaUnTagExclusivoDeTercero() {
		final String tagExclusivo = "Tag1";
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagExclusivo, tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, "Tag3"));

		summarizer.quitarTagsPara(receptor1, Sets.newHashSet(tagExclusivo, tagCompartido));

		checkNotificacionQuitados(receptor2, Sets.newHashSet(tagExclusivo));
		checkNotificacionQuitados(receptor3, Sets.newHashSet(tagExclusivo));
	}

	@Test
	public void noDeberiaNotificarNadaSiSoloQuitamosUnTagCompartidoDeUnTercero() {
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag4"));
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, "Tag4"));

		// Limpiamos el estado antes del paso a comprobar
		listener.reporte = null;
		summarizer.quitarTagsPara(receptor3, Sets.newHashSet(tagCompartido));

		Assert.assertNull(listener.reporte);
	}

	/**
	 * Verifica que exista una notificación para el receptor indicado con los tags esperados como
	 * quitados
	 */
	private void checkNotificacionQuitados(final ReceptorVortex receptorATestear, final HashSet<String> tagsEsperados) {
		final ReporteCambioDeTags reporte = listener.reporte;
		Assert.assertNotNull("Debería existir un reporte de notificaciones", reporte);
		final NotificacionDeCambioDeTags notificacionParaReceptor = reporte.getNotificacionPara(receptorATestear);
		Assert.assertNotNull("Debería existir una notificacion para el receptor indicado", notificacionParaReceptor);
		Assert.assertEquals(tagsEsperados, notificacionParaReceptor.getTagsQuitados());
	}

	/**
	 * Verifica que exista una notificación para el receptor indicado con los tags esperados como
	 * agregados
	 */
	private void checkNotificacionAgregados(final ReceptorVortex receptorATestear, final HashSet<String> tagsEsperados) {
		final ReporteCambioDeTags reporte = listener.reporte;
		Assert.assertNotNull("Debería existir un reporte de notificaciones", reporte);
		final NotificacionDeCambioDeTags notificacionParaReceptor = reporte.getNotificacionPara(receptorATestear);
		Assert.assertNotNull("Debería existir una notificacion para el receptor indicado", notificacionParaReceptor);
		Assert.assertEquals(tagsEsperados, notificacionParaReceptor.getTagsAgregados());
	}

	@Test
	public void deberiaNotificarAgregadosYQuitadosAlReemplazar() {
		final String tagViejo = "Tag1";
		final String tagMantenido1 = "Tag2";
		final String tagMantenido2 = "Tag3";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagViejo, tagMantenido1, tagMantenido2));
		summarizer.agregarTagsPara(receptor2, Sets.<String> newHashSet());

		final String tagNuevo = "Tag4";
		summarizer.reemplazarTagsPara(receptor1, Sets.newHashSet(tagMantenido1, tagMantenido2, tagNuevo));

		checkNotificacionAgregados(receptor2, Sets.newHashSet(tagNuevo));
		checkNotificacionQuitados(receptor2, Sets.newHashSet(tagViejo));
	}

	@Test
	public void deberiaNotificarTodosLosTagsAlPrimeroSiQuitamosTodosDelSegundoReceptor() {
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido, "Tag3"));

		final String tagExclusivo = "Tag4";
		final HashSet<String> tagsDelSegundo = Sets.newHashSet(tagCompartido, tagExclusivo);
		summarizer.agregarTagsPara(receptor2, tagsDelSegundo);

		summarizer.limpiarTagsPara(receptor2);

		checkNotificacionQuitados(receptor1, tagsDelSegundo);
	}

	@Test
	public void deberiaQuitarSoloLosTagsDiferentesSiQuitamosTodosDeUnTercero() {
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido, "Tag3"));

		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag4"));
		final String tagExclusivo = "Tag5";
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, tagExclusivo));

		summarizer.limpiarTagsPara(receptor3);

		checkNotificacionQuitados(receptor1, Sets.newHashSet(tagExclusivo));
		checkNotificacionQuitados(receptor2, Sets.newHashSet(tagExclusivo));
	}

	@Test
	public void deberiaNotificarAlPrimeroTodosLosReemplazadosDelSegundoSiEslaPrimeraVez() {
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1"));

		final HashSet<String> tagsReemplazados = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.reemplazarTagsPara(receptor2, tagsReemplazados);

		checkNotificacionAgregados(receptor1, tagsReemplazados);
	}

	@Test
	public void deberiaNotificarAlUltimoDelLosTagsQuitadosDelAnteultimo() {
		final String tagCompartido = "Tag1";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagCompartido, "Tag2"));
		final String tagExclusivoDelSegundo = "Tag3";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, tagExclusivoDelSegundo));
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, "Tag4"));

		summarizer.limpiarTagsPara(receptor3);
		summarizer.limpiarTagsPara(receptor2);
		checkNotificacionQuitados(receptor1, Sets.newHashSet(tagCompartido, tagExclusivoDelSegundo));
	}

	@Test
	public void deberiaNotificarAlUltimoDelTagQuitadoSiEsCompartido() {
		final String tagCompartido = "Tag1";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagCompartido, "Tag2"));
		final String tagExclusivoDelSegundo = "Tag3";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, tagExclusivoDelSegundo));
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, "Tag4"));

		summarizer.limpiarTagsPara(receptor3);
		summarizer.quitarTagsPara(receptor2, Sets.newHashSet(tagExclusivoDelSegundo));
		checkNotificacionQuitados(receptor1, Sets.newHashSet(tagExclusivoDelSegundo));
	}

	@Test
	public void deberiaQuitarTodosSiSeLimpiaElUltimoReceptor() {
		final String tagCompartido = "Tag1";
		final HashSet<String> tagsDelReceptot1 = Sets.newHashSet(tagCompartido, "Tag2");
		summarizer.agregarTagsPara(receptor1, tagsDelReceptot1);
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag3"));

		summarizer.limpiarTagsPara(receptor2);
		summarizer.limpiarTagsPara(receptor1);
		Assert.assertEquals(Sets.newHashSet(), summarizer.getAllTags());
	}

	@Test
	public void deberiaEliminarTodosLosTagsAlQuitarUnReceptor() {
		final HashSet<String> tagsDelPrimero = Sets.newHashSet("Tag1", "Tag2");
		summarizer.agregarTagsPara(receptor1, tagsDelPrimero);

		summarizer.quitar(receptor1);

		Assert.assertEquals(Sets.newHashSet(), summarizer.getAllTags());
		Assert.assertEquals(Sets.newHashSet(), summarizer.getAllReceptores());
	}
}
