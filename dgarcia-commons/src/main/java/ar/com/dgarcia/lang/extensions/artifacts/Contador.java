/*
 * Created on 06/04/2005
 *
 * 
 */
package ar.com.dgarcia.lang.extensions.artifacts;

import java.io.Serializable;

/**
 * @author D. Garcia
 *
 * Esta clase es un simple contador que permite tener una 
 * referencia a un objeto numerico pero a la vez modificiar 
 * su valor (sin tener que crear otro objeto).
 * Basicamente es un Long mutable
 */
public class Contador implements Comparable<Contador>, Serializable{
	/**
	 * Identificador de la clase para la VM
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Variable para el contador
	 */
	private long cantidad = 0;
	
	/**
	 * Constructor por defecto
	 */
	public Contador() {
		this(0);
	}
	
	/**
	 * Inicializa el contador
	 * @param cantidad
	 */
	public Contador(long cantidad){
		this.cantidad = cantidad;
	}
	
	/**
	 * Suma a la cantidad el numero pasado. Deber�a utilizarse 
	 * este m�todo para modificar la cantidad interna en 
	 * forma arbitraria
	 * @param n cualquier long
	 */
	public void sumar(long n){
		this.cantidad += n; 
	}
	
	/**
	 * @param arg0 
	 * @return  La comparacion
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Contador arg0) {
		Contador that = arg0;
		return (int)(this.cantidad - that.cantidad);
	}
	
	/**
	 * Incrementa la cantidad de este contador en uno
	 */
	public void incrementarUno(){
		this.cantidad++;
	}
	
	
	/**
	 * Resta de este contador la cantidad indicada
	 * @param n un entero positivo
	 */
	public void restar(int n){
		this.cantidad-= n;
	}
	
	/**
	 * Decrementa la cantidad en uno
	 */
	public void decrementarUno(){
		this.cantidad--;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		Contador that = (Contador)arg0;
		return this.cantidad == that.cantidad;
	}
	
	/**
	 * @return Returns the cantidad.
	 */
	public long getCantidad() {
		return cantidad;
	}
	
	/**
	 * Copiado del hashcode de Long
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (int)(cantidad ^ (cantidad >>> 32));
	}
	
	/**
	 * Incrementa el contador con la cantidad pasada
	 * @param n un entero positivo
	 */
	public void incrementar(int n){
		this.cantidad += n;
	}
	
	/**
	 * Establece la cantidad almacenada en este
	 * contador 
	 * @param cantidad Cantidad a la que sera inicializada
	 * esta instancia
	 */
	public void setCantidad(long cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return Long.toString(this.cantidad);
	}
}
