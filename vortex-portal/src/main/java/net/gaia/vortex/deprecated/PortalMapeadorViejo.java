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
package net.gaia.vortex.deprecated;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.conversiones.ConversorDeMensajesVortex;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.moleculas.portal.HandlerDePortal;
import net.gaia.vortex.deprecated.FlujoInmutableViejo;
import net.gaia.vortex.deprecated.FlujoVortexViejo;
import net.gaia.vortex.deprecated.NexoFiltroViejo;
import net.gaia.vortex.deprecated.NexoSinDuplicadosViejo;
import net.gaia.vortex.deprecated.NexoTransformadorViejo;
import net.gaia.vortex.deprecated.NodoMoleculaSupportViejo;
import net.gaia.vortex.deprecated.NodoViejo;
import net.gaia.vortex.deprecated.SelectorConFiltrosViejo;
import net.gaia.vortex.deprecated.SelectorViejo;
import net.gaia.vortex.impl.condiciones.EsMensajeDeOtroComponente;
import net.gaia.vortex.impl.conversiones.ConversorDefaultDeMensajes;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un portal con la red vortex que utiliza un mapeador interno para convertir
 * los objetos en mensajes vortex y vice-versa
 * 
 * @author D. García
 */
@Molecula
@Deprecated
public class PortalMapeadorViejo extends NodoMoleculaSupportViejo implements PortalViejo {

	private ConversorDeMensajesVortex mapeadorVortex;

	private Receptor procesoDesdeVortex;

	private DesobjetivizadorViejo procesoDesdeUsuario;

	private SelectorViejo selectorDeMensajesEntrantes;

	private IdDeComponenteVortex identificador;
	public static final String identificador_FIELD = "identificador";

	private NexoTransformadorViejo nodoDeSalidaAVortex;

	private TaskProcessor procesador;

	/**
	 * @see net.gaia.vortex.deprecated.PortalViejo#enviar(java.lang.Object)
	 */

	public void enviar(final Object mensaje) {
		procesoDesdeUsuario.vortificar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	protected void initializeWith(final TaskProcessor processor, final Receptor delegado) {
		final GenerarIdEnMensajeViejo generador = GenerarIdEnMensajeViejo.create(GeneradorDeIdsGlobalesParaComponentes
				.getInstancia().generarId());
		initializeWith(processor, delegado, generador);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	protected void initializeWith(final TaskProcessor processor, final Receptor delegado,
			final GenerarIdEnMensajeViejo generadorDeIdsCompartido) {
		this.procesador = processor;
		identificador = generadorDeIdsCompartido.getIdDeComponente();

		nodoDeSalidaAVortex = NexoTransformadorViejo.create(processor, generadorDeIdsCompartido, delegado);
		procesoDesdeUsuario = DesobjetivizadorViejo.create(processor, mapeadorVortex, nodoDeSalidaAVortex);

		// El selector enviará el mensaje según la condición que indique el usuario
		selectorDeMensajesEntrantes = SelectorConFiltrosViejo.create(processor);
		final NexoSinDuplicadosViejo filtroDescartaDuplicados = NexoSinDuplicadosViejo.create(processor,
				selectorDeMensajesEntrantes);

		// Primero descartamos los mensajes propios y luego los duplicados externos
		final EsMensajeDeOtroComponente siEsMensajeExterno = EsMensajeDeOtroComponente.create(identificador);
		procesoDesdeVortex = NexoFiltroViejo.create(processor, siEsMensajeExterno, filtroDescartaDuplicados);

		final FlujoVortexViejo flujoInterno = FlujoInmutableViejo.create(procesoDesdeVortex, nodoDeSalidaAVortex);
		initializeWith(flujoInterno);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#setDestino(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setDestino(final Receptor destino) {
		nodoDeSalidaAVortex.setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.deprecated.NexoSupport#getDestino()
	 */

	public Receptor getDestino() {
		return nodoDeSalidaAVortex.getDestino();
	}

	/**
	 * @see net.gaia.vortex.deprecated.PortalViejo#recibirCon(net.gaia.vortex.api.moleculas.portal.HandlerDePortal)
	 */

	public void recibirCon(final HandlerDePortal<?> handlerDelPortal) {
		// Casteamos para evitar chequeos molestos
		@SuppressWarnings("unchecked")
		final HandlerDePortal<Object> handlerDeMensajesCasteado = (HandlerDePortal<Object>) handlerDelPortal;

		// Creamos el handler que convertirá al tipo esperado por el usuario, los mensajes recibidos
		final Class<Object> tipoEsperadoComoMensajes = handlerDeMensajesCasteado.getTipoEsperado();
		final ObjetivizadorViejo<Object> convertirEnTipoEsperadoEInvocarHandler = ObjetivizadorViejo.create(procesador,
				mapeadorVortex, tipoEsperadoComoMensajes, handlerDeMensajesCasteado);

		// Filtramos por la condición que nos pasa el usuario
		final Condicion condicionParaRecibirMensajeEnHandler = handlerDelPortal.getBicondicional();

		// Agregamos el caso al selector de mensajes recibidos
		selectorDeMensajesEntrantes.conectarCon(convertirEnTipoEsperadoEInvocarHandler,
				condicionParaRecibirMensajeEnHandler);
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
	public static PortalMapeadorViejo createForIOWith(final TaskProcessor processor, final NodoViejo nexoConLaRed) {
		final PortalMapeadorViejo portal = createForOutputWith(processor, nexoConLaRed);
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
	public static PortalMapeadorViejo createForOutputWith(final TaskProcessor processor, final Receptor destino) {
		final PortalMapeadorViejo portal = new PortalMapeadorViejo();
		portal.mapeadorVortex = ConversorDefaultDeMensajes.create();
		portal.initializeWith(processor, destino);
		return portal;
	}

	/**
	 * Crea una instancia con el procesador pasado y el receptro nulo como destino
	 * 
	 * @param processor
	 *            El procesador para este componente
	 * @param generadorDeIdsCompartido
	 *            El id para que este portal use
	 * @return El portal creado
	 */
	public static PortalMapeadorViejo create(final TaskProcessor processor, final GenerarIdEnMensajeViejo generadorDeIdsCompartido) {
		final PortalMapeadorViejo portal = new PortalMapeadorViejo();
		portal.mapeadorVortex = ConversorDefaultDeMensajes.create();
		portal.initializeWith(processor, ReceptorNulo.getInstancia(), generadorDeIdsCompartido);
		return portal;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(identificador_FIELD, identificador).con("destino", getDestino()).toString();
	}

	public IdDeComponenteVortex getIdentificador() {
		return identificador;
	}

}
