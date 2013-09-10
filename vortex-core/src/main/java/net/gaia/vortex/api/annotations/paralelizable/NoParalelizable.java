/**
 * 28/01/2013 14:30:53 Copyright (C) 2011 Darío L. García
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
 * Este annotation es una marca para las operaciones sobre mensajes que NO son ejecutables por
 * varios threads a la vez.<br>
 * Normalmente todas las operaciones que modifican la instancia del mensaje directamente sin hacer
 * copia son no paralelizables porque un thread obstruye a otro.<br>
 * <br>
 * A diferencia de las transformaciones "normales" estas deben usarse con cuidado porque pueden
 * tener comportamiento errático si la red no está bien armada y el orden de las transformaciones
 * puede ser alterado según el orden en que procesen los mensajes las partes.<br>
 * <br>
 * Este tipo de transformaciones tiene más sentido en el origen del mensaje cuando todavía se está
 * formando porque sirven para asignar ID o atributos especiales, pero no son aconsejables para
 * nodos en el medio de la circulación
 * 
 * @author D. García
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface NoParalelizable {

}
