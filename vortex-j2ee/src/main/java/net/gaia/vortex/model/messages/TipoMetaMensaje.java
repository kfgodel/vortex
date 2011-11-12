/**
 * 20/08/2011 16:42:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages;

/**
 * Este enum representa los distintos tipos de meta mensaje
 * 
 * @author D. García
 */
public enum TipoMetaMensaje {
	CERRAR_CONEXION {
		@Override
		public String getTipoContenidoAsociado() {
			return "vortex.metamensaje.CerrarConexion";
		}
	},
	AGREGAR_TAGS {
		@Override
		public String getTipoContenidoAsociado() {
			return "vortex.metamensaje.AgregarTags";
		}
	},
	QUITAR_TAGS {
		@Override
		public String getTipoContenidoAsociado() {
			return "vortex.metamensaje.QuitarTags";
		}
	};

	/**
	 * Indica el tipo de contenido asociado a este tipo de metamensaje
	 * 
	 * @return El nombre del tipo de contenido que describe el mensaje
	 */
	public String getTipoContenidoAsociado() {
		return "";
	}

	/**
	 * Devuelve el tipo de metamensaje correspondiente el tipo de contenido recibido
	 * 
	 * @param tipoContenidoRecibido
	 *            El nombre del tipo de contenido
	 * @return null si no matchea con ninguno
	 */
	public static TipoMetaMensaje obtenerDeTipoDeContenido(final String tipoContenidoRecibido) {
		final TipoMetaMensaje[] allTipos = values();
		for (final TipoMetaMensaje tipoMetaMensaje : allTipos) {
			if (tipoMetaMensaje.getTipoContenidoAsociado().equals(tipoContenidoRecibido)) {
				return tipoMetaMensaje;
			}
		}
		return null;
	};
}
