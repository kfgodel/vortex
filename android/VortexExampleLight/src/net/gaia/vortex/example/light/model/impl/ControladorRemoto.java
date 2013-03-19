/**
 * 19/03/2013 00:04:43 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase representa un controlador remoto de la luz a través de vortex
 * 
 * @author D. García
 */
public class ControladorRemoto {

	private Luz luzReal;

	private Portal portalVortex;

	public static ControladorRemoto create(Luz luz, Portal portal) {
		ControladorRemoto controlador = new ControladorRemoto();
		controlador.luzReal = luz;
		controlador.portalVortex = portal;
		controlador.registrarFiltros();
		return controlador;
	}

	private void registrarFiltros() {
		luzReal.addChangeListener(new LuzChangeListener() {
			@Override
			public void onLuzChanged(int nuevoValor) {
				onCambioDeLuz(nuevoValor);
			}
		});

		portalVortex.recibirCon(new HandlerTipado<ConsultarEstadoDeLuz>(ValorEsperadoEn.elAtributo(
				ConsultarEstadoDeLuz.tipoDeMensaje_FIELD, ConsultarEstadoDeLuz.TIPO)) {
			@Override
			public void onMensajeRecibido(ConsultarEstadoDeLuz consulta) {
				onConsultaRecibida(consulta);
			}
		});
		portalVortex.recibirCon(new HandlerTipado<CambiarValorDeLuz>(AndCompuesto.de(
				ValorEsperadoEn.elAtributo(CambiarValorDeLuz.tipoDeMensaje_FIELD, CambiarValorDeLuz.TIPO),
				AtributoPresente.conNombre(CambiarValorDeLuz.nuevoValor_FIELD))) {
			@Override
			public void onMensajeRecibido(CambiarValorDeLuz cambio) {
				onCambioRecibida(cambio);
			}
		});

	}

	/**
	 * Invocado al cambiar el estado de la luz
	 */
	protected void onCambioDeLuz(int nuevoValor) {
		informarValorActual(nuevoValor);
	}

	/**
	 * Invocado al recibir un pedido de cambio de estado
	 */
	protected void onCambioRecibida(CambiarValorDeLuz cambio) {
		Integer nuevoValor = cambio.getNuevoValor();
		luzReal.cambiarA(nuevoValor);
	}

	/**
	 * Invocado al recibir una consulta de estado
	 */
	protected void onConsultaRecibida(ConsultarEstadoDeLuz consulta) {
		int valorActual = luzReal.getValorActual();
		informarValorActual(valorActual);
	}

	/**
	 * @param valorActual
	 */
	private void informarValorActual(int valorActual) {
		InformarEstadoDeLuz informe = InformarEstadoDeLuz.create(valorActual);
		portalVortex.enviar(informe);
	}
}
