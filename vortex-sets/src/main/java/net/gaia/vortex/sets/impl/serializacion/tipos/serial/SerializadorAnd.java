/**
 * 21/01/2013 18:33:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos.serial;

import java.util.List;
import java.util.Map;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.impl.helpers.VortexMap;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa el serializador para condiciones por AND
 * 
 * @author D. García
 */
public class SerializadorAnd extends WeakSingletonSupport implements SerializadorDeTipo<AndCompuesto> {

	private static final WeakSingleton<SerializadorAnd> ultimaReferencia = new WeakSingleton<SerializadorAnd>(
			DefaultInstantiator.create(SerializadorAnd.class));

	public static SerializadorAnd getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo#serializarDesde(java.lang.Object,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public Map<String, Object> serializarDesde(final AndCompuesto origen, final ContextoDeSerializacion contexto) {
		final Map<String, Object> serializado = new VortexMap();
		serializado.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_AND);

		final List<Condicion> subCondiciones = origen.getSubCondiciones();
		final List<Map<String, Object>> subSerializados = contexto.obtenerSerializadosDe(subCondiciones);
		serializado.put(MetadataDeSerializacion.TIPO_AND_FILTROS, subSerializados);

		return serializado;
	}

}
