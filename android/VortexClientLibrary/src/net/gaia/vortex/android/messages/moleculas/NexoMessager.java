/**
 * 23/03/2013 19:04:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.messages.atomos.Desmessageficador;
import net.gaia.vortex.android.messages.atomos.Messageficador;
import net.gaia.vortex.android.messages.conversor.ConversorMessageBundle;
import net.gaia.vortex.android.messages.conversor.ConversorVortexMessage;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortex;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutable;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;
import android.os.Message;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler;

/**
 * Esta clase representa el nexo como componente vortex que se encarga de enviar y recibir mensajes
 * vortex y pasarlos a la mensajería android
 * 
 * @author D. García
 */
@Molecula
public class NexoMessager extends NodoMoleculaSupport implements RemoteSessionReceptionHandler, Nexo,
		ComponenteConMemoria {

	private RemoteSession sesionRemota;
	public static final String sesionRemota_FIELD = "sesionRemota";

	private Receptor procesoDesdeVortex;

	private Desmessageficador procesoDesdeSesion;
	private MemoriaLimitadaDeMensajes memoriaDeMensajes;
	private NexoSinDuplicados nodoDeSalidaAVortex;
	private ConversorVortexMessage converter;

	private void initializeWith(final TaskProcessor processor, final Receptor delegado, final RemoteSession sesion) {
		// Guardamos la referencia al socket
		this.sesionRemota = sesion;
		this.converter = ConversorMessageBundle.create();

		// Con esta memoria evitamos recibir mensajes que el NodoSocket nos reenvíe siendo nuestros
		memoriaDeMensajes = MemoriaLimitadaDeMensajes.create(NexoSinDuplicados.CANTIDAD_MENSAJES_RECORDADOS);

		// Al recibir un mensaje desde vortex, descartamos duplicados y lo mandamos por el socket
		procesoDesdeVortex = NexoSinDuplicados.create(processor, memoriaDeMensajes,
				Messageficador.create(processor, sesion, converter));

		// Al recibir un mensaje desde el socket, descartamos duplicados y lo mandamos a la salida
		// (a quien estemos conectados en ese momento)
		nodoDeSalidaAVortex = NexoSinDuplicados.create(processor, memoriaDeMensajes, delegado);
		procesoDesdeSesion = Desmessageficador.create(processor, nodoDeSalidaAVortex, converter);

		// Definimos cual es el flujo de entrada y salida de esta molecula
		final FlujoVortex flujoInterno = FlujoInmutable.create(procesoDesdeVortex, procesoDesdeSesion);
		initializeWith(flujoInterno);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */

	@Override
	public void setDestino(final Receptor destino) {
		nodoDeSalidaAVortex.setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#getDestino()
	 */

	@Override
	public Receptor getDestino() {
		return nodoDeSalidaAVortex.getDestino();
	}

	public static NexoMessager create(final TaskProcessor processor, final RemoteSession socket, final Receptor delegado) {
		final NexoMessager nexo = new NexoMessager();
		nexo.initializeWith(processor, delegado, socket);
		return nexo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.add(sesionRemota_FIELD, sesionRemota).add("destino", getDestino()).toString();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler#onMessageReceived(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      android.os.Message)
	 */
	@Override
	public void onMessageReceived(RemoteSession sesion, Message mensaje) {
		procesoDesdeSesion.onMessageReceived(sesion, mensaje);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#desconectar()
	 */

	public void closeAndDispose() {
		sesionRemota.close();
	}

	/**
	 * @see net.gaia.vortex.core.api.memoria.ComponenteConMemoria#yaRecibio(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	@Override
	public boolean yaRecibio(final MensajeVortex mensaje) {
		return memoriaDeMensajes.tieneRegistroDe(mensaje);
	}
}
