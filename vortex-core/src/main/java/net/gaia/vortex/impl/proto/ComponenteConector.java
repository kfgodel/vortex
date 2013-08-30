/**
 * 19/08/2013 19:37:14 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.proto;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.support.ComponenteSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el comportamiento del componente conector que permite derivar mensajes a
 * otros receptores
 * 
 * @author D. García
 */
public class ComponenteConector extends ComponenteSupport implements Conector {
	private static final Logger LOG = LoggerFactory.getLogger(ComponenteConector.class);

	private Receptor conectado;
	public static final String conectado_FIELD = "conectado";

	public Receptor getConectado() {
		return conectado;
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El receptor destino no puede ser null. Usar el " + ReceptorNulo.class);
		}
		conectado = destino;
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#desconectar()
	 */
	public void desconectar() {
		final ReceptorNulo receptorNulo = ReceptorNulo.getInstancia();
		conectarCon(receptorNulo);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(conectado_FIELD, conectado).toString();
	}

	public static ComponenteConector create() {
		final ComponenteConector conector = new ComponenteConector();
		// Hacemos que comience desconectado
		conector.desconectar();
		return conector;
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Chequeo para evitar el toShortString() si no es necesario
		if (Loggers.ATOMOS.isDebugEnabled()) {
			Loggers.ATOMOS.debug("Delegando a nodo[{}] el mensaje[{}]", conectado.toShortString(), mensaje);
		}
		// No paso por el delegado, o no podemos saberlo, en cualquier caso lo entregamos
		try {
			conectado.recibir(mensaje);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + conectado
					+ "]. Descartando", e);
			ReceptorNulo.getInstancia().recibir(mensaje);
		}
		// Nada más que hacer
	}
}
