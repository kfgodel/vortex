/**
 * 09/05/2012 22:04:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.tests;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.vortex.bluevortex.api.ConexionVortex;
import net.gaia.vortex.bluevortex.api.ReporteDeEntrega;
import net.gaia.vortex.bluevortex.api.async.AsyncValue;
import net.gaia.vortex.bluevortex.nn.HandlerConColaDeMensajes;

import org.junit.Test;

/**
 * Esta clase prueba ejemplos de ruteos de mensajes entre dos partes directamente conectadas al
 * mismo nodo vortex
 * 
 * @author D. García
 */
public class TestRuteoSinIntermediariosDirecto extends BlueVortexTestSupport {

	/**
	 * Prueba que el caso más simple de uno a uno sea resuelto
	 */
	@Test
	public void deberiaPermitirEnviarYRecibirUnMensajeSinDeclararFiltros() {
		final HandlerConColaDeMensajes colaDeRecepcion = HandlerConColaDeMensajes.create();
		final ConexionVortex conexionReceptora = getVortex().crearConexion();
		conexionReceptora.setHandlerDeMensajes(colaDeRecepcion);

		final ConexionVortex conexionEmisora = getVortex().crearConexion();

		final Object mensajeEnviado = new Object();
		final ReporteDeEntrega reporte = conexionEmisora.enviar(mensajeEnviado);
		final AsyncValue<Long> cantidadEntregados = reporte.getCantidadDeEntregados();
		Assert.assertEquals("Debería indicar que el mensaje fue entregado a un receptor", 1, cantidadEntregados
				.waitForValueUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES)).intValue());

		final Object mensajeRecibido = colaDeRecepcion.getCola().poll();
		Assert.assertNotNull("Debería existir un mensaje recibido", mensajeRecibido);
		Assert.assertSame("El mensaje recibido debería ser la misma instancia enviada", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * Prueba que tanto el emisor como el receptor pueden estar ocupados mientras se realiza el
	 * ruteo, y eso no impide la entrega del mensaje
	 */
	@Test
	public void elRuteoDeMensajesDeberiaSerAsincrono() {

	}

	/**
	 * Verifica que al emisor no le llegue el mensaje que mandó.<br>
	 * 
	 */
	@Test
	public void elEmisorNoDeberiaRecibirSuPropioMensaje() {
		// TODO Existe la posibilidad que el emisor quiera recibir sus propios mensajes?
	}

	/**
	 * Prueba que una fuente pueda llegar a dos destinos en el mismo mensaje
	 */
	@Test
	public void elMismoMensajeDeberiaLlegarAInteresadosDistintos() {

	}

	/**
	 * Prueba que si existe una pequeña diferencia en los filtros, los mensajes sean discriminados
	 * correctamente
	 */
	@Test
	public void mensajesSimilaresDeberianLlegarALosDestinosCorrectos() {

	}

	/**
	 * Prueba que un receptor pueda empiece a recibir mensajes que antes no, si adapta su filtro
	 */
	@Test
	public void seDeberiaRecibirElMensajeSiSeModificaElFiltroReceptorAceptandolo() {

	}

	/**
	 * Prueba que al modificar el filtro rechazando un tipo de mensajes, ya no lleguen
	 */
	@Test
	public void noDeberiaRecibirElMensajeSiSeModificaElFiltroRechazandolo() {

	}

}
