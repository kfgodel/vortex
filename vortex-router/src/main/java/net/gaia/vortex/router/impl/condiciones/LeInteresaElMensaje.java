/**
 * 23/01/2013 17:43:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.condiciones;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.core.api.annotations.Paralelizable;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que evalua si un mensaje es aceptado remotamente en función de
 * la condición que nos pasó
 * 
 * @author D. García
 */
@Paralelizable
public class LeInteresaElMensaje implements Condicion {
	private static final Logger LOG = LoggerFactory.getLogger(LeInteresaElMensaje.class);

	private PataBidireccional pata;

	private ParteDeCondiciones filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final Condicion condicionActual = filtroDeSalida.getCondicion();
		final ResultadoDeCondicion resultado = condicionActual.esCumplidaPor(mensaje);
		if (ResultadoDeCondicion.FALSE.equals(resultado)) {
			LOG.debug("El mensaje[{}] es descartado en[{}] porque no interesa al destino[{}] segun condicion[{}]",
					new Object[] { mensaje.toShortString(), pata.toShortString(), pata.getNodoRemoto().toShortString(),
							condicionActual });
		}
		return resultado;
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(filtroDeSalida_FIELD, filtroDeSalida).toString();
	}

	public static LeInteresaElMensaje create(final ParteDeCondiciones filtroDeSalida, final PataBidireccional pata) {
		final LeInteresaElMensaje condicion = new LeInteresaElMensaje();
		condicion.filtroDeSalida = filtroDeSalida;
		condicion.pata = pata;
		return condicion;
	}

}
