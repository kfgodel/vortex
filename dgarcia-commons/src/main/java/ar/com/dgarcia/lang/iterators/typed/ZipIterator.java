/**
 * 16/09/2006 13:07:45 Copyright (C) 2006 Dario L. Garcia
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
package ar.com.dgarcia.lang.iterators.typed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ar.com.dgarcia.java.io.FileCopier;
import ar.com.dgarcia.lang.iterators.PreSizedIterator;
import ar.com.dgarcia.lang.iterators.ResetableIterator;
import ar.com.dgarcia.lang.iterators.adapters.EnumerationIteratorAdapter;
import ar.com.dgarcia.usercomm.msgs.BadException;
import ar.com.dgarcia.usercomm.msgs.ErroneousExecution;
import ar.com.dgarcia.usercomm.msgs.ErrorType;

/**
 * Esta clase permite iterar las entradas de zips, de un archivo zip de manera
 * de descomprimir el archivo u obtener informacion acerca de cada archivo
 * comprimido en ese zip.
 * 
 * @author D. Garcia
 */
public class ZipIterator extends EnumerationIteratorAdapter<ZipEntry> implements PreSizedIterator<ZipEntry>,
		ResetableIterator<ZipEntry> {

	/**
	 * Archivo Zip que sera iterado por esta instancia
	 */
	private ZipFile iteratedFile;

	/**
	 * Ultima entrada del archivo devuelto (utilizado para descomprimir)
	 */
	private ZipEntry lastEntry;

	/**
	 * @see ar.com.dgarcia.lang.iterators.adapters.EnumerationIteratorAdapter#next()
	 */
	
	public ZipEntry next() {
		lastEntry = super.next();
		return lastEntry;
	}

	/**
	 * Crea un iterador de las entradas a partir de un path a un archivo zip
	 * 
	 * @param zipFile
	 *            Archivo zip a iterar
	 * @return El nuevo iterador de entradas
	 * @throws SecurityException
	 *             Si existe un permiso que impide acceder al archivo
	 */
	public static ZipIterator createFrom(ZipFile zipFile) {
		ZipIterator iterator = new ZipIterator();
		iterator.iteratedFile = zipFile;
		iterator.reset();
		return iterator;
	}

	/**
	 * Devuelve la cantidad de entradas del archivo zip
	 * 
	 * @see ar.com.dgarcia.lang.iterators.PreSizedIterator#size()
	 */
	public int size() throws UnsupportedOperationException {
		return this.iteratedFile.size();
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.ResetableIterator#reset()
	 */
	public void reset() {
		this.initialize(this.iteratedFile.entries());
	}

	/**
	 * @see ar.com.dgarcia.lang.iterators.adapters.EnumerationIteratorAdapter#initialize(java.util.Enumeration)
	 */
	
	protected void initialize(Enumeration<? extends ZipEntry> baseEnumeration) {
		super.initialize(baseEnumeration);
		this.lastEntry = null;
	}

	/**
	 * Descomprime el contenido del ultimo {@link ZipEntry} en el path definido
	 * por el File pasado. El path debe ser valido y referenciar a un archivo,
	 * que exista o pueda ser creado.
	 * 
	 * @param destinationFile
	 *            Path en el que se decomprimira el contenido del archivo. Si el
	 *            archivo existe, su contenido sera reemplazado
	 * @throws FileNotFoundException
	 *             Si el archivo indicado no puede ser creado o accedido
	 * @throws IOException
	 *             Si se produce un error tanto en el zip, como en el archivo de
	 *             destino
	 */
	public void descomprimirComo(File destinationFile) throws FileNotFoundException, IOException {
		if (!destinationFile.isFile()) {
			ErroneousExecution.hasHappened("El file debe corresponder a un archivo", ErrorType.CONTRACT_VIOLATION);
		}
		if (lastEntry == null) {
			ErroneousExecution.hasHappened("No se puede descomprimir, ya que no se llamo a next()",
					ErrorType.CONTRACT_VIOLATION);
		}

		InputStream zippedStream = this.iteratedFile.getInputStream(lastEntry);
		if (zippedStream == null) {
			throw new FileNotFoundException("No se pudo acceder al flujo del archivo!?: " + lastEntry.getName());
		}
		FileOutputStream unzippedStream = new FileOutputStream(destinationFile);
		FileCopier.copyFrom(zippedStream, unzippedStream);
		unzippedStream.close();
		zippedStream.close();
	}

	/**
	 * Decomprime el contenido del ultimo {@link ZipEntry} devuelto en un
	 * archivo temporal que es devuelto
	 * 
	 * @return El File que representa el archivo temporal creado para contener
	 *         el archivo descomprimido
	 * @throws IOException
	 *             Si se produjo un error al acceder al contenido del zip o al
	 *             escribir los datos en el temporal
	 */
	public File descomprimirEnTemporal() throws IOException {
		File tmp;
		try {
			tmp = File.createTempFile("unzippedTemporal", null);
		} catch (IOException e) {
			BadException.hasHappened("No se pudo crear un temporal para descomprimir?!", e,
					ErrorType.UNHANDLED_SITUATION);
			throw null;
		}
		descomprimirComo(tmp);
		return tmp;
	}
}
