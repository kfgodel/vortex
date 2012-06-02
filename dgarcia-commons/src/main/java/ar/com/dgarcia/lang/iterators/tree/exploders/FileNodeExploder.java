/**
 * Oct 18, 2006 - 2:17:50 PM
 */
package ar.com.dgarcia.lang.iterators.tree.exploders;

import java.io.File;
import java.util.Iterator;

import ar.com.dgarcia.lang.iterators.adapters.ArrayIterator;
import ar.com.dgarcia.lang.iterators.tree.NodeExploder;

/**
 * Esta clase permite iterar archivos como si fueran arboles. Mediante
 * este {@link NodeExploder} se puede recorrer los archivos dentro de 
 * un directorio
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 */
public class FileNodeExploder implements NodeExploder<File> {
    
    /**
     * Unica instancia
     */
    private static FileNodeExploder instance = new FileNodeExploder();
    
    /**
     * @return La unica instancia
     */
    public static FileNodeExploder getInstance() {
        return instance;
    }



    /**
     * @param currentFile Directorio a iterar
     * @return El iterador de sus archivos o null si no es un directorio
     * @see ar.com.dgarcia.lang.iterators.tree.NodeExploder#evaluateOn(java.lang.Object)
     */
    public Iterator<File> evaluateOn(File currentFile) {
		if(!currentFile.isDirectory()){
		    return null;
		}
		return ArrayIterator.create(currentFile.listFiles());
    }

}
