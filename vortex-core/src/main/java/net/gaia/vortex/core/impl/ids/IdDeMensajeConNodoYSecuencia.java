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
package net.gaia.vortex.core.impl.ids;

import net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el identificador de un mensaje que toma el Id del nodo emisor y un número
 * de secuencia como definitorios
 * 
 * @author D. García
 */
public class IdDeMensajeConNodoYSecuencia implements IdDeMensaje {

	private IdDeComponenteVortex idDelEmisor;
	public static final String idDelEmisor_FIELD = "idDelEmisor";

	private Long numeroDeSecuencia;
	public static final String numeroDeSecuencia_FIELD = "numeroDeSecuencia";

	public static IdDeMensajeConNodoYSecuencia create(final IdDeComponenteVortex emisor, final Long secuencia) {
		final IdDeMensajeConNodoYSecuencia identificador = new IdDeMensajeConNodoYSecuencia();
		identificador.idDelEmisor = emisor;
		identificador.numeroDeSecuencia = secuencia;
		return identificador;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final IdDeMensaje o) {
		if (!(o instanceof IdDeMensajeConNodoYSecuencia)) {
			throw new UnhandledConditionException(
					"No se como comparar un ID basado en nodo con uno que no es. Falta definir algo!");
		}
		final IdDeMensajeConNodoYSecuencia that = (IdDeMensajeConNodoYSecuencia) o;
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
		if (!(obj instanceof IdDeMensajeConNodoYSecuencia)) {
			return false;
		}
		final IdDeMensajeConNodoYSecuencia that = (IdDeMensajeConNodoYSecuencia) obj;
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
	 * @see net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje#esOriginadoEn(net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex)
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
	 * @see net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje#getIdDelEmisor()
	 */
	@Override
	public IdDeComponenteVortex getIdDelEmisor() {
		return this.idDelEmisor;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje#getNumeroDeSecuencia()
	 */
	@Override
	public Long getNumeroDeSecuencia() {
		return this.numeroDeSecuencia;
	}

}
