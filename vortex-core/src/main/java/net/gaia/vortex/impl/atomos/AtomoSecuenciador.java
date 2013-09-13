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

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
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
public class AtomoSecuenciador extends MonoEmisorSupport implements Secuenciador {
	private static final Logger LOG = LoggerFactory.getLogger(AtomoSecuenciador.class);

	private Receptor receptorMedio;
	public static final String delegado_FIELD = "receptorMedio";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		try {
			getReceptorMedio().recibir(mensaje);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en el receptorMedio. Continuando camino del mensaje", e);
		}
		getReceptorFinal().recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#getReceptorMedio()
	 */
	public Receptor getReceptorMedio() {
		return receptorMedio;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#conectarReceptorMedio(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarReceptorMedio(final Receptor receptorMedio) {
		if (receptorMedio == null) {
			throw new IllegalArgumentException("El receptorMedio no puede ser null");
		}
		this.receptorMedio = receptorMedio;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#desconectarReceptorMedio()
	 */
	public void desconectarReceptorMedio() {
		conectarReceptorMedio(ReceptorNulo.getInstancia());
	}

	/**
	 * Crea una instancia sin parametros asumiendo el receptor nulo como receptor medio
	 */
	public static AtomoSecuenciador create() {
		return create(ReceptorNulo.getInstancia());
	}

	public static AtomoSecuenciador create(final Receptor receptorMedio) {
		final AtomoSecuenciador atomo = new AtomoSecuenciador();
		atomo.conectarReceptorMedio(receptorMedio);
		return atomo;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#conectarReceptorFinal(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarReceptorFinal(final Receptor receptorFinal) {
		// Aprovecho la definición heredada
		super.conectarCon(receptorFinal);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#getReceptorFinal()
	 */
	public Receptor getReceptorFinal() {
		return super.getConectado();
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Secuenciador#desconectarReceptorFinal()
	 */
	public void desconectarReceptorFinal() {
		conectarReceptorFinal(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.impl.support.MonoEmisorSupport#desconectar()
	 */
	@Override
	public void desconectar() {
		desconectarReceptorFinal();
		desconectarReceptorMedio();
	}

	/**
	 * @see net.gaia.vortex.impl.support.MonoEmisorSupport#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		if (getReceptorMedio().equals(destino)) {
			desconectarReceptorMedio();
		}
		if (getReceptorFinal().equals(destino)) {
			desconectarReceptorFinal();
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(delegado_FIELD, receptorMedio).con(conectado_FIELD, getConectado()).toString();
	}

	/**
	 * @see net.gaia.vortex.impl.support.MonoEmisorSupport#getConectados()
	 */
	@Override
	public List<Receptor> getConectados() {
		final ArrayList<Receptor> conectados = new ArrayList<Receptor>(2);
		conectados.add(getReceptorMedio());
		conectados.add(getReceptorFinal());
		return conectados;
	}
}
