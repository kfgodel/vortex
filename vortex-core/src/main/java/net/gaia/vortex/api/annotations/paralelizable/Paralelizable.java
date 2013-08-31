/**
 * 28/01/2013 14:27:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.annotations.paralelizable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Este annotation es una marca para las operaciones de mensaje que pueden ser ejecutadas
 * concurrentemente por varios threads. Generalmente se genera una copia del mensaje que es
 * modificado para no afectar otros threads.<br>
 * <br>
 * Básicamente este annotation indica que si se ejecuta en paralelo cada rama no afecta a la otra.<br>
 * <br>
 * Este tipo de transformaciones no tiene problemas en cuanto a dónde pueden ser usadas y por lo
 * tanto son el tipo preferible de transformación. (A diferencia de las {@link NoParalelizable})
 * 
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Paralelizable {

}
