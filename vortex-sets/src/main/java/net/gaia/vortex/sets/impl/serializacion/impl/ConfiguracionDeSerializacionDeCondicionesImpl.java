/**
 * 21/01/2013 16:59:54 Copyright (C) 2011 Darío L. García
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

import java.util.LinkedHashMap;
import java.util.Map;

import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.CondicionDesconocida;
import net.gaia.vortex.sets.impl.Negacion;
import net.gaia.vortex.sets.impl.OrCompuesto;
import net.gaia.vortex.sets.impl.ValorEsperadoEn;
import net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones;
import net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorAnd;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDesconocido;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorEquals;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorFalse;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorNot;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorOr;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorTrue;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorAnd;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorAnonimo;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDesconocida;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorEquals;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorFalse;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorNot;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorOr;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorTrue;

/**
 * Esta clase es la implementación de la configuración de serializaciones
 * 
 * @author D. García
 */
public class ConfiguracionDeSerializacionDeCondicionesImpl implements ConfiguracionDeSerializacionDeCondiciones {

	private Map<String, DeserializadorDeTipo<?>> deserializadoresPorTipo;
	private Map<Class<?>, SerializadorDeTipo<?>> serializadoresPorTipo;

	public Map<String, DeserializadorDeTipo<?>> getDeserializadoresPorTipo() {
		if (deserializadoresPorTipo == null) {
			deserializadoresPorTipo = new LinkedHashMap<String, DeserializadorDeTipo<?>>();
		}
		return deserializadoresPorTipo;
	}

	public Map<Class<?>, SerializadorDeTipo<?>> getSerializadoresPorTipo() {
		if (serializadoresPorTipo == null) {
			serializadoresPorTipo = new LinkedHashMap<Class<?>, SerializadorDeTipo<?>>();
		}
		return serializadoresPorTipo;
	}

	public static ConfiguracionDeSerializacionDeCondicionesImpl create() {
		final ConfiguracionDeSerializacionDeCondicionesImpl config = new ConfiguracionDeSerializacionDeCondicionesImpl();
		config.inicializar();
		return config;
	}

	/**
	 * Inicializa el estado de esta configuración con los serializadores basicos
	 */
	private void inicializar() {
		// Permite reconstruir el mapa original que da lugar a la condicion desconocida
		putSerializadorDelTipo(CondicionDesconocida.class, SerializadorDesconocida.getInstancia());

		// AND
		putSerializadorDelTipo(AndCompuesto.class, SerializadorAnd.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_AND, DeserializadorAnd.getInstancia());

		// OR
		putSerializadorDelTipo(OrCompuesto.class, SerializadorOr.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_OR, DeserializadorOr.getInstancia());

		// NOT
		putSerializadorDelTipo(Negacion.class, SerializadorNot.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_NOT, DeserializadorNot.getInstancia());

		// EQUALS
		putSerializadorDelTipo(ValorEsperadoEn.class, SerializadorEquals.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_EQUALS, DeserializadorEquals.getInstancia());

		// TRUE
		putSerializadorDelTipo(SiempreTrue.class, SerializadorTrue.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_TRUE, DeserializadorTrue.getInstancia());
		// FALSE
		putSerializadorDelTipo(SiempreFalse.class, SerializadorFalse.getInstancia());
		putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_FALSE, DeserializadorFalse.getInstancia());

		// EMPIEZA_CON
		// putSerializadorDelTipo(.class, .getInstancia());
		// putDeserializadorDelTipo(MetadataDeSerializacion.TIPO_, .getInstancia());
		// ATRIBUTO_PRESENTE
		// CONTIENE
		// REGEX_MATCH
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones#getSerializadorDelTipo(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C> SerializadorDeTipo<C> getSerializadorDelTipo(final Class<? extends C> clase) {
		SerializadorDeTipo<?> serializador = getSerializadoresPorTipo().get(clase);
		if (serializador == null) {
			// Si no existe un serializador entonces se trata como anónimo
			serializador = SerializadorAnonimo.getInstancia();
		}
		return (SerializadorDeTipo<C>) serializador;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones#getDeserializadorDelTipo(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <C> DeserializadorDeTipo<C> getDeserializadorDelTipo(final String tipoDeCondicion) {
		DeserializadorDeTipo<?> deserializador = getDeserializadoresPorTipo().get(tipoDeCondicion);
		if (deserializador == null) {
			// Si no es un tipo conocido lo tratamos como condicion desconocida
			deserializador = DeserializadorDesconocido.getInstancia();
		}
		return (DeserializadorDeTipo<C>) deserializador;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones#putSerializadorDelTipo(java.lang.Class,
	 *      net.gaia.vortex.sets.impl.serializacion.SerializadorDeTipo)
	 */
	@Override
	public <C> void putSerializadorDelTipo(final Class<? super C> clase,
			final SerializadorDeTipo<? extends C> serializador) {
		getSerializadoresPorTipo().put(clase, serializador);
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ConfiguracionDeSerializacionDeCondiciones#putDeserializadorDelTipo(java.lang.String,
	 *      net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo)
	 */
	@Override
	public <C> void putDeserializadorDelTipo(final String tipo, final DeserializadorDeTipo<? extends C> serializador) {
		getDeserializadoresPorTipo().put(tipo, serializador);
	}
}
