/**
 * 29/12/2006 23:32:54
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
package ar.com.dgarcia.spaceconf.tests;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.spaceconf.SpaceConfiguration;
import ar.com.dgarcia.spaceconf.tests.modelo.EscritorAutoPublicante;
import ar.com.dgarcia.spaceconf.tests.modelo.EscritorAutorizador;
import ar.com.dgarcia.spaceconf.tests.modelo.EscritorInspirable;
import ar.com.dgarcia.spaceconf.tests.modelo.Publicador;
import ar.com.dgarcia.spaceconf.tests.modelo.PublicadorResponsable;
import ar.com.dgarcia.spaceconf.tests.modelo.PublicadorSimple;

/**
 * 
 * @author D. Garcia 
 */
public class TestSpaceConfiguration {
	
	/**
	 * Prueba el escritor al estar en el mismo lugar que el publicador
	 * funcione
	 */
	@Test
	public void probarDependenciaSimple(){
		EscritorInspirable escritor = new EscritorInspirable();
		Publicador publicador = new PublicadorSimple();
		
		SpaceConfiguration.getInstance().putInside(this,escritor);
		verifyInSpace(this,escritor);
		SpaceConfiguration.getInstance().putInside(this,publicador);
		verifyInSpace(this,publicador);
		
		verifyEscritura(escritor, publicador);
	}

	/**
	 * Verifica que la instancia este contenida dentro del espacio indicado
	 * @param space Espacio contenedor
	 * @param expectedInstance Instancia contenida
	 */
	private void verifyInSpace(Object space, Object expectedInstance) {
		Object foundInstance = SpaceConfiguration.getInstance().findInside(space, expectedInstance.getClass());
		Assert.assertSame(expectedInstance,foundInstance);
	}

	/**
	 * Verifica que los escritos inspirados sean publicados
	 */
	private void verifyEscritura(EscritorInspirable escritor, Publicador publicador) {
		String[] textos = new String[]{"Un escrito", "Otro escrito"};
		for (String texto : textos) {
			escritor.inspirar(texto);
		}
		
		List<String> publicaciones = publicador.getPublicaciones();
		Assert.assertEquals(textos.length,publicaciones.size());
		for (int i = 0; i < textos.length; i++) {
			Assert.assertTrue(publicaciones.contains(textos[i]));
		}
	}
	
	/**
	 * Prueba que un objeto pueda encontrarse a si mismo en
	 * su espacio
	 */
	@Test
	public void probarAutoDependencia(){
		EscritorAutoPublicante escritor = new EscritorAutoPublicante();
		
		SpaceConfiguration.getInstance().putInside(this,escritor);
		verifyEscritura(escritor, escritor);
		
	}
	
	/**
	 * Prueba que dos objetos puedan encontrarse en el mismo espacio
	 * ambos dependiendo del otro para alguna tarea
	 */
	@Test
	public void probarDependenciaCruzada(){
		EscritorAutorizador escritor = new EscritorAutorizador();
		PublicadorResponsable publicador = new PublicadorResponsable();
		
		SpaceConfiguration.getInstance().putInside(this, escritor);
		SpaceConfiguration.getInstance().putInSameSpaceAs(escritor, publicador);
		
		verifyEscritura(escritor, publicador);
	}
}
