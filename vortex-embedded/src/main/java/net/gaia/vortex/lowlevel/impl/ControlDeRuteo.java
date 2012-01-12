/**
 * 28/11/2011 12:35:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;
import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa la información de control del ruteo de manera de determinar cuando está
 * terminado y qué pasó con cada mensaje
 * 
 * @author D. García
 */
public class ControlDeRuteo {

	private IdVortex idMensajeRuteado;
	private List<ReceptorVortex> receptoresNoInteresados;
	private List<ReceptorVortex> receptoresInteresados;
	private List<IdentificadorDeEnvio> mensajesPerdidos;
	private List<IdentificadorDeEnvio> mensajesConsumidos;
	private List<IdentificadorDeEnvio> mensajesRuteados;
	private List<IdentificadorDeEnvio> mensajesRechazados;
	private List<IdentificadorDeEnvio> recepcionesConfirmadas;
	private Lock lockParaContinuarProcesoDeRuteo;

	public List<IdentificadorDeEnvio> getMensajesRechazados() {
		if (mensajesRechazados == null) {
			mensajesRechazados = new ArrayList<IdentificadorDeEnvio>();
		}
		return mensajesRechazados;
	}

	public List<ReceptorVortex> getReceptoresNoInteresados() {
		return receptoresNoInteresados;
	}

	public void setReceptoresNoInteresados(final List<ReceptorVortex> receptoresNoInteresados) {
		this.receptoresNoInteresados = receptoresNoInteresados;
	}

	public List<ReceptorVortex> getReceptoresInteresados() {
		return receptoresInteresados;
	}

	public void setReceptoresInteresados(final List<ReceptorVortex> receptoresInteresados) {
		this.receptoresInteresados = receptoresInteresados;
	}

	public static ControlDeRuteo create(final IdVortex idMensaje) {
		final ControlDeRuteo control = new ControlDeRuteo();
		control.idMensajeRuteado = idMensaje;
		control.receptoresInteresados = null;
		control.receptoresNoInteresados = null;
		control.mensajesPerdidos = null;
		control.mensajesRuteados = null;
		control.recepcionesConfirmadas = null;
		control.lockParaContinuarProcesoDeRuteo = new ReentrantLock();
		return control;
	}

	/**
	 * Crea la confirmación de consumo con la información disponible actualmente
	 * 
	 * @return La confirmación de consumo con la información para enviar
	 */
	@HasDependencyOn(Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_ASIGNA_EL_ID_DEL_MENSAJE)
	public ConfirmacionConsumo crearConfirmacionDeConsumo() {
		final ConfirmacionConsumo confirmacion = ConfirmacionConsumo.create();
		confirmacion.setInteresados(this.receptoresInteresados.size());
		confirmacion.setNoInteresados(this.receptoresNoInteresados.size());
		return confirmacion;
	}

	public IdVortex getIdMensajeRuteado() {
		return idMensajeRuteado;
	}

	public List<IdentificadorDeEnvio> getMensajesPerdidos() {
		if (mensajesPerdidos == null) {
			mensajesPerdidos = new ArrayList<IdentificadorDeEnvio>();
		}
		return mensajesPerdidos;
	}

	public List<IdentificadorDeEnvio> getMensajesRuteados() {
		if (mensajesRuteados == null) {
			mensajesRuteados = new ArrayList<IdentificadorDeEnvio>();
		}
		return mensajesRuteados;
	}

	public List<IdentificadorDeEnvio> getRecepcionesConfirmadas() {
		if (recepcionesConfirmadas == null) {
			recepcionesConfirmadas = new ArrayList<IdentificadorDeEnvio>();
		}
		return recepcionesConfirmadas;
	}

	public List<IdentificadorDeEnvio> getMensajesConsumidos() {
		if (mensajesConsumidos == null) {
			mensajesConsumidos = new ArrayList<IdentificadorDeEnvio>();
		}
		return mensajesConsumidos;
	}

	/**
	 * Incrementa la cantidad de mensajes perdidos
	 */
	public void registrarMensajePerdido(final IdentificadorDeEnvio idEnvio) {
		getMensajesPerdidos().add(idEnvio);
		getMensajesRuteados().add(idEnvio);
	}

	/**
	 * Indica si existen más mensajes para rutear. Este método sólo indicará false para un sólo
	 * thread. Al resto le indicará true, aún cuando no existan más mensajes para rutear.
	 * Garantizando que sólo un thread continue el proceso de ruteo
	 * 
	 * @return true si existen mensajes para rutear, o ya se contestó false a otro thread. False si
	 *         no existen más mensajes para rutear y este thread es el primero en preguntar
	 */
	@HasDependencyOn(Decision.PARA_CONTINUAR_EL_RUTEO_DESPUES_DEL_ENVIO_SE_UTILIZAN_CONTADORES)
	public boolean existenMensajesEnRuta() {
		// Vemos si ya se rutearon todos
		final boolean existenMasMensajesParaConfirmar = getMensajesRuteados().size() < this.getReceptoresInteresados()
				.size();
		if (existenMasMensajesParaConfirmar) {
			return true;
		}
		final boolean podemosContinuarElRuteo = lockParaContinuarProcesoDeRuteo.tryLock();
		final boolean debemosIndicarQueExistenMas = !podemosContinuarElRuteo;
		return debemosIndicarQueExistenMas;
	}

	/**
	 * Registra en este control que el mensaje fue recibido por un receptor
	 */
	public void registrarRecepcionRealizada(final IdentificadorDeEnvio idenvio) {
		this.getRecepcionesConfirmadas().add(idenvio);
	}

	/**
	 * @param idEnvio
	 */
	public void registrarConsumoRealizado(final IdentificadorDeEnvio idEnvio) {
		this.getMensajesConsumidos().add(idEnvio);
		this.getMensajesRuteados().add(idEnvio);
	}

	/**
	 * Registra en este control que el mensaje enviado fue rechazado
	 * 
	 * @param idEnvio
	 */
	public void registrarMensajeRechazado(final IdentificadorDeEnvio idEnvio) {
		this.getMensajesRechazados().add(idEnvio);
		this.getMensajesRuteados().add(idEnvio);
	}
}
