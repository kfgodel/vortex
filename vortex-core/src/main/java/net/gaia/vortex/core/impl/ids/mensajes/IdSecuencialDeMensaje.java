/**
 * 01/09/2012 12:15:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.ids.mensajes;

import java.util.Map;

import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.impl.ids.componentes.IdInmutableDeComponente;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el identificador de un mensaje que toma el Id del nodo emisor y un número
 * de secuencia como definitorios
 * 
 * @author D. García
 */
public class IdSecuencialDeMensaje implements IdDeMensaje {

	private IdDeComponenteVortex idDelEmisor;
	public static final String idDelEmisor_FIELD = "idDelEmisor";

	private Long numeroDeSecuencia;
	public static final String numeroDeSecuencia_FIELD = "numeroDeSecuencia";

	public static IdSecuencialDeMensaje create(final IdDeComponenteVortex emisor, final Long secuencia) {
		final IdSecuencialDeMensaje identificador = new IdSecuencialDeMensaje();
		identificador.idDelEmisor = emisor;
		identificador.numeroDeSecuencia = secuencia;
		return identificador;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final IdDeMensaje o) {
		if (!(o instanceof IdSecuencialDeMensaje)) {
			throw new UnhandledConditionException(
					"No se como comparar un ID basado en nodo con uno que no es. Falta definir algo!");
		}
		final IdSecuencialDeMensaje that = (IdSecuencialDeMensaje) o;
		// El orden viene dado por el ID de emisor primero y luego la secuencia
		final int comparacionDeEmisor = this.idDelEmisor.compareTo(that.idDelEmisor);
		if (comparacionDeEmisor != 0) {
			return comparacionDeEmisor;
		}
		// Es el mismo emisor
		final int comparacionDeSecuencia = this.numeroDeSecuencia.compareTo(that.numeroDeSecuencia);
		return comparacionDeSecuencia;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof IdSecuencialDeMensaje)) {
			return false;
		}
		final IdSecuencialDeMensaje that = (IdSecuencialDeMensaje) obj;
		// Comparo primero la secuencia porque requiere menos procesamiento
		if (!this.numeroDeSecuencia.equals(that.numeroDeSecuencia)) {
			// Las secuencias ya no coinciden
			return false;
		}
		final boolean mismoEmisor = esOriginadoEn(that.idDelEmisor);
		return mismoEmisor;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// Basado en el hash de arrays y colecciones
		int result = 1;
		result = 31 * result + this.idDelEmisor.hashCode();
		result = 31 * result + this.numeroDeSecuencia.hashCode();
		return result;
	}

	/**
	 * @see net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje#esOriginadoEn(net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex)
	 */
	@Override
	public boolean esOriginadoEn(final IdDeComponenteVortex idDelNodo) {
		final boolean mismoEmisor = this.idDelEmisor.equals(idDelNodo);
		return mismoEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeSecuencia_FIELD, numeroDeSecuencia).con(idDelEmisor_FIELD, idDelEmisor)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje#getIdDelEmisor()
	 */
	@Override
	public IdDeComponenteVortex getIdDelEmisor() {
		return this.idDelEmisor;
	}

	/**
	 * @see net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje#getNumeroDeSecuencia()
	 */
	@Override
	public Long getNumeroDeSecuencia() {
		return this.numeroDeSecuencia;
	}

	/**
	 * @see net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje#getAsMap()
	 */
	@Override
	public Map<String, Object> getAsMap() {
		final Map<String, Object> mapa = new CaseInsensitiveHashMap<Object>();

		final String idDelEmisor = getIdDelEmisor().getValorActual();
		mapa.put(IdDeMensaje.EMISOR_DEL_ID_KEY, idDelEmisor);

		mapa.put(IdDeMensaje.SECUENCIA_DEL_ID_KEY, getNumeroDeSecuencia());

		return mapa;
	}

	/**
	 * Regenera el id representado por el mapa pasado
	 * 
	 * @param mapa
	 *            El mapa cuyo estado representa un ID
	 * @return El Id regenerado
	 * @throws UnhandledConditionException
	 *             Si el mapa no representa un ID en la forma esperada
	 */
	public static IdDeMensaje regenerarDesde(final Map<String, Object> idComoMapa) throws UnhandledConditionException {
		final Object emisorEnMensaje = idComoMapa.get(IdDeMensaje.EMISOR_DEL_ID_KEY);
		if (emisorEnMensaje == null) {
			throw new UnhandledConditionException("El mapa no tiene emisor como ID: " + idComoMapa);
		}
		String valorDelIdDelEmisor;
		try {
			valorDelIdDelEmisor = (String) emisorEnMensaje;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("La parte del ID correspondiente al emisor no es String: "
					+ emisorEnMensaje + " mapa[" + idComoMapa + "]", e);
		}

		final Object secuenciaDeMensaje = idComoMapa.get(IdDeMensaje.SECUENCIA_DEL_ID_KEY);
		if (secuenciaDeMensaje == null) {
			throw new UnhandledConditionException("El mapa no tiene secuencia en como ID" + idComoMapa);
		}
		Number numeroDeSecuencia;
		try {
			numeroDeSecuencia = (Number) secuenciaDeMensaje;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mapa tiene una secuencia que no es Number: " + emisorEnMensaje
					+ " mensaje[" + idComoMapa + "]", e);
		}

		final IdDeComponenteVortex idDelEmisor = IdInmutableDeComponente.create(valorDelIdDelEmisor);
		final IdDeMensaje idGenerado = IdSecuencialDeMensaje.create(idDelEmisor, numeroDeSecuencia.longValue());
		return idGenerado;
	}
}
