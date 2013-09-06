/**
 * 22/08/2013 00:53:51 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.support.MonoConectableSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el transformador como atomo básico utilizando una transformación para las
 * modificaciones.<br>
 * Si falla la transformación, el mensaje es descartado. <br>
 * <br>
 * Esta clase procesa los mensajes recibidos en el mismo thread que se los entrega, no teniendo
 * thread propio
 * 
 * @author D. García
 */
public class AtomoTransformador extends MonoConectableSupport implements Transformador {
	private static final Logger LOG = LoggerFactory.getLogger(AtomoTransformador.class);

	private Transformacion transformacion;
	public static final String transformacion_FIELD = "transformacion";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensajeOriginal) {
		if (Loggers.ATOMOS.isTraceEnabled()) {
			Loggers.ATOMOS.trace("Transformando con [{}] el mensaje[{}]", transformacion, mensajeOriginal);
		}
		MensajeVortex mensajeTransformado;
		try {
			mensajeTransformado = transformacion.transformar(mensajeOriginal);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en la transformacion[" + transformacion + "] del mensajeOriginal["
					+ mensajeOriginal + "] antes de delegarlo. Descartando mensajeOriginal", e);
			// Nada más para hacer
			getConectorParaDescartes().recibir(mensajeOriginal);
			return;
		}
		if (Loggers.ATOMOS.isDebugEnabled()) {
			Loggers.ATOMOS.debug("Transformado mensaje original[{}] en [{}]", mensajeOriginal, mensajeTransformado);
		}
		getConectorDeSalida().recibir(mensajeTransformado);
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Transformador#setTransformacion(net.gaia.vortex.api.transformaciones.Transformacion)
	 */
	public void setTransformacion(final Transformacion transformacion) {
		this.transformacion = transformacion;
	}

	/**
	 * @see net.gaia.vortex.api.atomos.Transformador#getTransformacion()
	 */
	public Transformacion getTransformacion() {
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(transformacion_FIELD, transformacion).con(conectorUnico_FIELD, getConectorDeSalida()).toString();
	}

	public static AtomoTransformador create(final Transformacion transformacion) {
		final AtomoTransformador transformador = new AtomoTransformador();
		transformador.inicializar();
		transformador.setTransformacion(transformacion);
		return transformador;
	}
}
