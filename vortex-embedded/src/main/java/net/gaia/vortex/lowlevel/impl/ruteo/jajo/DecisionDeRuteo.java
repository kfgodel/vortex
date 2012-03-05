/**
 * 04/03/2012 21:51:35 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.lowlevel.impl.ruteo.jajo;

/**
 * Este enum representa las posibles decisiones de ruteo del {@link OptimizadorJaJo}
 * 
 * @author D. García
 */
public enum DecisionDeRuteo {
	/**
	 * Aplicable a los nodos de los que no tenemos feedback para un tag, en cuyo caso se envía
	 */
	NO_DECIDIDO,
	/**
	 * Ya tenemos feedback positivo de que el nodo recibió bien el tag
	 */
	ENVIAR {
		@Override
		public boolean esReemplazable() {
			return false;
		}
	},
	/**
	 * Tenemos feedback negativo que el nodo recibió mal el tag
	 */
	NO_ENVIAR {
		@Override
		public boolean permiteEnvio() {
			return false;
		}
	};

	/**
	 * Indica si esta decisión permite el envío de un mensaje
	 * 
	 * @return Por defecto la indecisión permite
	 */
	public boolean permiteEnvio() {
		return true;
	}

	/**
	 * Indica si esta decisión puede ser reemplazada por otra
	 * 
	 * @return false si esta decision es enviar, true en cualquier otro caso
	 */
	public boolean esReemplazable() {
		return true;
	}

}
