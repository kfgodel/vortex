/**
 * 08/07/2012 10:22:04 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.dgarcia.lang.conc;

import java.util.concurrent.Callable;

import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;

/**
 * Esta interfaz es aplicable a las entidades que permiten operar sobre un estado diferenciando las
 * operaciones de lectura y de escritura. De manera que las operaciones de lectura que no son
 * modificatorias puedan compartir el el estado y las de escritura no
 * 
 * @author D. García
 */
public interface ReadWriteOperable {

	/**
	 * Realiza la operación pasada como de sólo lectura, permitiendo a otros thread de sólo lectura
	 * realizar sus operaciones concurrentemente.<br>
	 * Las operaciones de escritura serán bloqueadas hasta que termine esta
	 * 
	 * @param readOperation
	 *            La acción a realizar sobre la estructura protegida
	 * @throws UnsuccessfulWaitException
	 *             Si no fue posible obtener el lock para realizar la operacion
	 */
	public abstract <T> T doReadOperation(Callable<T> readOperation) throws UnsuccessfulWaitException;

	/**
	 * Realiza la operación pasada como de escritura asegurando al thread el acceso exclusivo.<br>
	 * Las otras operaciones serán bloqueadas hasta que esta termine
	 * 
	 * @param writeOperation
	 *            La operación de modificación a realizar sobre la estructura protegida
	 * @throws UnsuccessfulWaitException
	 *             Si no fue posible obtener el lock para realizar la operacion
	 */
	public abstract <T> T doWriteOperation(Callable<T> writeOperation) throws UnsuccessfulWaitException;

}