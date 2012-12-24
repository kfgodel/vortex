/**
 * 22/12/2012 19:08:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.patas;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.memoria.NexoFiltroDuplicados;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;

/**
 * Esta clase es la implementación de la pata bidireccional
 * 
 * @author D. García
 */
public class PataBidi extends ComponenteConProcesadorSupport implements PataBidireccional {

	private static final AtomicLong proximoId = new AtomicLong(0);

	private NodoBidireccional nodoLocal;
	public static final String nodoLocal_FIELD = "nodoLocal";

	private Receptor nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private Long idLocal;
	public static final String idLocal_FIELD = "idLocal";

	private Long idRemoto;
	public static final String idRemoto_FIELD = "idRemoto";

	private ParteDeCondiciones filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	private Condicion filtroDeEntrada;
	public static final String filtroDeEntrada_FIELD = "filtroDeEntrada";

	private Condicion filtroDeEntradaPublicado;
	public static final String filtroDeEntradaPublicado_FIELD = "filtroDeEntradaPublicado";

	private MemoriaDeMensajes memoriaDeEnviados;

	private ListenerDeCambioDeFiltroEnPata listener;

	public static PataBidi create(final NodoBidireccional nodoLocal, final Receptor nodoRemoto,
			final ListenerDeCambioDeFiltroEnPata listener, final TaskProcessor taskProcessor,
			final ParteDeCondiciones parteDeCondicion) {
		final PataBidi pata = new PataBidi();
		pata.initializeWith(taskProcessor);
		pata.filtroDeSalida = parteDeCondicion;
		pata.setIdLocal(proximoId.getAndIncrement());
		pata.nodoLocal = nodoLocal;
		pata.nodoRemoto = nodoRemoto;
		pata.listener = listener;
		pata.memoriaDeEnviados = MemoriaLimitadaDeMensajes.create(NexoFiltroDuplicados.CANTIDAD_MENSAJES_RECORDADOS);
		pata.inicializarFiltros();
		return pata;
	}

	/**
	 * Establece el estado inicial de los filtros en esta pata
	 */
	private void inicializarFiltros() {
		// Inicialmente entra y sale todo lo que recibamos
		this.filtroDeSalida.cambiarA(SiempreTrue.getInstancia());
		this.filtroDeEntrada = SiempreTrue.getInstancia();
		this.resetearFiltroDeEntradaPublicado();
	}

	/**
	 * Cambia el estado de publicación del filtro de entrada. Marcándolo como si el filtro de
	 * entrada aún no estuviese publicado (para posterior publicación)
	 */
	private void resetearFiltroDeEntradaPublicado() {
		this.filtroDeEntradaPublicado = FiltroNoPublicado.getInstancia();
	}

	public NodoBidireccional getNodoLocal() {
		return nodoLocal;
	}

	public void setNodoLocal(final NodoBidireccional nodoLocal) {
		this.nodoLocal = nodoLocal;
	}

	public Receptor getNodoRemoto() {
		return nodoRemoto;
	}

	public void setNodoRemoto(final Receptor nodoRemoto) {
		this.nodoRemoto = nodoRemoto;
	}

	public Long getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(final Long idLocal) {
		this.idLocal = idLocal;
	}

	public Long getIdRemoto() {
		return idRemoto;
	}

	public void setIdRemoto(final Long idRemoto) {
		this.idRemoto = idRemoto;
	}

	/**
	 * Comienza el intercambio de meta-mensajes para determinar con qué otra pata se está hablando,
	 * y poder establecer comunicaciones bidireccionales.<br>
	 * Esta acción mandará un meta-mensaje por esta pata para solicitar un ID de pata remota
	 */
	public void conseguirIdRemoto() {
		// TODO Auto-generated method stub

	}

}
