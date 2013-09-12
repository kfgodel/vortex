/**
 * 04/09/2013 22:37:14 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.moleculas;

import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.builder.VortexPortal;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.api.moleculas.portal.ErrorDeMapeoVortexException;
import net.gaia.vortex.api.moleculas.portal.HandlerDePortal;
import net.gaia.vortex.impl.atomos.Desobjetivizador;
import net.gaia.vortex.impl.atomos.Objetivizador;
import net.gaia.vortex.impl.support.EmisorSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa la forma más básica de portal que sólo convierte los objetos en mensaje, y
 * los mensajes en objetos.<br>
 * A diferencia de otros portales, este no discrimina mensajes propios o repetidos por lo que la
 * topología de la red no puede tener bucles
 * 
 * @author D. García
 */
public class PortalConversor extends EmisorSupport implements Portal {

	private Selector desdeVortex;
	public static final String desdeVortex_FIELD = "desdeVortex";

	private Desobjetivizador haciaVortex;

	private VortexPortal builder;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		desdeVortex.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	public void enviar(final Object mensaje) throws ErrorDeMapeoVortexException {
		haciaVortex.vortificar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MonoConectable#getConectorDeSalida()
	 */
	public Conector getConectorDeSalida() {
		return haciaVortex.getConectorDeSalida();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Portal#recibirCon(net.gaia.vortex.api.moleculas.portal.HandlerDePortal)
	 */
	public <T> void recibirCon(final HandlerDePortal<T> handlerDelPortal) {
		// Agregamos el caso al selector de mensajes recibidos
		final Condicion bicondicionalDelHandler = handlerDelPortal.getBicondicional();
		final Conector conectorCondicionado = desdeVortex.crearConectorCon(bicondicionalDelHandler);

		// Si pasa la condicion se lo entregamos al conversor a objetos
		final Class<T> tipoEsperadoComoMensajes = handlerDelPortal.getTipoEsperado();
		final Objetivizador conversorAObjeto = builder
				.conversorHaciaObjetos(tipoEsperadoComoMensajes, handlerDelPortal);
		conectorCondicionado.conectarCon(conversorAObjeto);
	}

	public static PortalConversor create(final VortexPortal builder) {
		final PortalConversor portal = new PortalConversor();
		portal.inicializar(builder);
		return portal;
	}

	/**
	 * Inicializa el estado de esta instancia con los componentes internos necesarios
	 * 
	 * @param builder
	 *            El builder desde el cual crear las instancias necesarias como dependencias
	 */
	private void inicializar(final VortexPortal builder) {
		// Guardamos el builder que necesitamos para agregar componentes al selector
		this.builder = builder;

		// Los mensajes entrantes los mandamos al selector que sera configurado con condiciones y
		// handlers
		this.desdeVortex = builder.getCore().selector();

		// Los objetos entrantes los convertimos en mensajes, y los mandamos al conector de salida
		this.haciaVortex = builder.conversorDesdeObjetos();
	}

	/**
	 * @see net.gaia.vortex.impl.support.ComponenteSupport#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(desdeVortex_FIELD, desdeVortex).toString();
	}

}
