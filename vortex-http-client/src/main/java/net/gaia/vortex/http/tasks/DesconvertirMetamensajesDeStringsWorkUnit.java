/**
 * 02/02/2012 23:14:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.tasks;

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.MetamensajeVortex;
import net.gaia.vortex.protocol.messages.TipoContenidoMetamensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para desconvertir los metamensajes
 * recibidos y pasarlos a su version Objeto desde json
 * 
 * @author D. García
 */
public class DesconvertirMetamensajesDeStringsWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DesconvertirMetamensajesDeStringsWorkUnit.class);

	private ContextoDeOperacionHttp contexto;
	private List<MensajeVortex> mensajesRecibidos;
	private InterpreteJson interprete;

	public static DesconvertirMetamensajesDeStringsWorkUnit create(final ContextoDeOperacionHttp contexto,
			final List<MensajeVortex> mensajesRecibidos) {
		final DesconvertirMetamensajesDeStringsWorkUnit name = new DesconvertirMetamensajesDeStringsWorkUnit();
		name.contexto = contexto;
		name.mensajesRecibidos = mensajesRecibidos;
		return name;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Definimos el intérprete para la desconversión
		interprete = contexto.getInterpreteJson();

		// Desconvertimos cada metamensaje
		for (final MensajeVortex mensajeRecibido : mensajesRecibidos) {
			if (!mensajeRecibido.esMetaMensaje()) {
				continue;
			}
			desconvertirValorDeJson(mensajeRecibido);
		}

		// Se los entregamos al receptor
		final EntregarMensajesASesionWorkUnit entrega = EntregarMensajesASesionWorkUnit.create(contexto,
				mensajesRecibidos);
		contexto.getProcessor().process(entrega);
	}

	/**
	 * Toma el valor del metamensaje recibido y lo convierte a su versión Objeto
	 * 
	 * @param mensajeRecibido
	 *            El mensaje recibido
	 */
	private void desconvertirValorDeJson(final MensajeVortex mensajeRecibido) {
		final ContenidoVortex contenidoVortex = mensajeRecibido.getContenido();
		final String tipoContenido = contenidoVortex.getTipoContenido();
		final Class<? extends MetamensajeVortex> tipoEsperado = TipoContenidoMetamensaje.getTipoFrom(tipoContenido);
		if (tipoEsperado == null) {
			LOG.error("No se reconoce el tipo de metamensaje recibido, desconversion de json no realizada:"
					+ mensajeRecibido.toPrettyPrint());
			return;
		}
		String metamensajeComoJson;
		try {
			metamensajeComoJson = (String) contenidoVortex.getValor();
		} catch (final ClassCastException e) {
			LOG.error("El metamensaje recibido no es un String, desconversion de json no realizada:"
					+ mensajeRecibido.toPrettyPrint());
			return;
		}
		final MetamensajeVortex metamensaje = interprete.fromJson(metamensajeComoJson, tipoEsperado);
		contenidoVortex.setValor(metamensaje);
	}
}
