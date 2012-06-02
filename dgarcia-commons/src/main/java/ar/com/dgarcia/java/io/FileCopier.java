/**
 * Created on 24/01/2007 01:21:24 Copyright (C) 2007 Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package ar.com.dgarcia.java.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Esta clase extiende las posibilidades de copia de archivos y de flujos de
 * bytes
 * 
 * @version 1.0
 * @since 24/01/2007
 * @author D. Garcia
 */
public class FileCopier {

	/**
	 * Tamaño del buffer de bytes utilizado para copia de archivos
	 */
	static final int COPY_BUFFER_SIZE = 100 * 1024;

	/**
	 * Copia el contenido de un flujo en otro
	 * 
	 * @param from
	 *            Flujo del que se tomaran los datos
	 * @param to
	 *            Flujo en el que se copiaran los bytes
	 * @throws IOException
	 *             Si se produce un error de acceso a los flujos
	 */
	public static void copyFrom(InputStream from, OutputStream to) throws IOException {
		byte[] buffer = new byte[COPY_BUFFER_SIZE];
		while (true) {
			int amountRead = from.read(buffer);
			if (amountRead == -1) {
				break;
			}
			to.write(buffer, 0, amountRead);
		}
	}
}
