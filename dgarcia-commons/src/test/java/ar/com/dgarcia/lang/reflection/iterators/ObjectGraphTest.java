/**
 * 08/12/2006 19:10:26
 * Copyright (C) 2006  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.lang.reflection.iterators;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.lang.iterators.tree.TreeIterator;
import ar.com.dgarcia.lang.iterators.tree.treeorder.BreadthFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.GraphOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder;
import ar.com.dgarcia.lang.reflection.expressions.ObjectExploder;

/**
 * Esta clase testea el funcionamiento de {@link TreeIterator}
 * pra iterar grafos de objetos
 * 
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia 
 */
public class ObjectGraphTest {
	/**
	 * Clase para probar la iteracion de objetos como relacionados, como si
	 * fueran grafos
	 * 
	 * @author D. Garcia 
	 */
	private static class ObjectoNodo{
		/**
		 * Sub nodo izquierdo
		 */
		public ObjectoNodo nodoIzquierdo;
		/**
		 * Sub nodo derecho
		 */
		public ObjectoNodo nodoDerecho;
	}
	
	
	/**
	 * Verifica la iteracion en una estructura de objetos tipo arbol
	 */
	@Test
	public void probarIteracionArbolObjetos(){
		ObjectoNodo raiz = new ObjectoNodo();
		ObjectoNodo izquierdo = new ObjectoNodo();
		ObjectoNodo derecho = new ObjectoNodo();
		
		raiz.nodoIzquierdo = izquierdo;
		raiz.nodoDerecho = derecho;
		
		TreeOrder<Object> order = GraphOrder.createFrom(BreadthFirstOrder.create());
		TreeIterator<Object> objects = TreeIterator.createFromRoot(raiz, ObjectExploder.getInstance(), order );
			
		Assert.assertTrue(objects.hasNext());
		Assert.assertSame(raiz, objects.next());
		Assert.assertSame(raiz.nodoIzquierdo, objects.next());
		Assert.assertSame(raiz.nodoDerecho, objects.next());
		Assert.assertFalse(objects.hasNext());
	}
	
	/**
	 * Verifica la iteracion en una instancia que tiene una referencia a si
	 * misma
	 */
	@Test
	public void probarIteracionAutoreferencia(){
		ObjectoNodo raiz = new ObjectoNodo();
		raiz.nodoDerecho = raiz;
		
		TreeOrder<Object> order = GraphOrder.createFrom(BreadthFirstOrder.create());
		TreeIterator<Object> objects = TreeIterator.createFromRoot(raiz, ObjectExploder.getInstance(), order );
		
		Assert.assertTrue(objects.hasNext());
		Assert.assertSame(raiz, objects.next());
		Assert.assertFalse(objects.hasNext());
	}
	
	/**
	 * Verifica la iteracion en una referencia circular
	 */
	@Test
	public void probarIteracionReferenciaCircular(){
		ObjectoNodo raiz = new ObjectoNodo();
		ObjectoNodo derecho = new ObjectoNodo();
		
		raiz.nodoDerecho = derecho;
		derecho.nodoDerecho = raiz;
		
		GraphOrder<Object> order = GraphOrder.createFrom(BreadthFirstOrder.create());
		TreeIterator<Object> objects = TreeIterator.createFromRoot(raiz, ObjectExploder.getInstance(), order );
		
		Assert.assertTrue(objects.hasNext());
		Assert.assertSame(raiz, objects.next());
		Assert.assertSame(derecho, objects.next());
		Assert.assertFalse(objects.hasNext());
		
	}
	
	/**
	 * Verifica la iteracion en una rama que repite un nodo de otra rama
	 */
	@Test
	public void probarIteracionNodoRepetido(){
		ObjectoNodo raiz = new ObjectoNodo();
		ObjectoNodo derecho = new ObjectoNodo();
		ObjectoNodo izquierdo = new ObjectoNodo();
		
		raiz.nodoIzquierdo = izquierdo;
		raiz.nodoDerecho = derecho;
		derecho.nodoIzquierdo = izquierdo;
		
		TreeOrder<Object> order = GraphOrder.createFrom(BreadthFirstOrder.create());
		TreeIterator<Object> objects = TreeIterator.createFromRoot(raiz, ObjectExploder.getInstance(), order );
			
		Assert.assertTrue(objects.hasNext());
		Assert.assertSame(raiz, objects.next());
		Assert.assertSame(raiz.nodoIzquierdo, objects.next());
		Assert.assertSame(raiz.nodoDerecho, objects.next());
		Assert.assertFalse(objects.hasNext());
	}
	
}
