/**
 * 23/01/2013 21:49:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.ejecutors;

import java.util.Map;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.conversiones.ConversorDeMensajesVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.messages.PublicacionDeFiltros;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el efecto de cambiar el filtro de entrada al recibir una publicación de
 * filtro
 * 
 * @author D. García
 */
public class CambiarFiltroDeSalida extends ReceptorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(CambiarFiltroDeSalida.class);

	private PataBidireccional pata;
	private ConversorDeMensajesVortex conversor;
	private SerializadorDeCondiciones serializador;
	private ParteDeCondiciones filtroDeLaPata;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public void recibir(final MensajeVortex mensaje) {
		final PublicacionDeFiltros publicacion = conversor.convertirDesdeVortex(mensaje, PublicacionDeFiltros.class);
		final Map<String, Object> nuevoFiltro = publicacion.getFiltro();
		if (nuevoFiltro == null) {
			throw new UnhandledConditionException("Recibimos un filtro nulo como publicación en el mensaje: " + mensaje);
		}
		final Condicion nuevaCondicion = serializador.deserializar(nuevoFiltro);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug(" En [{}] cambiando filtro remoto a[{}] segun publicacion recibida[{}]",
					new Object[] { pata.toShortString(), nuevaCondicion, mensaje.toShortString() });
		}
		filtroDeLaPata.cambiarA(nuevaCondicion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).toString();
	}

	public static CambiarFiltroDeSalida create(final ConversorDeMensajesVortex conversor,
			final SerializadorDeCondiciones serializador, final ParteDeCondiciones filtroDeLaPata,
			final PataBidireccional pata) {
		final CambiarFiltroDeSalida cambiar = new CambiarFiltroDeSalida();
		cambiar.conversor = conversor;
		cambiar.filtroDeLaPata = filtroDeLaPata;
		cambiar.serializador = serializador;
		cambiar.pata = pata;
		return cambiar;
	}
}
