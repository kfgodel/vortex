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

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;

/**
 * Esta clase representa la información de control del ruteo de manera de determinar cuando está
 * terminado y qué pasó con cada mensaje
 * 
 * @author D. García
 */
@HasDependencyOn(Decision.CONFIO_EN_QUE_NO_HAY_ACUSES_REPETIDOS)
public class ControlDeRuteo {

	private IdVortex idMensajeRuteado;
	private List<ReceptorVortex> receptoresNoInteresados;
	private List<ReceptorVortex> receptoresInteresados;
	private ConcurrentLinkedQueue<ReceptorVortex> ruteosRealizados;
	private AtomicLong acumuladoCantidadFallas;
	private AtomicLong acumuladoCantidadDuplicados;
	private AtomicLong acumuladoCantidadInteresados;
	private AtomicLong acumuladoCantidadConsumidos;

	private Lock lockParaContinuarProcesoDeRuteo;

	public ConcurrentLinkedQueue<ReceptorVortex> getRuteosRealizados() {
		if (ruteosRealizados == null) {
			ruteosRealizados = new ConcurrentLinkedQueue<ReceptorVortex>();
		}
		return ruteosRealizados;
	}

	public void setRuteosRealizados(final ConcurrentLinkedQueue<ReceptorVortex> ruteosRealizados) {
		this.ruteosRealizados = ruteosRealizados;
	}

	public AtomicLong getAcumuladoCantidadConsumidos() {
		return acumuladoCantidadConsumidos;
	}

	public void setAcumuladoCantidadConsumidos(final AtomicLong acumuladoCantidadConsumidos) {
		this.acumuladoCantidadConsumidos = acumuladoCantidadConsumidos;
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
		this.acumuladoCantidadInteresados.set(receptoresInteresados.size());
	}

	public static ControlDeRuteo create(final IdVortex idMensaje) {
		final ControlDeRuteo control = new ControlDeRuteo();
		control.idMensajeRuteado = idMensaje;
		control.lockParaContinuarProcesoDeRuteo = new ReentrantLock();
		control.acumuladoCantidadConsumidos = new AtomicLong();
		control.acumuladoCantidadFallas = new AtomicLong();
		control.acumuladoCantidadDuplicados = new AtomicLong();
		control.acumuladoCantidadInteresados = new AtomicLong();
		return control;
	}

	public IdVortex getIdMensajeRuteado() {
		return idMensajeRuteado;
	}

	/**
	 * Incrementa la cantidad de mensajes perdidos
	 */
	public void registrarMensajePerdido(final IdentificadorDeEnvio idEnvio) {
		final ReceptorVortex receptorDestino = idEnvio.getReceptorDestino();
		getRuteosRealizados().add(receptorDestino);
		this.acumuladoCantidadFallas.incrementAndGet();
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
		final boolean existenMasMensajesParaConfirmar = getRuteosRealizados().size() < this.getReceptoresInteresados()
				.size();
		if (existenMasMensajesParaConfirmar) {
			return true;
		}
		final boolean podemosContinuarElRuteo = lockParaContinuarProcesoDeRuteo.tryLock();
		final boolean debemosIndicarQueExistenMas = !podemosContinuarElRuteo;
		return debemosIndicarQueExistenMas;
	}

	public void registrarConsumoRealizado(final IdentificadorDeEnvio idEnvio) {
		final ReceptorVortex receptor = idEnvio.getReceptorDestino();
		this.getRuteosRealizados().add(receptor);
		this.acumuladoCantidadConsumidos.incrementAndGet();
	}

	public void registrarMensajeDuplicados(final IdentificadorDeEnvio idEnvio) {
		final ReceptorVortex receptor = idEnvio.getReceptorDestino();
		this.getRuteosRealizados().add(receptor);
		this.acumuladoCantidadDuplicados.incrementAndGet();
	}

	/**
	 * Registra en este control que el mensaje enviado fue rechazado
	 * 
	 * @param idEnvio
	 */
	public void registrarMensajeFallado(final IdentificadorDeEnvio idEnvio) {
		final ReceptorVortex receptorDestino = idEnvio.getReceptorDestino();
		this.getRuteosRealizados().add(receptorDestino);
		this.acumuladoCantidadFallas.incrementAndGet();
	}

	/**
	 * @return
	 */
	@HasDependencyOn(Decision.LA_TAREA_DE_ENVIO_DE_ACUSE_ASIGNA_EL_ID_DEL_MENSAJE)
	public AcuseConsumo crearAcuseDeConsumo() {
		final AcuseConsumo acuse = AcuseConsumo.create();
		acuse.setCantidadInteresados(this.acumuladoCantidadInteresados.get());
		acuse.setCantidadDuplicados(this.acumuladoCantidadDuplicados.get());
		acuse.setCantidadFallados(this.acumuladoCantidadFallas.get());
		acuse.setCantidadConsumidos(this.acumuladoCantidadConsumidos.get());
		return acuse;
	}
}
