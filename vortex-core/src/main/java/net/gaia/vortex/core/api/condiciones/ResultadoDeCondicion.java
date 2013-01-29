/**
 * 20/01/2013 13:38:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.condiciones;

/**
 * Este enum representa los tres valores que una condición puede devolver al ser evaluada.<br>
 * Estos estados permiten evaluar condiciones desconocidas con resultados determinables
 * 
 * @author D. García
 */
public enum ResultadoDeCondicion {
	/**
	 * Indica que la condición es cumplida por el mensaje
	 */
	TRUE {
		@Override
		public boolean esTrue() throws IndecidibleException {
			return true;
		}
	},
	/**
	 * Indica que la condición no es cumplida por el mensaje
	 */
	FALSE {
		@Override
		public boolean esTrue() throws IndecidibleException {
			return false;
		}
	},
	/**
	 * Indica que la condición no es aplicable, no es evaluable en el mensaje, o es una condición
	 * desconocida por esta aplicación, por lo que el resultado no es decidible
	 */
	INDECIDIBLE {
		@Override
		public boolean esTrue() throws IndecidibleException {
			throw new IndecidibleException("El resultado no es booleano. Podría ser tanto true como false");
		}

		@Override
		public boolean esBooleano() {
			return false;
		}
	};

	/**
	 * Devuelve el resultado que corresponde el valor booleano pasado
	 * 
	 * @param booleano
	 *            El valor de referencia para obtener la instancia de resultado
	 * @return El resultado que representa el valor pasado
	 */
	public static ResultadoDeCondicion paraBooleano(final boolean booleano) {
		if (booleano) {
			return TRUE;
		}
		return FALSE;
	}

	/**
	 * Indica si el valor booleano que se corresponde con este resultado es true. Lanzando una
	 * excepción si el valor es indecidible
	 * 
	 * @return El valor booleano
	 * @throws IndecidibleException
	 *             Si este resultado es indecidible por true o false
	 */
	public abstract boolean esTrue() throws IndecidibleException;

	/**
	 * Indica si el valor booleano que se corresponde con este resultado es false. Lanzando una
	 * excepción si el valor es indecidible
	 * 
	 * @return El valor booleano
	 * @throws IndecidibleException
	 *             Si este resultado es indecidible por true o false
	 */
	public boolean esFalse() throws IndecidibleException {
		return !esTrue();
	}

	/**
	 * Indica si este resultado es booleano y representa un valor de verdad true, o false.<br>
	 * {@link #INDECIDIBLE} es el único valor que no es booleano
	 * 
	 * @return false si este resultado es indecidible
	 */
	public boolean esBooleano() {
		return true;
	}
}
