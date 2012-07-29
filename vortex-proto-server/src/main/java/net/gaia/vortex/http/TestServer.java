package net.gaia.vortex.http;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;
import net.gaia.vortex.http.impl.ServidorDeNexoHttp;
import net.gaia.vortex.server.impl.RealizarConexiones;

public class TestServer {

	public static void main(final String[] args) throws Exception {
		final TaskProcessor procesador = VortexProcessorFactory.createProcessor();
		final NodoMultiplexor nodoCentral = NodoMultiplexor.create(procesador);
		final ServidorDeNexoHttp servidor = ServidorDeNexoHttp.create(procesador, 8080,
				RealizarConexiones.con(nodoCentral));
		servidor.iniciarServidorHttp();
	}
}