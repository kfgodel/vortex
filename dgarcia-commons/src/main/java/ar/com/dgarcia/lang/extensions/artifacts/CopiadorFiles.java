package ar.com.dgarcia.lang.extensions.artifacts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
/**
*
*<h1>Copiando archivos utilizando <i>java.nio</i></h1>
*<i>Sagran, 9 de Enero de 2003</i>
*<hr>
*
*
*<h2>Conceptos previos</h2>
*
*La clave del paquete java.nio es el concepto de <i>ByteBuffer</i>, el cual, como su propio
*nombre indica es un contenedor de datos de tipo  <i>byte</i>.
*Existen m�todos para manipular directamente un <i>ByteBuffer</i>, pero adem�s existen
*ciertas entidades que pueden leer y escribir en ellos. Dichas entidades implementan la interfaz
*<i>ByteChannel</i>. Una implementaci�n de <i>ByteChannel</i> representa una conexi�n
*de entrada/salida capaz de leer y escribir secuencias de bytes en un <i>ByteBuffer</i>.
*<p>
*Existen varias clases que implementan <i>ByteChannel</i>. La que nos interesa en este caso es
*<i>FileChannel</i>. Inexplicablemente en J2SE-1.4.1 no disponemos de constructores o factor�as
*que nos permitan construir un <i>FileChannel</i> a partir de un <i>File</i> (en la documentaci�n
*se comenta que dipondremos de ellos en el futuro). Para obtener un <i>FileChannel</i> necesitamos
*la existencia previa de un ejemplar de alguna de las siguientes clases de <i>java.io</i>:
*<i>FileInputStream</i>, <i>FileOutputStream</i> o <i>RandomAccessFile</i>. Las tres clases
*tienen el m�todo <i>getChannel()</i> que nos proporciona un <i>FileChannel</i> que representa una conexi�n
*con el archivo al que est� vinculado el ejemplar de la clase de <i>java.io</i>.
*<p>
*La clase <i>FileChannel</i>, adem�s de facilitar la lectura/escritura en ByteBuffer presenta
*varias caracter�sticas muy interesantes. Aqu� s�lo comentaremos la posibilidad de realizar
*tranferencias directas de bytes de un <i>FileChannel</i> a otro utilizando al m�ximo las posibilidades
*del sistema operativo. Para ello disponemos del m�todo:
*<a href="http://java.sun.com/j2se/1.4.1/docs/api/java/nio/channels/FileChannel.html#transferTo(long, long, java.nio.channels.WritableByteChannel)">
*<i>transferTo(...)</i></a> (Echadle un vistazo al �ltimo parrafo de la 'doc' del m�todo).
*<h2>El c�digo</h2>
*<pre>
*FileInputStream fis  = new FileInputStream(nombreFuente);
*FileOutputStream fos = new FileOutputStream(nombreDestino);
*FileChannel canalFuente  = fis.getChannel();
*FileChannel canalDestino = fos.getChannel();
*canalFuente.transferTo(0, canalFuente.size(), canalDestino);
*fis.close();
*fos.close();
*</pre>
*Hay que observar que el m�todo <i>close()</i> de <i>FileInputStream</i> y
*<i>FileOutputStream</i> se encarga de cerrar el <i>FileChannel</i> asociado.
*
*<h2>Un ejemplo para copiar y compilar</h2>
*
*Para facilitar la comprensi�n del c�digo no voy a controlar de manera rigurosa ni
*errores ni excepciones, y simplemente "tirar� la basura por la ventana".
* @author Sangran
* @version 1.0
*
* Modificiada por DLG
*/

public class CopiadorFiles {

	/**
	 * Copia un archivo desde el path origen al path destino.
	 * Si el path destino no existe, es creado
	 * @param pathOrigen Archivo a copiar
	 * @param pathDestino Archivo que se creara o sobreescribira
	 * como la copia del pathOrigen
	 * @throws IOException Si se produce un error en la copia
	 */
	public static void copiar(String pathOrigen, String pathDestino) throws IOException {
	    FileInputStream fis  = new FileInputStream(pathOrigen);
	    makeOutputPath(pathDestino);
	    FileOutputStream fos = new FileOutputStream(pathDestino);
	    FileChannel canalFuente  = fis.getChannel();
	    FileChannel canalDestino = fos.getChannel();
	    canalFuente.transferTo(0, canalFuente.size(), canalDestino);
	    fis.close();
	    fos.close();
	}

	/**
	 * Crea los directorios necesarios para el path del archivo
	 * indicado
	 * @param path Path a crear si no existe
	 */
	public static void makeOutputPath(String path){
		File fs = new File(path);
		File dir = new File(fs.getParent());
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
}
