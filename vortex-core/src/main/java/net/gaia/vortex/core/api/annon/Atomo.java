/**
 * 12/06/2012 23:43:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.annon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esta annotation es aplicable a aquellos componentes básicos de Vortex que son utilizados para
 * definir comportamiento mínimo que es utilizable por componentes más complejos.<br>
 * El atomo tiene la capacidad de procesar atómicamente en su propio thread la tarea que representa.<br>
 * <br>
 * A diferencia de una {@link Molecula} los átomos no pueden cambiar su comportamiento interno, sólo
 * pueden alterar a quién están conectados. Por lo que su modificación en runtime puede pensarse
 * como "dura".<br>
 * <br>
 * Cabe aclarar que la distinción entre {@link Atomo}, {@link Molecula} y {@link Organismo} es un
 * tanto arbitrario y en una red se pueden encontrar niveles de mezclados. La diferenciación es sólo
 * teórica
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Atomo {

}