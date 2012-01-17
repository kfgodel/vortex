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
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.TagsDeReceptor;
import net.gaia.vortex.lowlevel.impl.tags.TagChangeListener;
import net.gaia.vortex.lowlevel.impl.tags.TagSummarizer;
import net.gaia.vortex.lowlevel.impl.tags.TagSummarizerImpl;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba el comportamiento esperado del sumarizador de tags del nodo.
 * 
 * @author D. García
 */
public class TagSummarizerTest {

	private TagSummarizer summarizer;

	private TestTagListener listener;

	private final ReceptorVortex receptor1 = new TestReceptor();
	private final ReceptorVortex receptor2 = new TestReceptor();
	private final ReceptorVortex receptor3 = new TestReceptor();

	/**
	 * Clase de listener para estos tests
	 */
	public class TestTagListener implements TagChangeListener {

		public Set<String> tagAgregadosGlobalmente;
		public Set<String> tagQuitadosGlobalmente;

		@Override
		public void onTagsAgregadosGlobalmente(final Set<String> tagAgregadosGlobalmente,
				final ReceptorVortex receptorQueLosAgrega) {
			this.tagAgregadosGlobalmente = tagAgregadosGlobalmente;
		}

		@Override
		public void onTagsQuitadosGlobalmente(final Set<String> tagQuitadosGlobalmente,
				final ReceptorVortex receptorQueLosQuito) {
			this.tagQuitadosGlobalmente = tagQuitadosGlobalmente;
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
		public boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje) {
			return false;
		}

		@Override
		public boolean envioPreviamente(final IdVortex idDeMensajeAConfirmar) {
			return false;
		}

		@Override
		public TagsDeReceptor getTags() {
			return null;
		}
	}

	@Before
	public void createSummarizer() {
		listener = new TestTagListener();
		summarizer = TagSummarizerImpl.create(listener);
	}

	@Test
	public void elListenerDeberiaEstarEnCeroSiNoSeModificarElSummarizer() {
		Assert.assertNull(listener.tagAgregadosGlobalmente);
		Assert.assertNull(listener.tagQuitadosGlobalmente);
	}

	@Test
	public void elSummarizerDeberiaTenerUnaListaVaciaDeTagsInicialmente() {
		final Set<String> globalTags = summarizer.getGlobalTags();
		Assert.assertTrue(globalTags.isEmpty());
	}

	@Test
	public void deberiaAgregarGlobalmenteTodosLosTagsDelPrimerReceptor() {
		final HashSet<String> tagsAgregados = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.agregarTagsPara(receptor1, tagsAgregados);

		Assert.assertEquals(tagsAgregados, listener.tagAgregadosGlobalmente);
		Assert.assertEquals(tagsAgregados, summarizer.getGlobalTags());
	}

	@Test
	public void deberiaAgregarGlobalmenteSoloLosTagsNuevosDelSegundoReceptor() {
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", "Tag2", "Tag3"));

		final String tagNuevo1 = "Tag4";
		final String tagNuevo2 = "Tag5";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet("Tag2", "Tag3", tagNuevo1, tagNuevo2));

		final Set<String> tagRealmenteNuevos = Sets.newHashSet(tagNuevo1, tagNuevo2);
		Assert.assertEquals(tagRealmenteNuevos, listener.tagAgregadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3", "Tag4", "Tag5"), summarizer.getGlobalTags());
	}

	@Test
	public void noDeberiaAgregarNadaSiSoloAgregamosTagsCompartidos() {
		final String tagCompartido1 = "Tag2";
		final String tagCompartido2 = "Tag3";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido1, tagCompartido2));
		// Limpiamos el listener para confirmar el test
		listener.tagAgregadosGlobalmente = null;

		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido1, tagCompartido2));
		Assert.assertNull(listener.tagAgregadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3"), summarizer.getGlobalTags());
	}

	@Test
	public void noDeberiaQuitarNadaSiSoloQuitamosDelSegundoReceptorUnTagCompartido() {
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag4"));

		summarizer.quitarTagsPara(receptor2, Sets.newHashSet(tagCompartido));

		Assert.assertNull(listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3", "Tag4"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaQuitarGlobalmenteSiSeQuitaUnTagExclusivoDeUnReceptor() {
		final String tagExclusivo = "Tag1";
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagExclusivo, tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag3"));

		summarizer.quitarTagsPara(receptor1, Sets.newHashSet(tagExclusivo, tagCompartido));

		Assert.assertEquals(Sets.newHashSet(tagExclusivo), listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag2", "Tag3"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaAgregarLosNuevosYQuitarLosViejosAlReemplazar() {
		final String tagViejo = "Tag1";
		final String tagMantenido1 = "Tag2";
		final String tagMantenido2 = "Tag3";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagViejo, tagMantenido1, tagMantenido2));

		final String tagNuevo = "Tag4";
		summarizer.reemplazarTagsPara(receptor1, Sets.newHashSet(tagMantenido1, tagMantenido2, tagNuevo));

		Assert.assertEquals(Sets.newHashSet(tagViejo), listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet(tagNuevo), listener.tagAgregadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag2", "Tag3", "Tag4"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaAgregarLosTagsNuevosYQuitarLosViejosSiSeReemplazanTodosLosDelSegundoReceptor() {
		final String tagMantenido1 = "Tag2";
		final String tagMantenido2 = "Tag3";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagMantenido1, tagMantenido2));
		final String tagViejo = "Tag4";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagMantenido1, tagMantenido2, tagViejo));

		final String tagNuevo = "Tag5";
		summarizer.reemplazarTagsPara(receptor2, Sets.newHashSet(tagMantenido1, tagMantenido2, tagNuevo));

		Assert.assertEquals(Sets.newHashSet(tagViejo), listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet(tagNuevo), listener.tagAgregadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3", "Tag5"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaQuitarSoloLosTagsDiferentesSiQuitamosTodosDelSegundoReceptor() {
		final String tagCompartido = "Tag2";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet("Tag1", tagCompartido, "Tag3"));

		final String tagExclusivo = "Tag4";
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, tagExclusivo));

		summarizer.limpiarTagsPara(receptor2);
		Assert.assertEquals(Sets.newHashSet(tagExclusivo), listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaDarComoAgregadoTodosLosReemplazadosSiEslaPrimeraVez() {
		final HashSet<String> tagsReemplazados = Sets.newHashSet("Tag1", "Tag2", "Tag3");
		summarizer.reemplazarTagsPara(receptor1, tagsReemplazados);

		Assert.assertEquals(tagsReemplazados, listener.tagAgregadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2", "Tag3"), summarizer.getGlobalTags());

	}

	@Test
	public void noDeberiaQuitarTagsCompartidosEntreTresAunqueSaquemosDos() {
		final String tagCompartido = "Tag1";
		summarizer.agregarTagsPara(receptor1, Sets.newHashSet(tagCompartido, "Tag2"));
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag3"));
		summarizer.agregarTagsPara(receptor3, Sets.newHashSet(tagCompartido, "Tag4"));

		summarizer.limpiarTagsPara(receptor2);
		Assert.assertFalse(listener.tagQuitadosGlobalmente.contains(tagCompartido));

		summarizer.limpiarTagsPara(receptor3);
		Assert.assertFalse(listener.tagQuitadosGlobalmente.contains(tagCompartido));
		Assert.assertEquals(Sets.newHashSet("Tag1", "Tag2"), summarizer.getGlobalTags());
	}

	@Test
	public void deberiaQuitarTodosSiSeQuitaElUltimoReceptor() {
		final String tagCompartido = "Tag1";
		final HashSet<String> tagsDelReceptot1 = Sets.newHashSet(tagCompartido, "Tag2");
		summarizer.agregarTagsPara(receptor1, tagsDelReceptot1);
		summarizer.agregarTagsPara(receptor2, Sets.newHashSet(tagCompartido, "Tag3"));

		summarizer.limpiarTagsPara(receptor2);
		// Limpiamos el estado
		listener.tagQuitadosGlobalmente = null;

		summarizer.limpiarTagsPara(receptor1);
		Assert.assertEquals(tagsDelReceptot1, listener.tagQuitadosGlobalmente);
		Assert.assertEquals(Sets.newHashSet(), summarizer.getGlobalTags());
	}
}
