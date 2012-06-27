/**
 * 27/06/2012 18:31:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.ids;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.api.moleculas.ids.VortexIdentificable;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un hub que identifica los mensajes registrándose como nodo visitado, y
 * descartando los mensajes que ya lo visitaron
 * 
 * @author D. García
 */
@Atomo
public class MultiplexorIdentificador extends ComponenteConProcesadorSupport implements Multiplexor,
		VortexIdentificable {

	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";
	private Multiplexor multiplexorDeSalida;
	private NexoIdentificador identificadorDeEntrada;

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		multiplexorDeSalida.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		multiplexorDeSalida.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		identificadorDeEntrada.recibir(mensaje);
	}

	/**
	 * Inicializa el estado de esta instancia
	 */
	protected void initializeWith(final TaskProcessor processor, final IdentificadorVortex identificador) {
		super.initializeWith(processor);
		this.identificador = identificador;
		multiplexorDeSalida = MultiplexorParalelo.create(processor);
		identificadorDeEntrada = NexoIdentificador.create(processor, identificador, multiplexorDeSalida);
	}

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ids.VortexIdentificable#getIdentificador()
	 */
	@Override
	public IdentificadorVortex getIdentificador() {
		return identificador;
	}

	public static MultiplexorIdentificador create(final TaskProcessor processor, final IdentificadorVortex identificador) {
		final MultiplexorIdentificador hub = new MultiplexorIdentificador();
		hub.initializeWith(processor, identificador);
		return hub;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(identificador_FIELD, identificador).toString();
	}

}
