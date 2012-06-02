/**
 * 31/12/2006 19:04:58
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

import java.util.List;

/**
 * Esta interfaz representa un publicador que recibe textos
 * para publicar
 * 
 * @author D. Garcia 
 */
public interface Publicador {

	/**
	 * Publica el texto recibido en algun medio
	 * @param texto Texto a publicar
	 */
	void publicar(String texto);

	/**
	 * Devuelve las publicaciones realizadas por este publicador
	 * @return El conjunto de publicaciones realizadas
	 */
	List<String> getPublicaciones();

}
