/**
 * 13/03/2012 22:49:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.pc_volume;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.android.pcvolume.messages.PcVolumenChange;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.impl.InterpreteJackson;
import net.gaia.vortex.hilevel.api.ClienteVortex;
import net.gaia.vortex.hilevel.api.HandlerDeMensajesApi;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.hilevel.api.impl.ConfiguracionClienteVortex;
import net.gaia.vortex.http.api.NodoRemotoHttp;
import net.gaia.vortex.http.api.config.ConfiguracionSinEncriptacion;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.pc_volume.changer.JavaSoundChanger;
import net.gaia.vortex.pc_volume.changer.PcVolumeChanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

/**
 * Esta clase representa el programa que corre como instancia autónoma para cambiar el volumen de la
 * PC
 * 
 * @author D. García
 */
public class PCVolumeMain implements HandlerDeMensajesApi {
	private static final Logger LOG = LoggerFactory.getLogger(PCVolumeMain.class);

	public static PCVolumeMain I;

	private TaskProcessor processor;
	private ClienteVortex clienteVortex;
	private InterpreteJson interpreteJson;
	private PcVolumeChanger volumeChanger;
	private String computerId;

	public static void main(final String[] args) {
		final int argsLength = args.length;
		if (argsLength < 1) {
			System.out
					.println("Se debe indicar como parametro extra el nombre de la máquina que coincide con el nombre usado en android");
			return;
		}
		I = PCVolumeMain.create(args[0]);
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(final TaskProcessor processor) {
		this.processor = processor;
	}

	public ClienteVortex getClienteVortex() {
		return clienteVortex;
	}

	public void setClienteVortex(final ClienteVortex clienteVortex) {
		this.clienteVortex = clienteVortex;
	}

	public static PCVolumeMain create(final String computerId) {
		final PCVolumeMain main = new PCVolumeMain();
		main.initialize();
		main.computerId = computerId;
		return main;
	}

	/**
	 * Inicializa esta instancia para funcionar global
	 */
	private void initialize() {
		interpreteJson = InterpreteJackson.create();
		volumeChanger = JavaSoundChanger.create();
		crearClienteVortex();
		createProcessor();
	}

	/**
	 * 
	 */
	private void createProcessor() {
		processor = ExecutorBasedTaskProcesor.create();
	}

	/**
	 * 
	 */
	private void crearClienteVortex() {
		final ConfiguracionSinEncriptacion configuracionNodo = ConfiguracionSinEncriptacion.create(
				"http://kfgodel.info/vortex/controllers/naked", null);
		configuracionNodo.setPeriodoDePollingEnSegundos(4L);
		final NodoVortex nodoOnline = NodoRemotoHttp.create(configuracionNodo, "Nodo vortex online");
		final ConfiguracionClienteVortex configuracionCliente = ConfiguracionClienteVortex.create(nodoOnline, this);
		clienteVortex = ClienteVortexImpl.create(configuracionCliente);
		clienteVortex.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet("VORTEX.PC.VOLUME.CHANGE"));
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.HandlerDeMensajesApi#onMensajeRecibido(net.gaia.vortex.hilevel.api.MensajeVortexApi)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortexApi mensajeRecibido) {
		final String tipoDeContenido = mensajeRecibido.getTipoDeContenido();
		if ("vortex.pc-volume.change".equals(tipoDeContenido)) {
			recibirMensajeDeCambio(mensajeRecibido.getContenido());
			return;
		}
		LOG.error("Recibimos un mensaje en el TAG que no indica com tipo \"vortex.pc-volume.change\". Ignorando");
	}

	/**
	 * Intenta interpretar el mensaje como un cambio de volumen. Si es para esta máquina efectiviza
	 * el cambio
	 * 
	 * @param contenido
	 */
	private void recibirMensajeDeCambio(final Object contenido) {
		String json;
		try {
			json = (String) contenido;
		} catch (final ClassCastException e) {
			LOG.error("Se recibió un mensaje cuyo contenido no es String");
			return;
		}
		final PcVolumenChange volumeChange = interpreteJson.fromJson(json, PcVolumenChange.class);
		final String changedComputerId = volumeChange.getComputerId();
		if (!Objects.equal(this.computerId, changedComputerId)) {
			// El cambio es para otra PC
			return;
		}
		int volumenLevel = volumeChange.getVolumenLevel();
		if (volumenLevel > 100) {
			volumenLevel = 100;
		} else if (volumenLevel < 0) {
			volumenLevel = 0;
		}
		LOG.debug("Se recibió pedido de cambio de volumen a: {}", volumenLevel);
		volumeChanger.changeTo(volumenLevel);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.HandlerDeMensajesApi#onReporteDeEntregaRecibido(net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi)
	 */
	@Override
	public void onReporteDeEntregaRecibido(final ReporteDeEntregaApi reporte) {
		LOG.debug("Reporte vortex entregado?: {}", reporte);
	}
}
