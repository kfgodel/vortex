/**
 * 31/12/2006 18:55:52
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
package ar.com.dgarcia.spaceconf.tests.modelo;

import ar.com.dgarcia.spaceconf.SpaceConfiguration;

/**
 * Esta clase representa un escritor que al ser inspirado
 * escribe un texto para ser publicado
 * 
 * @author D. Garcia 
 */
public class EscritorInspirable {

	/**
	 * Publicador obtenido del contexto necesario para la publicacion
	 */
	private Publicador publicador;
	
	/**
	 * Inspira a este escritor para que publique un texto
	 * @param idea Texto a escribir
	 */
	public void inspirar(String idea) {
		this.getPublicador().publicar(idea);
	}

	/**
	 * Devuelve el publicador, buscandolo en el contexto si aun no se
	 * definio ninguno
	 * @return El publicador
	 */
	private Publicador getPublicador() {
		if(publicador == null){
			publicador = SpaceConfiguration.getInstance().findInSameSpaceAs(this,Publicador.class);
			if(publicador == null){
				throw new RuntimeException("Este escritor necesita un publicador en el mismo espacio");
			}
		}
		return publicador;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
