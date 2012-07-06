/**
 * 17/06/2012 18:42:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.api.moleculas.ids.ReceptorIdentificable;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorVariable;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.NoPasoPreviamente;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;
import net.gaia.vortex.core.impl.tasks.DelegarMensaje;
import net.gaia.vortex.core.impl.transformaciones.RegistrarPaso;
import net.gaia.vortex.portal.api.moleculas.HandlerDePortal;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.atomos.Desvortificador;
import net.gaia.vortex.portal.impl.atomos.Vortificador;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorDefault;

/**
 * Esta clase representa un portal con la red vortex que utiliza un mapeador interno para convertir
 * los objetos en mensajes vortex y vice-versa
 * 
 * @author D. García
 */
@Molecula
public class PortalMapeador extends NexoSupport implements Portal, ReceptorIdentificable {
	private MapeadorVortex mapeadorVortex;
	private ReceptorVariable<Receptor> receptorDeSalida;
	private IdentificadorVortex identificador;
	private Receptor procesoDeEntrada;
	private Vortificador procesoDeSalida;
	private Multiplexor multiplexorDeCondiciones;

	/**
	 * @see net.gaia.vortex.core.api.moleculas.ids.VortexIdentificable#getIdentificador()
	 */
	@Override
	public IdentificadorVortex getIdentificador() {
		return identificador;
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#enviar(java.lang.Object)
	 */
	@Override
	public void enviar(final Object mensaje) {
		procesoDeSalida.vortificar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor,
	 *      net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor, final Receptor delegado) {
		identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();

		receptorDeSalida = ReceptorVariable.create(delegado);
		super.initializeWith(processor, delegado);
		final NexoTransformador registrarIdPropioEnMensaje = NexoTransformador.create(processor,
				RegistrarPaso.por(identificador), receptorDeSalida);
		procesoDeSalida = Vortificador.create(mapeadorVortex, registrarIdPropioEnMensaje);

		multiplexorDeCondiciones = MultiplexorParalelo.create(processor);
		procesoDeEntrada = NexoFiltro.create(processor, NoPasoPreviamente.por(identificador), multiplexorDeCondiciones);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		super.setDestino(destino);
		this.receptorDeSalida.setReceptorActual(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		return receptorDeSalida.getReceptorActual();
	}

	/**
	 * @see net.gaia.vortex.portal.api.moleculas.Portal#recibirCon(net.gaia.vortex.portal.api.moleculas.HandlerDePortal)
	 */
	@Override
	public void recibirCon(final HandlerDePortal<?> handlerDelPortal) {
		// Casteamos para evitar chequeos molestos
		@SuppressWarnings("unchecked")
		final HandlerDePortal<Object> handlerDeMensajesCasteado = (HandlerDePortal<Object>) handlerDelPortal;
		final Class<Object> tipoEsperadoComoMensajes = handlerDeMensajesCasteado.getTipoEsperado();
		final Desvortificador<Object> convertirEnTipoEsperadoEInvocarHandler = Desvortificador.create(getProcessor(),
				mapeadorVortex, tipoEsperadoComoMensajes, handlerDeMensajesCasteado);

		final Condicion condicionParaRecibirMensajeEnHandler = handlerDelPortal.getCondicionNecesaria();
		final NexoFiltro evaluarCondicionYHandlear = NexoFiltro.create(getProcessor(),
				condicionParaRecibirMensajeEnHandler, convertirEnTipoEsperadoEInvocarHandler);
		multiplexorDeCondiciones.conectarCon(evaluarCondicionYHandlear);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		// Al recibir un mensaje se lo pasamos al filtro de entrada
		return DelegarMensaje.create(mensaje, procesoDeEntrada);
	}

	/**
	 * Crea un portal vortex que se conectará con el nodo indicado como enlace con la red vortex.<br>
	 * Los mensajes serán enviados al nodo y recibidos del nodo
	 * 
	 * @param processor
	 *            El procesador para las tareas internas de este portal
	 * @param nexoConLaRed
	 *            El nodo con el cual este portal accede a la red vortex
	 * @return El portal creado
	 */
	public static PortalMapeador createForIOWith(final TaskProcessor processor, final Nodo nexoConLaRed) {
		final PortalMapeador portal = createForOutputWith(processor, nexoConLaRed);
		nexoConLaRed.conectarCon(portal);
		return portal;
	}

	/**
	 * Crea un nuevo portal permitiendo indicar la fuente de los mensajes recibidos en la red, y el
	 * destino de los mensajes a la red como dos componentes separados
	 * 
	 * @param processor
	 *            El procesador de las tareas internas de este portal
	 * @param destino
	 *            El componente al que se le enviarán los mensajes que van a la red
	 * @param fuente
	 *            El componente del que se recibirán los mensajes que vienen de la red
	 * @return El portal creado
	 */
	public static PortalMapeador createForOutputWith(final TaskProcessor processor, final Receptor destino) {
		final PortalMapeador portal = new PortalMapeador();
		portal.mapeadorVortex = MapeadorDefault.create();
		portal.initializeWith(processor, destino);
		return portal;
	}

}
