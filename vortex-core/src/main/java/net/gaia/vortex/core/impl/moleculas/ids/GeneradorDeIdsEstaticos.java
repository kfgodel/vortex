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
package net.gaia.vortex.core.impl.moleculas.ids;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.core.api.moleculas.ids.GeneradorDeIds;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase es la implementación default del generador de ids que asigna ids fijos a cada molecula
 * 
 * @author D. García
 */
public class GeneradorDeIdsEstaticos implements GeneradorDeIds {

	private static final WeakSingleton<GeneradorDeIdsEstaticos> ultimaReferencia = new WeakSingleton<GeneradorDeIdsEstaticos>(
			DefaultInstantiator.create(GeneradorDeIdsEstaticos.class));

	public static GeneradorDeIdsEstaticos getInstancia() {
		return ultimaReferencia.get();
	}

	private final String identificadorDeGenerador;
	private final AtomicLong idsGenerados = new AtomicLong(0);

	/**
	 * Constructor que define la primera
	 */
	public GeneradorDeIdsEstaticos() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getTimestampString());
		builder.append(getRandomString());
		identificadorDeGenerador = builder.toString();
	}

	/**
	 * Devuelve una cadena generada aleatoreamente para este generador
	 * 
	 * @return La cadena que intenta ser diversa entre las instancias de generadores
	 */
	private String getRandomString() {
		final Random randomPart = new Random(hashCode());
		final String randomString = asString(randomPart.nextLong());
		return randomString;
	}

	/**
	 * Devuelve el timestamp actual como una cadena hexa
	 * 
	 * @return El timestamp representado como una cadena hexa
	 */
	private String getTimestampString() {
		final String hexaTimestamp = asString(getCurrentTimestamp());
		return hexaTimestamp;
	}

	/**
	 * Genera una representación como String del valor pasado como long
	 * 
	 * @param valor
	 *            El valor a representar
	 * @return La representación de ancho fijo, con 0 adicionales
	 */
	public String asString(final long valor) {
		final String hexString = String.format("%1$016X", valor);
		return hexString;
	}

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ids.GeneradorDeIds#generarId()
	 */
	@Override
	public IdentificadorVortex generarId() {
		final StringBuilder builder = new StringBuilder(identificadorDeGenerador);
		builder.append(getTimestampString());
		builder.append(getSecuenciaString());
		final String nuevoValor = builder.toString();
		final IdentificadorVortex identificadorCreado = IdentificadorEstatico.create(nuevoValor);
		return identificadorCreado;
	}

	/**
	 * Devuelve el numero de secuencia de este generador como string
	 * 
	 * @return La cadena con la representación hexa del número de secuencia
	 */
	private String getSecuenciaString() {
		final String secuencia = asString(idsGenerados.getAndIncrement());
		return secuencia;
	}

	/**
	 * Devuelve la cantidad de milisegundos que representan el momento actual
	 * 
	 * @return El timestamp como referencia de tiempo absoluta
	 */
	private long getCurrentTimestamp() {
		return System.currentTimeMillis();
	}
}
