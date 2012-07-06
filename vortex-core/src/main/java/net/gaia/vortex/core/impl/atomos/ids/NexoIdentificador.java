/**
 * 27/06/2012 15:15:07 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.ReceptorVariable;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.NoPasoPreviamente;
import net.gaia.vortex.core.impl.transformaciones.RegistrarPaso;
import net.gaia.vortex.core.prog.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el nexo que descarta los mensajes propios recibidos y pone una marca
 * identificatoria a los salientes.<br>
 * Este átomo más complejo sirve para las moléculas con ID que requieren evitar sus propios mensajes
 * (como un hub)
 * 
 * @author D. García
 */
@Atomo
public class NexoIdentificador extends ComponenteConProcesadorSupport implements Nexo {
	private static final Logger LOG = LoggerFactory.getLogger(NexoIdentificador.class);

	private ReceptorVariable<Receptor> destinoVariable;
	private NexoFiltro filtroDeEntrada;

	private IdentificadorVortex identificador;
	public static final String identificador_FIELD = "identificador";

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		Loggers.RUTEO.trace("Delegando a atomo[{}], mensaje[{}] desde nodo[{}]", new Object[] { filtroDeEntrada,
				mensaje, this });
		filtroDeEntrada.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		if (!getDestino().equals(destino)) {
			LOG.debug("Se intentó desconectar un nexo[{}] de un destino[{}] al que no estaba conectado. Ignorando",
					this, destino);
			return;
		}
		setDestino(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino no puede ser null en el nexo identificador");
		}
		destinoVariable.setReceptorActual(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		final Receptor destinoActual = destinoVariable.getReceptorActual();
		return destinoActual;
	}

	public static NexoIdentificador create(final TaskProcessor processor, final IdentificadorVortex identificador,
			final Receptor delegado) {
		final NexoIdentificador nexo = new NexoIdentificador();
		nexo.initializeWith(processor, identificador, delegado);
		return nexo;
	}

	/**
	 * Inicializa esta instancia con los argumentos pasados
	 * 
	 * @param processor
	 *            El procesador a utilizar en los componentes internos
	 * @param identificador
	 *            El identificador que será discriminado por este nexo
	 * @param delegado
	 */
	protected void initializeWith(final TaskProcessor processor, final IdentificadorVortex identificador,
			final Receptor delegado) {
		super.initializeWith(processor);
		this.identificador = identificador;
		destinoVariable = ReceptorVariable.create(delegado);
		final NexoTransformador registrarPasoIdentificador = NexoTransformador.create(processor,
				RegistrarPaso.por(identificador), destinoVariable);
		filtroDeEntrada = NexoFiltro
				.create(processor, NoPasoPreviamente.por(identificador), registrarPasoIdentificador);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(identificador_FIELD, identificador).toString();
	}

}
