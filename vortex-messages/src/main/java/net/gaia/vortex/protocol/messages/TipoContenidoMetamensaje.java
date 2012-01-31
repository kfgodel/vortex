/**
 * 31/01/2012 13:04:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages;

import net.gaia.vortex.protocol.messages.conn.CerrarConexion;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.AcuseDuplicado;
import net.gaia.vortex.protocol.messages.routing.AcuseFallaRecepcion;
import net.gaia.vortex.protocol.messages.routing.SolicitudAcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.SolicitudEsperaAcuseConsumo;
import net.gaia.vortex.protocol.messages.tags.AgregarTags;
import net.gaia.vortex.protocol.messages.tags.LimpiarTags;
import net.gaia.vortex.protocol.messages.tags.QuitarTags;
import net.gaia.vortex.protocol.messages.tags.ReemplazarTags;

/**
 * Este enum representa los tipos de contenidos usados en los mensajes vortex para describir un
 * metamensaje
 * 
 * @author D. García
 */
public enum TipoContenidoMetamensaje {
	AGREGAR_TAGS(AgregarTags.class, "vortex.metamensaje.AgregarTags"), //
	QUITAR_TAGS(QuitarTags.class, "vortex.metamensaje.QuitarTags"), //
	REEMPLAZAR_TAGS(ReemplazarTags.class, "vortex.metamensaje.ReemplazarTags"), //
	LIMPIAR_TAGS(LimpiarTags.class, "vortex.metamensaje.LimpiarTags"), //
	CERRAR_CONEXION(CerrarConexion.class, "vortex.metamensaje.CerrarConexion"), //
	SOLICITUD_ACUSE_CONSUMO(SolicitudAcuseConsumo.class, "vortex.metamensaje.SolicitudAcuseConsumo"), //
	SOLICITUD_ESPERA_ACUSE_CONSUMO(SolicitudEsperaAcuseConsumo.class, "vortex.metamensaje.SolicitudEsperaAcuseConsumo"), //
	ACUSE_CONSUMO(AcuseConsumo.class, "vortex.metamensaje.AcuseConsumo"), //
	ACUSE_DUPLICADO(AcuseDuplicado.class, "vortex.metamensaje.AcuseDuplicado"), //
	ACUSE_FALLA(AcuseFallaRecepcion.class, "vortex.metamensaje.AcuseFallaRecepcion");

	private String tipoDeContenido;
	private Class<? extends MetamensajeVortex> clase;

	private TipoContenidoMetamensaje(final Class<? extends MetamensajeVortex> clase, final String tipoDeContenido) {
		this.tipoDeContenido = tipoDeContenido;
		this.clase = clase;
	}

	public String getTipoDeContenido() {
		return tipoDeContenido;
	}

	public Class<? extends MetamensajeVortex> getClase() {
		return clase;
	}

	/**
	 * Devuelve el string que describe el tipo de contenido para este tipo de metamensaje
	 * 
	 * @param valorComoObjeto
	 *            El metamensaje del que se quiere obtener el tipo
	 * @return El texto del metamensaje
	 */
	public static String getTipoDeContenidoFor(final MetamensajeVortex valorComoObjeto) {
		if (valorComoObjeto == null) {
			throw new IllegalArgumentException("El metamensaje no puede ser null para saber su tipo");
		}
		final Class<? extends MetamensajeVortex> claseDeMetamensaje = valorComoObjeto.getClass();
		final TipoContenidoMetamensaje[] allTipos = values();
		for (final TipoContenidoMetamensaje tipoContenidoMetamensaje : allTipos) {
			if (tipoContenidoMetamensaje.getClase().equals(claseDeMetamensaje)) {
				return tipoContenidoMetamensaje.getTipoDeContenido();
			}
		}
		throw new RuntimeException("No existe un tipo de contenido para el metamensaje: " + valorComoObjeto);
	}

	/**
	 * Devuelve la clase de metamensaje concreta que corresponde al tipo de contenido pasada
	 * 
	 * @param tipoContenidoDeclarado
	 *            El tipo de contenido
	 * @return null si el tipo indicado no se corresponde con un metamensaje registrado
	 */
	public static Class<?> getTipoFrom(final String tipoContenidoDeclarado) {
		final TipoContenidoMetamensaje[] allTipos = values();
		for (final TipoContenidoMetamensaje tipoContenidoMetamensaje : allTipos) {
			if (tipoContenidoMetamensaje.getTipoDeContenido().equals(tipoContenidoDeclarado)) {
				return tipoContenidoMetamensaje.getClase();
			}
		}
		return null;
	}

}
