/**
 * 13/10/2012 18:39:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.mensajes;

/**
 * Esta clase representa el mensaje que el nodo envia para confirmar por donde vino el envio
 * 
 * @author D. García
 */
public class ConfirmacionDePublicacion extends MensajeSupport {

	private PublicacionDeFiltros publicacion;

	public PublicacionDeFiltros getPublicacion() {
		return publicacion;
	}

	public void setPublicacion(final PublicacionDeFiltros publicacion) {
		this.publicacion = publicacion;
	}

	public static ConfirmacionDePublicacion create(final PublicacionDeFiltros publicacion) {
		final ConfirmacionDePublicacion name = new ConfirmacionDePublicacion();
		name.publicacion = publicacion;
		return name;
	}
}
