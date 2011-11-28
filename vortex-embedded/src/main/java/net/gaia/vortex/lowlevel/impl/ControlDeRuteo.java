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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;

/**
 * Esta clase representa la información de control del ruteo de manera de determinar cuando está
 * terminado y qué pasó con cada mensaje
 * 
 * @author D. García
 */
public class ControlDeRuteo {

	private int receptoresNoInteresados;
	private List<ReceptorVortex> receptoresInteresados;
	private int mensajesPerdidos;
	private int mensajesRuteados;
	private Lock lockParaContinuarProcesoDeRuteo;

	public int getReceptoresNoInteresados() {
		return receptoresNoInteresados;
	}

	public void setReceptoresNoInteresados(final int receptoresNoInteresados) {
		this.receptoresNoInteresados = receptoresNoInteresados;
	}

	public List<ReceptorVortex> getReceptoresInteresados() {
		return receptoresInteresados;
	}

	public void setReceptoresInteresados(final List<ReceptorVortex> receptoresInteresados) {
		this.receptoresInteresados = receptoresInteresados;
	}

	public static ControlDeRuteo create(final SeleccionDeReceptores receptoresDelMensaje) {
		final ControlDeRuteo control = new ControlDeRuteo();
		control.receptoresInteresados = receptoresDelMensaje.getSeleccionados();
		control.receptoresNoInteresados = receptoresDelMensaje.getExcluidos();
		control.mensajesPerdidos = 0;
		control.mensajesRuteados = 0;
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
		confirmacion.setNoInteresados(this.receptoresNoInteresados);
		return confirmacion;
	}

	/**
	 * Incrementa la cantidad de mensajes perdidos
	 */
	public void registrarMensajePerdido() {
		mensajesPerdidos++;
		mensajesRuteados++;
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
		final boolean existenMasMensajesParaConfirmar = mensajesRuteados < this.receptoresInteresados.size();
		if (existenMasMensajesParaConfirmar) {
			return true;
		}
		final boolean podemosContinuarElRuteo = lockParaContinuarProcesoDeRuteo.tryLock();
		final boolean debemosIndicarQueExistenMas = !podemosContinuarElRuteo;
		return debemosIndicarQueExistenMas;
	}
}
