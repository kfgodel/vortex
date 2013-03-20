/**
 * 27/06/2012 14:07:30 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.ids.componentes;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.core.api.ids.componentes.GeneradorDeIdsComponentes;
import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;

/**
 * Esta clase es la implementación default del generador de ids que intenta asignar IDs distintos
 * para los componentes de la red vortex, intentando que no existan Ids duplicados en toda la red.<br>
 * Este ID es utilizado en la identificación de los mensajes que circulan, mientras más universal
 * sea el ID de los componentes generador por esta instancia, menos probabilidades de colisión en
 * los mensajes generados.
 * 
 * @author D. García
 */
public class GeneradorDeIdsGlobalesParaComponentes implements GeneradorDeIdsComponentes {

	public static final String PART_SEPARATOR = "-";
	public static final int LONGITUD_INCIAL_SECUENCIA = 1;
	public static final int LONGITUD_INICIAL_GENERADOR_TIMESTAMP = 1;
	public static final int LONGITUD_INICIAL_RANDOM_PART = 4;
	public static final int LONGITUD_INICIAL_VORTEX_TIMESTAMP = 4;

	private static final GeneradorDeIdsGlobalesParaComponentes instancia = GeneradorDeIdsGlobalesParaComponentes
			.create();

	public static GeneradorDeIdsGlobalesParaComponentes getInstancia() {
		return instancia;
	}

	/**
	 * Timestamp tomado como momento 0 en la cronología de vortex para la asignación de IDs
	 */
	public static final long MOMENTO_CERO_VORTEX = 1344121609204L;

	private String identificadorDelGenerador;
	private long momentoCeroGenerador;
	private final AtomicLong idsGenerados = new AtomicLong(0);

	public static GeneradorDeIdsGlobalesParaComponentes create() {
		final GeneradorDeIdsGlobalesParaComponentes generador = new GeneradorDeIdsGlobalesParaComponentes();
		generador.inicializar();
		return generador;
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	private void inicializar() {
		this.momentoCeroGenerador = getCurrentTimestamp();
		final StringBuilder builder = new StringBuilder();
		builder.append(getRandomString());
		builder.append(PART_SEPARATOR);
		builder.append(getVortexTimestampString());
		identificadorDelGenerador = builder.toString();
	}

	/**
	 * Devuelve el String que representa el Timestamp de la creación del generador
	 * 
	 * @return La cadena que representa el timestamp de este generador
	 */
	private String getVortexTimestampString() {
		final long currentStamp = momentoCeroGenerador - MOMENTO_CERO_VORTEX;
		final String asString = asString(currentStamp, LONGITUD_INICIAL_VORTEX_TIMESTAMP);
		return asString;
	}

	/**
	 * Devuelve una cadena generada aleatoreamente para este generador
	 * 
	 * @return La cadena que intenta ser diversa entre las instancias de generadores
	 */
	private String getRandomString() {
		final Random randomPart = new Random(hashCode());
		final int randomInt = randomPart.nextInt(0xFFFF);
		final String randomString = asString(randomInt, LONGITUD_INICIAL_RANDOM_PART);
		return randomString;
	}

	/**
	 * Devuelve el timestamp actual como una cadena hexa
	 * 
	 * @return El timestamp representado como una cadena hexa
	 */
	private String getGeneradorTimestampString() {
		final long now = getCurrentTimestamp();
		final long timestampDelGenerador = now - momentoCeroGenerador;
		final String hexaTimestamp = asString(timestampDelGenerador, LONGITUD_INICIAL_GENERADOR_TIMESTAMP);
		return hexaTimestamp;
	}

	/**
	 * Genera una representación como String del valor pasado como long
	 * 
	 * @param valor
	 *            El valor a representar
	 * @return La representación de ancho fijo, con 0 adicionales
	 */
	public static String asString(final long valor, final int padding) {
		final String hexString = String.format("%1$0" + padding + "X", valor);
		return hexString;
	}

	/**
	 * @see net.gaia.vortex.core.api.ids.componentes.GeneradorDeIdsComponentes#generarId()
	 */
	
	public IdDeComponenteVortex generarId() {
		final StringBuilder builder = new StringBuilder();
		builder.append(identificadorDelGenerador);
		builder.append(PART_SEPARATOR);
		builder.append(getGeneradorTimestampString());
		builder.append(PART_SEPARATOR);
		builder.append(getSecuenciaString());
		final String nuevoValor = builder.toString();
		final IdDeComponenteVortex identificadorCreado = IdInmutableDeComponente.create(nuevoValor);
		return identificadorCreado;
	}

	/**
	 * Devuelve el numero de secuencia de este generador como string
	 * 
	 * @return La cadena con la representación hexa del número de secuencia
	 */
	private String getSecuenciaString() {
		final long valorDeSecuencia = idsGenerados.getAndIncrement();
		final String secuencia = asString(valorDeSecuencia, LONGITUD_INCIAL_SECUENCIA);
		return secuencia;
	}

	/**
	 * Devuelve la cantidad de milisegundos que representan el momento actual
	 * 
	 * @return El timestamp como referencia de tiempo absoluta
	 */
	private static long getCurrentTimestamp() {
		return System.currentTimeMillis();
	}
}
