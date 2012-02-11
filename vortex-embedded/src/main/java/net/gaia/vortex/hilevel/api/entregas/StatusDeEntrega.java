/**
 * 10/02/2012 20:19:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.entregas;

/**
 * Este enum define los estados posibles de entrega de un mensaje vortex
 * 
 * @author D. García
 */
public enum StatusDeEntrega {
	/**
	 * Aún no existe información del destino del mensaje
	 */
	DESCONOCIDO,
	/**
	 * El mensaje no fue aceptado por el nodo por existir otro anterior con el mismo ID
	 */
	RECHAZADO_POR_DUPLICADO {
		@Override
		public boolean esRechazo() {
			return true;
		}
	},
	/**
	 * El mensaje fue rechazado al producirse errores en su procesamiento
	 */
	RECHAZADO_POR_ERROR {
		@Override
		public boolean esRechazo() {
			return true;
		}
	},
	/**
	 * El mensaje fue ruteado correctamente pero no existían nodos interesados en el mensaje
	 */
	SIN_INTERESADOS {
		@Override
		public boolean fueRuteado() {
			return true;
		}
	},
	/**
	 * Existían interesados a los que se les mandó el mensaje pero no se recibieron confirmaciones
	 * de que fuera consumido
	 */
	NO_CONSUMIDO {
		@Override
		public boolean fueRuteado() {
			return true;
		}
	},
	/**
	 * El mensaje fue ruteado y entregado a al menos 1 interesado
	 */
	CONSUMIDO {
		@Override
		public boolean correspondeAMensajeEntregado() {
			return true;
		}

		@Override
		public boolean fueRuteado() {
			return true;
		}
	};

	/**
	 * Indica si este status corresponde a un mensaje que fue entregado a algún destino
	 * 
	 * @return true si el mensaje fue consumido, false en otro caso
	 */
	public boolean correspondeAMensajeEntregado() {
		return false;
	}

	/**
	 * Indica si este estatus se considera error de parte del emisor del mensaje
	 * 
	 * @return true si fueron rechazos por falla o por duplicado
	 */
	public boolean esRechazo() {
		return false;
	}

	/**
	 * Indica si este status corresponde a un ruteo aceptado por el nodo, más allá de que el mensaje
	 * haya llegado a alguien
	 * 
	 * @return false si es error o desconocido, true si se recibió un acuse de consumo por el
	 *         mensaje
	 */
	public boolean fueRuteado() {
		return false;
	}

}
