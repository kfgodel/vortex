package ar.com.dgarcia.lang.iterators.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.iterators.tree.NodeExploder;
import ar.com.dgarcia.lang.iterators.tree.TreeIterator;
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
			public Iterator<String> evaluateOn(String node) {
				if (node.length() > 2) {
					return null;
				}
				ArrayList<String> subNodos = new ArrayList<String>();
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
		TreeOrder<String> order = BreadthFirstOrder.create();
		TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, order);
		String[] expected = new String[] { "A", //
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
		TreeOrder<String> order = DeepFirstOrder.create();
		TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, order);
		String[] expected = new String[] { "A", //
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
		TreeOrder<String> basicOrder = DeepFirstOrder.create();
		GraphOrder<String> graphOrder = GraphOrder.createFrom(basicOrder);
		TreeIterator<String> iterador = TreeIterator.createFromRoot(raiz, nodeExplorer, graphOrder);
		String[] expected = new String[] { "A", //
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
	private void checkExpected(TreeIterator<String> iterador, String[] expected) {
		for (int i = 0; i < expected.length; i++) {
			String node = expected[i];
			Assert.assertTrue(iterador.hasNext());
			String obtained = iterador.next();
			Assert.assertEquals(node, obtained);
		}
		Assert.assertFalse(iterador.hasNext());
	}

	@Test
	public void probarRecorridoArchivos() {
		String classPathString = System.getProperty("java.class.path");
		String[] files = classPathString.split(";");
		for (String directoryPath : files) {
			File classPathFile = new File(directoryPath);
			TreeOrder<File> order = BreadthFirstOrder.create();
			TreeIterator<File> fileEntries = TreeIterator.createFromRoot(classPathFile, FileNodeExploder.getInstance(),
					order);
			while (fileEntries.hasNext()) {
				System.out.println(fileEntries.next());
			}
		}
	}
}
