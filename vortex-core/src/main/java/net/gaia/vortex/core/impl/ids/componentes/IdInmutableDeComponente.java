/**
 * 27/06/2012 14:11:45 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implemetación default del identificador que se mantiene constante en el tiempo
 * 
 * @author D. García
 */
public class IdInmutableDeComponente implements IdDeComponenteVortex {

	private String valor;
	public static final String valor_FIELD = "valor";

	/**
	 * @see net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex#getValorActual()
	 */
	@Override
	public String getValorActual() {
		return valor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(valor_FIELD, valor).toString();
	}

	public static IdInmutableDeComponente create(final String valor) {
		final IdInmutableDeComponente identificador = new IdInmutableDeComponente();
		identificador.valor = valor;
		return identificador;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof IdDeComponenteVortex)) {
			return false;
		}
		final IdDeComponenteVortex that = (IdDeComponenteVortex) obj;
		final boolean mismoValor = this.getValorActual().equals(that.getValorActual());
		return mismoValor;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getValorActual().hashCode();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final IdDeComponenteVortex o) {
		final int orden = this.getValorActual().compareTo(o.getValorActual());
		return orden;
	}
}
