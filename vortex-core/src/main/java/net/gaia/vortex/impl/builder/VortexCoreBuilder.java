/**
 * 22/08/2013 01:14:52 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.builder;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.GeneradorDeIdsDeMensajes;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Identificador;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.atomos.AtomoBifurcador;
import net.gaia.vortex.impl.atomos.AtomoMultiplexor;
import net.gaia.vortex.impl.atomos.AtomoSecuenciador;
import net.gaia.vortex.impl.atomos.AtomoTransformador;
import net.gaia.vortex.impl.condiciones.EsMensajeNuevo;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaDeMensajes;
import net.gaia.vortex.impl.mensajes.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.impl.moleculas.MoleculaCompuesta;
import net.gaia.vortex.impl.moleculas.MoleculaDistribuidor;
import net.gaia.vortex.impl.moleculas.MoleculaIdentificador;
import net.gaia.vortex.impl.moleculas.MoleculaSelector;
import net.gaia.vortex.impl.moleculas.MoleculaTerminal;
import net.gaia.vortex.impl.proto.ConectorAsincrono;
import net.gaia.vortex.impl.proto.ConectorBloqueante;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el builder de instancias para las clases del core de vortex
 * 
 * @author D. García
 */
public class VortexCoreBuilder implements VortexCore {

	/**
	 * Cantidad máxima de mensajes registrados en una memoria de nodo para detectar duplicados
	 */
	public static final int CANTIDAD_MENSAJES_RECORDADOS = 1000;

	private TaskProcessor processor;

	/**
	 * Crea un builder de componentes vortex que comparte el procesador pasado con otras instancias
	 * 
	 * @param processor
	 *            El procesador a utilizar en los componentes que tienen procesamiento autonomo
	 * @return El builder creado
	 */
	public static VortexCoreBuilder create(final TaskProcessor processor) {
		final VortexCoreBuilder builder = new VortexCoreBuilder();
		builder.processor = processor;
		return builder;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#secuenciar(net.gaia.vortex.api.basic.Receptor,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	public Secuenciador secuenciar(final Receptor delegado, final Receptor salida) {
		final Secuenciador secuenciador = secuenciadorDe(delegado);
		secuenciador.conectarCon(salida);
		return secuenciador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#secuenciadorDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public Secuenciador secuenciadorDe(final Receptor delegado) {
		final AtomoSecuenciador secuenciador = AtomoSecuenciador.create(delegado);
		return secuenciador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#multiplexar(net.gaia.vortex.api.basic.Receptor[])
	 */
	public Multiplexor multiplexar(final Receptor... receptores) {
		final AtomoMultiplexor multiplexor = AtomoMultiplexor.create();
		for (final Receptor receptor : receptores) {
			multiplexor.conectarCon(receptor);
		}
		return multiplexor;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#multiplexarSinDuplicados(net.gaia.vortex.api.basic.Receptor[])
	 */
	public Compuesto<Filtro, Multiplexor> multiplexarSinDuplicados(final Receptor... receptores) {
		final Multiplexor multiplexor = multiplexar(receptores);
		final Filtro filtroSinDuplicados = filtrarMensajesDuplicadosA(multiplexor);
		final Compuesto<Filtro, Multiplexor> molecula = componer(filtroSinDuplicados, multiplexor);
		return molecula;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#componer(net.gaia.vortex.api.basic.Receptor,
	 *      net.gaia.vortex.api.basic.Emisor)
	 */
	public <E extends Receptor, S extends Conectable> Compuesto<E, S> componer(final E entrada, final S salida) {
		final Compuesto<E, S> molecula = MoleculaCompuesta.create(entrada, salida);
		return molecula;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#bifurcarSi(net.gaia.vortex.api.condiciones.Condicion,
	 *      net.gaia.vortex.api.basic.Receptor, net.gaia.vortex.api.basic.Receptor)
	 */
	public Bifurcador bifurcarSi(final Condicion condicion, final Receptor receptorPorTrue,
			final Receptor receptorPorFalse) {
		final AtomoBifurcador bifurcador = AtomoBifurcador.create(condicion);
		bifurcador.conectarPorTrueCon(receptorPorTrue);
		bifurcador.conectarPorFalseCon(receptorPorFalse);
		return bifurcador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtrarEntradaCon(net.gaia.vortex.api.condiciones.Condicion,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	public Filtro filtrarEntradaCon(final Condicion condicion, final Receptor receptor) {
		final Filtro filtro = filtroDe(condicion);
		filtro.conectarPorTrueCon(receptor);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtroDe(net.gaia.vortex.api.condiciones.Condicion)
	 */
	public Filtro filtroDe(final Condicion condicion) {
		final AtomoBifurcador filtro = AtomoBifurcador.create(condicion);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#transformarCon(net.gaia.vortex.api.transformaciones.Transformacion,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	public Transformador transformarCon(final Transformacion transformacion, final Receptor receptor) {
		final Transformador transformador = transformadorPara(transformacion);
		transformador.conectarCon(receptor);
		return transformador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#transformadorPara(net.gaia.vortex.api.transformaciones.Transformacion)
	 */
	public Transformador transformadorPara(final Transformacion transformacion) {
		final AtomoTransformador transformador = AtomoTransformador.create(transformacion);
		return transformador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtrarMensajesDuplicadosA(net.gaia.vortex.api.basic.Receptor)
	 */
	public Filtro filtrarMensajesDuplicadosA(final Receptor receptor) {
		final Filtro filtro = filtroSinMensajesDuplicados();
		filtro.conectarPorTrueCon(receptor);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtroSinMensajesDuplicados()
	 */
	public Filtro filtroSinMensajesDuplicados() {
		final Filtro filtroCreado = filtroDe(condicionSinDuplicados());
		return filtroCreado;
	}

	/**
	 * Crea una condición para evitar mensajes duplicados
	 * 
	 * @return La condicion que devuelve true cuando el mensaje es nuevo
	 */
	private EsMensajeNuevo condicionSinDuplicados() {
		final MemoriaDeMensajes memoriaDelFiltro = crearMemoriaDeMensajes();
		final EsMensajeNuevo condicion = EsMensajeNuevo.create(memoriaDelFiltro);
		return condicion;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtrarSalidaDe(net.gaia.vortex.api.atomos.Conector,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */
	public Filtro filtrarSalidaDe(final Conectable conector, final Condicion condicion) {
		final Filtro filtro = filtroDe(condicion);
		conector.conectarCon(filtro);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#selector()
	 */
	public Selector selector() {
		final MoleculaSelector selector = MoleculaSelector.create(this);
		return selector;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#asincronizar(net.gaia.vortex.api.basic.Receptor)
	 */
	public Conector asincronizar(final Receptor receptor) {
		final Conector conectorCreado = conectorAsicrono();
		conectorCreado.conectarCon(receptor);
		return conectorCreado;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#getProcessor()
	 */
	public TaskProcessor getProcessor() {
		return processor;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#identificador()
	 */
	public Identificador identificador() {
		return MoleculaIdentificador.create(this);
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#crearIdDeComponente()
	 */
	public IdDeComponenteVortex crearIdDeComponente() {
		final IdDeComponenteVortex idDeComponeteCreado = GeneradorDeIdsGlobalesParaComponentes.getInstancia()
				.generarId();
		return idDeComponeteCreado;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#crearMemoriaDeMensajes()
	 */
	public MemoriaDeMensajes crearMemoriaDeMensajes() {
		final MemoriaDeMensajes memoriaDelFiltro = MemoriaLimitadaDeMensajes.create(CANTIDAD_MENSAJES_RECORDADOS);
		return memoriaDelFiltro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#crearGeneradorDeIdsParaMensajes(net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex)
	 */
	public GeneradorDeIdsDeMensajes crearGeneradorDeIdsParaMensajes(final IdDeComponenteVortex idAsignado) {
		final GeneradorSecuencialDeIdDeMensaje generador = GeneradorSecuencialDeIdDeMensaje.create(idAsignado);
		return generador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#distribuidor()
	 */
	public Distribuidor distribuidor() {
		return MoleculaDistribuidor.create(this);
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#conector()
	 */
	public Conector conector() {
		return ConectorBloqueante.create();
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#terminal()
	 */
	public Terminal terminal() {
		return MoleculaTerminal.create(this);
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#conectorAsicrono()
	 */
	public Conector conectorAsicrono() {
		return ConectorAsincrono.create(processor);
	}
}
