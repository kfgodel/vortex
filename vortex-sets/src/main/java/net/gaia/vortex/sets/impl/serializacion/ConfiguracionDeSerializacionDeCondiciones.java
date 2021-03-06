/**
 * 21/01/2013 16:59:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion;

import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo;

/**
 * Esta interfaz define los metodos necesarios en la configuracion de serializacion para determinar
 * tipos conocidos al serializar y deserializar
 * 
 * @author D. García
 */
public interface ConfiguracionDeSerializacionDeCondiciones {

	<C> SerializadorDeTipo<C> getSerializadorDelTipo(Class<? extends C> clase);

	<C> DeserializadorDeTipo<C> getDeserializadorDelTipo(String tipoDeCondicion);

	<C> void putSerializadorDelTipo(Class<? super C> clase, SerializadorDeTipo<? extends C> serializador);

	<C> void putDeserializadorDelTipo(String tipo, DeserializadorDeTipo<? extends C> serializador);

}
