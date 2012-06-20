/**
 * 17/06/2012 20:01:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.atomos;

import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.portal.api.moleculas.ErrorDeMapeoVortexException;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que linda en el límite de la red vortex y es utilizado
 * desde un portal para introducir mensajes en la red.<br>
 * Este componente no tiene threads propios para la conversión de manera que si falla el invocante
 * reciba la excepción
 * 
 * @author D. García
 */
@Atomo
public class Vortificador implements Emisor {
	private static final Logger LOG = LoggerFactory.getLogger(Vortificador.class);

	private Receptor delegado;
	public static final String delegado_FIELD = "delegado";

	private MapeadorVortex mapeador;
	public static final String mapeador_FIELD = "mapeador";

	public void vortificar(final Object mensaje) throws ErrorDeMapeoVortexException {
		final MensajeVortex vortificado = mapeador.convertirAVortex(mensaje);
		delegado.recibir(vortificado);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino no puede ser null en el vortificador");
		}
		this.delegado = destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		LOG.info("Se intentó desconectar un destino[" + destino + "] del vortificador. Ignorando");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(mapeador_FIELD, mapeador).add(delegado_FIELD, delegado).toString();
	}

	public static Vortificador create(final MapeadorVortex mapeador, final Receptor delegado) {
		final Vortificador vortificador = new Vortificador();
		vortificador.mapeador = mapeador;
		vortificador.delegado = delegado;
		return vortificador;
	}
}
