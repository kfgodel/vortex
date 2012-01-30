/**
 * 28/11/2011 13:25:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.envios;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.lowlevel.impl.ids.GeneradorMensajesDeNodo;
import net.gaia.vortex.lowlevel.impl.mensajes.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.nodo.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ruteo.ControlDeRuteo;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la información de contexto necesaria para las tareas relacionadas con el
 * envío de mensajes
 * 
 * @author D. García
 */
public class ContextoDeEnvio {

	private ContextoDeRuteoDeMensaje contextoDeRuteo;
	private ReceptorVortex receptorInteresado;
	private IdentificadorDeEnvio idDeEnvio;
	private EsperaDeAccion esperaDeAcuseConsumo;
	private ControlDeConsumoDeEnvio controlDeConsumo;

	public static ContextoDeEnvio create(final ContextoDeRuteoDeMensaje contextoDeRuteo, final ReceptorVortex interesado) {
		final ContextoDeEnvio contextoEnvio = new ContextoDeEnvio();
		contextoEnvio.contextoDeRuteo = contextoDeRuteo;
		contextoEnvio.receptorInteresado = interesado;
		contextoEnvio.controlDeConsumo = ControlDeConsumoDeEnvio.create();
		contextoEnvio.esperaDeAcuseConsumo = EsperaDeAccion.create();
		final MensajeVortex mensaje = contextoDeRuteo.getMensaje();
		final IdVortex idDelMensaje = mensaje.getIdentificacion();
		contextoEnvio.idDeEnvio = IdentificadorDeEnvio.create(idDelMensaje, interesado);
		return contextoEnvio;
	}

	public ContextoDeRuteoDeMensaje getContextoDeRuteo() {
		return contextoDeRuteo;
	}

	public void setContextoDeRuteo(final ContextoDeRuteoDeMensaje contextoDeRuteo) {
		this.contextoDeRuteo = contextoDeRuteo;
	}

	public ReceptorVortex getReceptorInteresado() {
		return receptorInteresado;
	}

	public void setReceptorInteresado(final ReceptorVortex receptorInteresado) {
		this.receptorInteresado = receptorInteresado;
	}

	public IdentificadorDeEnvio getIdDeEnvio() {
		return idDeEnvio;
	}

	public void setIdDeEnvio(final IdentificadorDeEnvio idDeEnvio) {
		this.idDeEnvio = idDeEnvio;
	}

	public MemoriaDeMensajes getMemoriaDeMensajes() {
		return this.contextoDeRuteo.getMemoriaDeMensajes();
	}

	public MensajeVortex getMensaje() {
		return contextoDeRuteo.getMensaje();
	}

	public TaskProcessor getProcesador() {
		return contextoDeRuteo.getProcesador();
	}

	public GeneradorMensajesDeNodo getGeneradorDeMensajes() {
		return this.contextoDeRuteo.getGeneradorMensajes();
	}

	public ConfiguracionDeNodo getConfig() {
		return this.contextoDeRuteo.getConfiguracionDeNodo();
	}

	public ControlDeRuteo getControlDeRuteo() {
		return this.contextoDeRuteo.getControl();
	}

	public EsperaDeAccion getEsperaDeAcuseConsumo() {
		return esperaDeAcuseConsumo;
	}

	public void setEsperaDeConfirmacionConsumo(final EsperaDeAccion esperaDeConfirmacionConsumo) {
		this.esperaDeAcuseConsumo = esperaDeConfirmacionConsumo;
	}

	public ControlDeConsumoDeEnvio getControlDeConsumo() {
		return controlDeConsumo;
	}

	public void setControlDeConsumo(final ControlDeConsumoDeEnvio controlDeConsumo) {
		this.controlDeConsumo = controlDeConsumo;
	}

}
