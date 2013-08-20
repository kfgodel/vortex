/**
 * 20/08/2013 00:51:08 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos.productores;

import net.gaia.vortex.api.basic.productores.MensajeFactory;
import net.gaia.vortex.api.basic.productores.Productor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.impl.support.MonoEmisorSupport;

/**
 * Esta clase representa un productor de mensajes que utiliza una {@link MensajeFactory} para
 * generar los mensajes enviados.<br>
 * A través de esta clase se pueden generar nuevos mensajes en la red, separando el que (la
 * factory), del cuando (la invocación a {@link #producirMensaje()})
 * 
 * @author D. García
 */
public class ProductorConFactory extends MonoEmisorSupport implements Productor {

	private MensajeFactory factory;

	/**
	 * Genera un nuevo mensaje con el {@link MensajeFactory} interno y lo envía a la red por el
	 * conector de salida
	 */
	public void producirMensaje() {
		final MensajeVortex mensajeCreado = factory.crearMensaje();
		getConectorUnico().recibir(mensajeCreado);
	}

	public static ProductorConFactory create(final MensajeFactory factory) {
		final ProductorConFactory prodcutor = new ProductorConFactory();
		prodcutor.inicializar();
		prodcutor.factory = factory;
		return prodcutor;
	}
}
