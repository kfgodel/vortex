/**
 * 12/06/2012 23:44:21 Copyright (C) 2011 Darío L. García
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
 * Este annotation es aplicable a los componentes de vortex que representan un segundo nivel de
 * complejidad.<br>
 * Las moléculas usan a los {@link Atomo}s internamente para definir su comportamiento. A diferencia
 * de los atomos, las moleculas pueden modificar las conexiones internas de sus atomos para
 * modificar su comportamiento en runtime alterando las conexiones entre átomos. Esto les permite
 * mayor flexibilidad, a la vez que implica mayor complejidad.<br>
 * <br>
 * A su vez, las moleculas pueden ser utilizadas por {@link Organismo}s como elementos base para
 * comportamiento más complejo.<br>
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
public @interface Molecula {

}
