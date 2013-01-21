/**
 * 21/01/2013 16:45:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.impl;

import java.util.Iterator;
import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones;
import net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.ProblemaDeSerializacionException;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeTipo;
import ar.com.dgarcia.lang.iterators.tree.treeorder.LeavesFirstIterator;

/**
 * Esta clase implementa el serializador de condiciones
 * 
 * @author D. García
 */
public class SerializadorDeCondicionesImpl implements SerializadorDeCondiciones {

	private ConfiguracionDeSerializacionDeCondiciones config;

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones#serializar(net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public Map<String, Object> serializar(final Condicion condicionRaiz) throws ProblemaDeSerializacionException {
		// Iteramos desde las raices para asegurarnos de resolver primero las condiciones que seran
		// usadas por otras
		final Iterator<Condicion> iteradorDesdeSubCondiciones = LeavesFirstIterator.createFromRoot(condicionRaiz,
				ObtenerSubCondiciones.getInstancia());

		// El contexto nos permite asociar condiciones con sus versiones serializadas
		final ContextoDeSerializacion contexto = ContextoDeSerializacionImpl.create();
		while (iteradorDesdeSubCondiciones.hasNext()) {
			final Condicion condicionASerializar = iteradorDesdeSubCondiciones.next();
			// Obtenemos el serializador de acuerdo al tipo
			final Class<? extends Condicion> tipoDeCondicion = condicionASerializar.getClass();
			final SerializadorDeTipo<Condicion> serializador = config.getSerializadorDelTipo(tipoDeCondicion);
			// Generamos la version serializada
			final Map<String, Object> condicionSerializada = serializador.serializarDesde(condicionASerializar,
					contexto);
			// La guardamos en el contexto para que sea usable por condiciones compuestas
			contexto.guardarSerializadoDe(condicionASerializar, condicionSerializada);
		}

		// Obtenemos la version serializada de la condicion raiz
		final Map<String, Object> condicionSerializadaRaiz = contexto.obtenerSerializadoDe(condicionRaiz);
		return condicionSerializadaRaiz;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones#deserializar(java.util.Map)
	 */
	@Override
	public Condicion deserializar(final Map<String, Object> mapaRaiz) throws ProblemaDeSerializacionException {
		// Iteramos desde las raices para resolver los mapas que seran usados por sus parents
		final Iterator<Map<String, Object>> iteradorDesdeSubCondiciones = LeavesFirstIterator.createFromRoot(mapaRaiz,
				ObtenerSubMapas.getInstancia());

		// El contexto nos permite asociar condiciones con sus versiones serializadas
		final ContextoDeSerializacion contexto = ContextoDeSerializacionImpl.create();
		while (iteradorDesdeSubCondiciones.hasNext()) {
			final Map<String, Object> mapaADeserializar = iteradorDesdeSubCondiciones.next();
			// Obtenemos el deserializador de acuerdo al tipo
			final String tipoDeCondicion = (String) mapaADeserializar.get(MetadataDeSerializacion.ATRIBUTO_TIPO);
			final DeserializadorDeTipo<Condicion> deserializador = config.getDeserializadorDelTipo(tipoDeCondicion);

			// Deserializamos la condicion desde el mapa
			final Condicion condicionDeserializada = deserializador.deserializarDesde(mapaADeserializar, contexto);
			// La guardamos en el contexto para que sea usable por condiciones compuestas
			contexto.guardarDeserializadoDe(mapaADeserializar, condicionDeserializada);
		}

		// Obtenemos la version deserializada de la condicion raiz
		final Condicion condicionDeserializadaRaiz = contexto.obtenerDeserializadoDe(mapaRaiz);
		return condicionDeserializadaRaiz;
	}

	public static SerializadorDeCondicionesImpl create() {
		final SerializadorDeCondicionesImpl serializador = new SerializadorDeCondicionesImpl();
		serializador.config = ConfiguracionDeSerializacionDeCondicionesImpl.create();
		return serializador;
	}
}
