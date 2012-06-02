/*
 * Created on 04/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ar.com.dgarcia.lang.extensions;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * @author D. Garcia
 *
 * Esta clase provee un conjunto de metodos para formatear
 * Strings desde en distintos usos.
 */
public class Formateo {
	
	/**
	 * Formato utilizado en las bases de datos para guardar
	 * la fecha
	 */
	private static final SimpleDateFormat BD_TIME_STAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @return Una cadena con la fecha actual formateada
	 * @deprecated {@link #toBdTimeStamp(Date)}
	 */
	public static String getTimeStamp(){
		Calendar cal = new GregorianCalendar();
	    
	    // Get the components of the date
	    int year = cal.get(Calendar.YEAR);             // 2002
	    int month = cal.get(Calendar.MONTH);           // 0=Jan, 1=Feb, ...
	    int day = cal.get(Calendar.DAY_OF_MONTH);      // 1...
	    int hora = cal.get(Calendar.HOUR_OF_DAY);
	    int min = cal.get(Calendar.MINUTE);
	    int sec = cal.get(Calendar.SECOND);
	    
	    return year + "\\" + paddedNum(month,2) + "\\" + paddedNum(day,2) + " " + paddedNum(hora,2) + ":" + paddedNum(min,2) + ":" + paddedNum(sec,2);
	}
	
	/**
	 * Convierte un numero a String agregandole la cantidad de ceros 
	 * que sean necesarios para completar los espacios indicados a 
	 * la izquierda
	 * @param num numero a convertir
	 * @param espacios cantidad de caracteres
	 * que debe ocupar el numero
	 * @return una String con el formato
	 */
	public static String paddedNum(long num, int espacios){
		StringBuilder cadena = new StringBuilder();
		for (espacios--;espacios > 0; espacios--) {
			long mask = (long)Math.pow(10,espacios); 
			if((num / mask) == 0)
				cadena.append('0');
			else break;
		}
		cadena.append(Long.toString(num));			
		return cadena.toString();
	}

	/**
	 * Formatea una lista agregando elemento a elemento, 
	 * separandolos con la cadena separadora. Ej 1,2,3
	 * Sin incluir la cadena separadora al final 
	 * @param lista List de elementos cuyo toString() tiene 
	 * el valor que se quiere mostrar
	 * @param separador Cadena que se utiliza entre medio de 
	 * cada elemento
	 * @return Por lo menos un string vacio
	 */
	public static String separatedList(Collection<?> lista, String separador){
		StringBuilder cadena = new StringBuilder();
		for (Iterator<?> itLista = lista.iterator(); itLista.hasNext();) {
			cadena.append(itLista.next() + separador);
		}
		if(lista.size() > 0){
			cadena.delete(cadena.length() - separador.length(), cadena.length());
		}
		return cadena.toString();
	}
	
	/**
	 * Convierte la fecha pasada en un formato cadena
	 * aceptable por bases de datos (SQL)
	 * @param fecha
	 * @return La cadena expresando la fecha recibida
	 */
	public static String toBdTimeStamp(Date fecha){
		return BD_TIME_STAMP.format(fecha);
	}
	
	/**
	 * Genera una cadena con el elemento duplicado la cantidad
	 * de veces indicada, utilizando el separadaor para
	 * separar cada ocurrencia
	 * @param elemento Elemento a repetir
	 * @param separador Separador de cada ocurrencia del elemento
	 * @param cantidadVeces Cantidad de ocurrencias a repetir
	 * @return Una cadena con el elemento repetido la cantidad
	 * de veces que se indique, separando cada ocurrencia
	 * con el separador (no se incluye una al final)
	 */
	public static String repeatSeparated(String elemento, String separador, int cantidadVeces){
		StringBuilder cadena = new StringBuilder();
		int i = 0;
		for (; i < cantidadVeces; i++) {
			cadena.append(elemento + separador);
		}
		if(i>0){
			cadena.delete(cadena.length() - separador.length(),cadena.length());
		}
		return cadena.toString();
	}

	/**
	 * Crea una String que describe una instancia mostrando
	 * su estado.
	 * Primero se muestra el nombre corto de la clase y luego
	 * los pares de valores para los atributos (nombre atrib., valor) 
	 * @param objeto Objeto a mostrar en una string
	 * @return Una String con la descripcion del objeto
	 */
	public static String stateToString(Object objeto) {
		Class<?> clazz = objeto.getClass();
		String className = clazz.getSimpleName();
		StringBuilder cadena = new StringBuilder();
		cadena.append(className + "[");
		try {
			Field[] atributos = clazz.getDeclaredFields();
			for (int i = 0; i < atributos.length; i++) {
				cadena.append(atributos[i].getName()+ ": ");
				atributos[i].setAccessible(true);
				cadena.append(atributos[i].get(objeto) + ", ");
			}
			if(atributos.length > 0){
				cadena.delete(cadena.length()-2, cadena.length());
			}
			cadena.append("]");
		} catch (Exception e) {
			cadena.append("Error: " + e.getMessage());
		}
		return cadena.toString();
	}
}
