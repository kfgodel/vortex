package ar.com.dgarcia.lang.iterators.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.iterators.tree.exploders.FileNodeExploder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.BreadthFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.DeepFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.GraphOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder;

/**
 * Esta clase testea los iteradores de arboles
 * 
 * @author dgarcia
 */
public class TreeIteratorTest {

	/**
	 * Nodo raiz de todas las iteraciones
	 */
	private String raiz;

	/**
	 * Expansor de los subnodos de un nodo
	 */
	private NodeExploder<String> nodeExplorer;

	@Before
	public void setUp() {
		raiz = "A";
		nodeExplorer = new NodeExploder<String>() {
			
			public Iterator<String> evaluateOn(final String node) {
				if (node.length() > 2) {
					return null;
				}
				final ArrayList<String> subNodos = new ArrayList<String>();
				subNodos.add(node + "A");
				subNodos.add(node + "B");
				subNodos.add(node + "C");
				subNodos.add(node + "C");
				return subNodos.iterator();
			}
		};
	}

	@Test
	public void probarIteradorALoAncho() {
		final TreeOrder<String> order = BreadthFirstOrder.create();
		final TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, order);
		final String[] expected = new String[] { "A", //
				"AA", //
				"AB", //
				"AC", //
				"AC", //
				"AAA", //
				"AAB", //
				"AAC", //
				"AAC", //
				"ABA", //
				"ABB", //
				"ABC", //
				"ABC", //
				"ACA", //
				"ACB", //
				"ACC", //
				"ACC", //
				"ACA", //
				"ACB", //
				"ACC", //
				"ACC", //
		};
		checkExpected(iterador, expected);
	}

	@Test
	public void probarIteradorEnProfundo() {
		final TreeOrder<String> order = DeepFirstOrder.create();
		final TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, order);
		final String[] expected = new String[] { "A", //
				"AA", //
				"AAA", //
				"AAB", //
				"AAC", //
				"AAC", //
				"AB", //
				"ABA", //
				"ABB", //
				"ABC", //
				"ABC", //
				"AC", //
				"ACA", //
				"ACB", //
				"ACC", //
				"ACC", //
				"AC", //
				"ACA", //
				"ACB", //
				"ACC", //
				"ACC", //
		};
		checkExpected(iterador, expected);
	}

	@Test
	public void probarIteradorEnProfundoGrafo() {
		final TreeOrder<String> basicOrder = DeepFirstOrder.create();
		final GraphOrder<String> graphOrder = GraphOrder.createFrom(basicOrder);
		final TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, graphOrder);
		final String[] expected = new String[] { "A", //
				"AA", //
				"AAA", //
				"AAB", //
				"AAC", //
				"AB", //
				"ABA", //
				"ABB", //
				"ABC", //
				"AC", //
				"ACA", //
				"ACB", //
				"ACC", //
		};
		checkExpected(iterador, expected);
	}

	/**
	 * Comprueba que los elementos obtenidos coincidan con los esperados
	 * 
	 * @param iterador
	 *            Iterador de los obtenidos
	 * @param expected
	 *            Valores esperados
	 */
	private void checkExpected(final Iterator<String> iterador, final String[] expected) {
		for (int i = 0; i < expected.length; i++) {
			final String node = expected[i];
			Assert.assertTrue(iterador.hasNext());
			final String obtained = iterador.next();
			Assert.assertEquals(node, obtained);
		}
		Assert.assertFalse(iterador.hasNext());
	}

	@Test
	public void probarRecorridoArchivos() {
		final String classPathString = System.getProperty("java.class.path");
		final String[] files = classPathString.split(";");
		for (final String directoryPath : files) {
			final File classPathFile = new File(directoryPath);
			final TreeOrder<File> order = BreadthFirstOrder.create();
			final TreeIterator<File> fileEntries = TreeIterator.createFromRoot(classPathFile,
					FileNodeExploder.getInstance(), order);
			while (fileEntries.hasNext()) {
				System.out.println(fileEntries.next());
			}
		}
	}

	@Test
	public void probarIteradorDeHojasPrimero() {
		final Iterator<String> iterador = LeavesFirstIterator.createFromRoot(raiz, nodeExplorer);
		final String[] expected = new String[] { "ACC", //
				"ACC", //
				"ACB", //
				"ACA", //
				"ACC", //
				"ACC", //
				"ACB", //
				"ACA", //
				"ABC", //
				"ABC", //
				"ABB", //
				"ABA", //
				"AAC", //
				"AAC", //
				"AAB", //
				"AAA", //
				"AC", //
				"AC", //
				"AB", //
				"AA", //
				"A", //
		};
		checkExpected(iterador, expected);
	}

}
