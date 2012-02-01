/**
 * 01/02/2012 19:07:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;

import com.google.common.collect.Sets;

/**
 * Esta clase prueba el cliente http conectado con el nodo publico de vortex
 * 
 * @author D. García
 */
public class HttpClientTest {

	private NodoVortex nodo;

	public void prepararTest() {
		nodo = NodoRemotoHttp.create("http://kfgodel.info/vortex/controllers/main");
	}

	public void deberiaPermitirEnviarUnMensajeAOtroCliente() {
		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteReceptor = ClienteVortexImpl.create(nodo, encoladorDelReceptor);
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet("java.lang.Object"));

		// Creamos el emisor que no necesita un tag
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(nodo, encoladorDelEmisor);
		final Object contenidoEnviado = new Object();
		final MensajeVortexApi mensaje = MensajeVortexApi.create(contenidoEnviado);
		clienteEmisor.enviar(mensaje);

		final MensajeVortexApi mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final Object contenidoRecibido = mensajeRecibido.getContenido();
		Assert.assertSame(contenidoEnviado, contenidoRecibido);
	}

}
