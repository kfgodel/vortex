/**
 * 21/08/2011 17:31:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.conectores.http;

import java.util.List;

import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.externals.json_old.InterpreteJson;
import net.gaia.vortex.model.messages.MensajeRecibido;
import net.gaia.vortex.model.messages.protocolo.MensajeVortex;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;
import net.gaia.vortex.services.ServicioConexionHttp;
import net.gaia.vortex.services.ServicioRuteo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Esta clase representa un conector HTTP de Vortex que sabe como manejar los pedidos http y
 * derivarlos al ruteador interno. Esta clase es la primera capa de contacto de vortex con el mundo
 * externo usando HTTP
 * 
 * @author D. García
 */
@Component
public class ConectorHttp {

	@Autowired
	private ServicioConexionHttp servicioConexionHttp;

	@Autowired
	private ServicioRuteo servicioRuteo;

	@Autowired
	private InterpreteJson interpreteJson;

	/**
	 * @param pedido
	 */
	public void procesar(final OperacionHttp pedido) {
		final ComandoHttp comando = servicioConexionHttp.interpretarComoComando(pedido);
		if (comando == null) {
			// No hacemos nada. Simplemente retornamos por ahora
			return;
		}
		if (comando.solicitaEnvioDeMensajes()) {
			final List<MensajeVortex> mensajesParaEnviar = comando.getMensajesParaEnviar();
			for (final MensajeVortex mensajeParaEnvio : mensajesParaEnviar) {
				final MensajeRecibido mensajeRecibido = MensajeRecibido.create(mensajeParaEnvio,
						comando.getReferenciaReceptor(), interpreteJson);
				servicioRuteo.rutear(mensajeRecibido);
			}
		}
		if (comando.solicitaRecepcionDeMensajes()) {
			final ReferenciaAReceptor localizador = comando.getReferenciaReceptor();
			servicioConexionHttp.enviarPendientesA(localizador, pedido);
		}
	}

}
