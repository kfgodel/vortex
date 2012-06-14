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
package net.gaia.vortex.core3.api.annon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Este annotation es aplicable a los componentes de vortex que representan un segundo nivel de
 * complejidad.<br>
 * Las moléculas usan a los {@link Atomo}s para definir su comportamiento, pero a diferencia de
 * ellos las referencias entre moléculas son más flexibles y pueden modificarse en runtime mediante
 * conexiones lo que permite mayor flexibilidad en los cambios de comportamiento.<br>
 * <br>
 * Las instancias de esta interfaz son utilizadas por los {@link Organismo}s para definir
 * comportamientos mucho más complejo pero que es altamente variable y configurable
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Molecula {

}
