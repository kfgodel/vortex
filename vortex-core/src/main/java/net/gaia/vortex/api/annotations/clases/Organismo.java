/**
 * 12/06/2012 23:46:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.annotations.clases;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Este annotation es aplicable a los componentes vortex que representan en sí mismos una red vortex
 * conectable con otras redes.<br>
 * Este es el mayor nivel de complejidad vortex utilizable en un programa y normalmente estará
 * compuesta de varias {@link Molecula}s y {@link Atomo}s, aunque podría también incluir otros
 * {@link Organismo}s como parte de sí.<br>
 * <br>
 * Cabe aclarar que la distinción entre {@link Atomo}, {@link Molecula} y {@link Organismo} es un
 * tanto arbitrario y en una red se pueden encontrar niveles de mezclados. La diferenciación es sólo
 * teórica.<br>
 * Estos annotations se usan para guiar al lector del código respecto de cada componente
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Organismo {

}
