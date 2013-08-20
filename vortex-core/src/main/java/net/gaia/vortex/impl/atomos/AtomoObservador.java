/**
 * 20/08/2013 01:08:30 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.atomos.Observador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.impl.support.MonoEmisorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el observador permitiendo pasar los mensajes recibidos a un receptor antes
 * de enviarlos a la salida.<br>
 * Esta clase procesa los mensajes recibidos en el mismo thread que se los entrega, no teniendo
 * thread propio
 * 
 * @author D. García
 */
@Atomo
public class AtomoObservador extends MonoEmisorSupport implements Observador {
	private static final Logger LOG = LoggerFactory.getLogger(AtomoObservador.class);

	private Receptor observador;
	public static final String observador_FIELD = "observador";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		try {
			observador.recibir(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el observador. Continuando camino del mensaje", e);
		}
		getConectorUnico().recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Observador#getObservador()
	 */
	public Receptor getObservador() {
		return observador;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Observador#setObservador(net.gaia.vortex.api.basic.Receptor)
	 */
	public void setObservador(final Receptor observador) {
		if (observador == null) {
			throw new IllegalArgumentException("El observador no puede ser null");
		}
		this.observador = observador;
	}

	public static AtomoObservador create(final Receptor observador) {
		final AtomoObservador atomo = new AtomoObservador();
		atomo.inicializar();
		atomo.setObservador(observador);
		return atomo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(observador_FIELD, observador).con(conectorUnico_FIELD, getConectorUnico())
				.toString();
	}

}
