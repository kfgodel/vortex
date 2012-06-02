/**
 * Created on 14/01/2007 16:47:11 Copyright (C) 2007 Dario L. Garcia
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
import java.util.Iterator;

import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.lang.iterators.tree.TreeIterator;
import ar.com.dgarcia.lang.iterators.tree.exploders.FileNodeExploder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.DeepFirstOrder;
import ar.com.dgarcia.lang.iterators.tree.treeorder.TreeOrder;

/**
 * Esta clase permite recorrer archivos de un directorio de diferentes maneras
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia
 */
public class FileIterator {

	/**
	 * Condicion que filtra los directorios
	 */
	private static final JustFilesCondition justFilesCondition = new JustFilesCondition();

	/**
	 * Esta clase representa la condicion que permite filtrar los fileEntries
	 * que corresponden a directorios. Mediante esta condicion se pueden iterar
	 * solo los archivos de un directorio
	 * 
	 * @version 1.0
	 * @since 14/01/2007
	 * @author D. Garcia
	 */
	public static final class JustFilesCondition implements Condition<File> {
		/**
		 * Filtra los files que corresponden a directorios
		 * 
		 * @param fileEntry
		 *            File a evaluar
		 * @return false si la entrada es un directorio
		 * @see ar.com.dgarcia.lang.closures.Condition#isMetBy(java.lang.Object)
		 */
		public boolean isMetBy(File fileEntry) {
			return !fileEntry.isDirectory();
		}
	}

	/**
	 * Condicion que filtra los directorios
	 */
	private static final JustDirsCondition justDirsCondition = new JustDirsCondition();

	/**
	 * Esta clase representa la condicion que permite filtrar los fileEntries
	 * que corresponden a directorios. Mediante esta condicion se pueden iterar
	 * solo los directorios dentro de un directorio
	 * 
	 * @version 1.0
	 * @since 14/01/2007
	 * @author D. Garcia
	 */
	public static final class JustDirsCondition implements Condition<File> {
		/**
		 * Filtra los files que no corresponden a directorios
		 * 
		 * @param fileEntry
		 *            File a evaluar
		 * @return true si la entrada es un directorio
		 * @see ar.com.dgarcia.lang.closures.Condition#isMetBy(java.lang.Object)
		 */
		public boolean isMetBy(File fileEntry) {
			return fileEntry.isDirectory();
		}
	}

	/**
	 * Crea un iterador de archivos a partir de un directorio. Este iterador
	 * permite recorrer todos los archivos incluidos en el directorio
	 * (incluyendo subdirectorios) solo devolviendo los archivos contenidos.
	 * (Los directorios son salteados)
	 * 
	 * @param file
	 *            FileEntry al directorio a recorrer (puede ser un archivo en
	 *            cuyo caso se recorrera ese solo)
	 * @return EL iterador de los archivos
	 */
	public static Iterator<File> everyFileDeepInside(File file) {
		Iterator<File> fileEntries = everyEntryDeepInside(file);
		ConditionalIterator<File> archivos = ConditionalIterator.createFrom(justFilesCondition, fileEntries);
		return archivos;
	}

	/**
	 * Crea un iterador de directorios a partir de un directorio. Este iterador
	 * permite recorrer todos los subdirectorios incluidos en el directorio.
	 * 
	 * @param file
	 *            FileEntry al directorio a recorrer (puede ser un archivo en
	 *            cuyo caso se recorrera ese solo)
	 * @return EL iterador de los archivos
	 */
	public static Iterator<File> everyDirDeepInside(File file) {
		Iterator<File> fileEntries = everyEntryDeepInside(file);
		ConditionalIterator<File> archivos = ConditionalIterator.createFrom(justDirsCondition, fileEntries);
		return archivos;
	}

	/**
	 * Devuelve todas las entradas del directorio pasadp, devolviendo un file
	 * por cada una. Se incluyen archivos y directorios.<br>
	 * Si se pasa un archivo, se devolverá ese sólo.
	 * 
	 * @param file
	 *            Directorio o archivo a iterar
	 * @return Un iterador que recorrera todos los archivos del directorio
	 *         pasado entrando en subdirectorios.
	 */
	public static Iterator<File> everyEntryDeepInside(File file) {
		TreeOrder<File> searchOrder = DeepFirstOrder.create();
		TreeIterator<File> fileEntries = TreeIterator.createFromRoot(file, FileNodeExploder.getInstance(), searchOrder);
		return fileEntries;
	}

}
