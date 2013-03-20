/**
 * 18/03/2013 23:07:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.example.light.model.impl;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.example.light.model.Luz;
import net.gaia.vortex.example.light.model.LuzChangeListener;
import net.gaia.vortex.example.light.model.messages.CambiarValorDeLuz;
import net.gaia.vortex.example.light.model.messages.ConsultarEstadoDeLuz;
import net.gaia.vortex.example.light.model.messages.InformarEstadoDeLuz;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.mensaje.HandlerTipado;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa una luz controlada remotamente a través de vortex
 * 
 * @author D. García
 */
public class LuzRemota implements Luz {
	private static final Logger LOG = LoggerFactory.getLogger(LuzRemota.class);

	private Portal portalVortex;

	private List<LuzChangeListener> listeners;

	private int estadoRemoto;

	public static LuzRemota create(Portal portal) {
		LuzRemota luz = new LuzRemota();
		luz.estadoRemoto = 0;
		luz.portalVortex = portal;
		luz.inicializarFiltros();
		return luz;
	}

	/**
	 * Registra los filtros para recibir actualizaciones de la luz
	 */
	private void inicializarFiltros() {
		portalVortex.recibirCon(new HandlerTipado<InformarEstadoDeLuz>(AndCompuesto.de(
				ValorEsperadoEn.elAtributo(InformarEstadoDeLuz.tipoDeMensaje_FIELD, InformarEstadoDeLuz.TIPO),
				AtributoPresente.conNombre(InformarEstadoDeLuz.estadoActual_FIELD))) {
			@Override
			public void onMensajeRecibido(InformarEstadoDeLuz respuestaEstado) {
				LOG.debug("Recibida informe de estado desde vortex: {}", respuestaEstado.getEstadoActual());
				onRespuestaDeEstado(respuestaEstado);
			}
		});
	}

	/**
	 * Invocado al recibir la respuesta del estado de la luz
	 * 
	 * @param respuestaEstado
	 *            La respuesta con el estado actual
	 */
	protected void onRespuestaDeEstado(InformarEstadoDeLuz respuestaEstado) {
		Integer nuevoValor = respuestaEstado.getEstadoActual();
		if (estadoRemoto == nuevoValor) {
			// No hay cmabio
			return;
		}
		estadoRemoto = nuevoValor;
		notificarListeners();
	}

	/**
	 * Notifica a todos los listeners del nuevo valor
	 */
	private void notificarListeners() {
		List<LuzChangeListener> allListeners = getListeners();
		for (LuzChangeListener listener : allListeners) {
			listener.onLuzChanged(estadoRemoto);
		}
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#cambiarA(int)
	 */
	@Override
	public void cambiarA(int nuevoValor) {
		CambiarValorDeLuz pedido = CambiarValorDeLuz.create(nuevoValor);
		LOG.debug("Enviando cambio de estado a vortex: {}", nuevoValor);
		portalVortex.enviar(pedido);
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#addChangeListener(net.gaia.vortex.example.light.model.LuzChangeListener)
	 */
	@Override
	public void addChangeListener(LuzChangeListener luzChangeListener) {
		getListeners().add(luzChangeListener);
	}

	/**
	 * @see net.gaia.vortex.example.light.model.Luz#getValorActual()
	 */
	@Override
	public int getValorActual() {
		LOG.debug("Enviando pedido de estado a vortex");
		portalVortex.enviar(ConsultarEstadoDeLuz.create());
		return estadoRemoto;
	}

	public List<LuzChangeListener> getListeners() {
		if (listeners == null) {
			listeners = new ArrayList<LuzChangeListener>(2);
		}
		return listeners;
	}

}
