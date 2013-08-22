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

import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.condiciones.EsMensajeNuevo;
import net.gaia.vortex.core.impl.memoria.MemoriaDeMensajes;
import net.gaia.vortex.core.impl.memoria.MemoriaLimitadaDeMensajes;
import net.gaia.vortex.impl.atomos.AtomoBifurcador;
import net.gaia.vortex.impl.atomos.AtomoMultiplexor;
import net.gaia.vortex.impl.atomos.AtomoSecuenciador;
import net.gaia.vortex.impl.atomos.AtomoTransformador;
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

	public static VortexCoreBuilder create() {
		final VortexCoreBuilder builder = new VortexCoreBuilder();
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
		secuenciador.getConectorUnico().conectarCon(salida);
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
			multiplexor.crearConector().conectarCon(receptor);
		}
		return multiplexor;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#bifurcarSi(net.gaia.vortex.core.api.condiciones.Condicion,
	 *      net.gaia.vortex.api.basic.Receptor, net.gaia.vortex.api.basic.Receptor)
	 */
	public Bifurcador bifurcarSi(final Condicion condicion, final Receptor receptorPorTrue,
			final Receptor receptorPorFalse) {
		final AtomoBifurcador bifurcador = AtomoBifurcador.create(condicion);
		bifurcador.getConectorPorTrue().conectarCon(receptorPorTrue);
		bifurcador.getConectorPorFalse().conectarCon(receptorPorFalse);
		return bifurcador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtrarCon(net.gaia.vortex.core.api.condiciones.Condicion,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	public Bifurcador filtrarCon(final Condicion condicion, final Receptor receptor) {
		final Bifurcador filtro = filtroDe(condicion);
		filtro.getConectorPorTrue().conectarCon(receptor);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtroDe(net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	public Bifurcador filtroDe(final Condicion condicion) {
		final AtomoBifurcador filtro = AtomoBifurcador.create(condicion);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#transformarCon(net.gaia.vortex.core.api.transformaciones.Transformacion,
	 *      net.gaia.vortex.api.basic.Receptor)
	 */
	public Transformador transformarCon(final Transformacion transformacion, final Receptor receptor) {
		final Transformador transformador = transformadorPara(transformacion);
		transformador.getConectorUnico().conectarCon(receptor);
		return transformador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#transformadorPara(net.gaia.vortex.core.api.transformaciones.Transformacion)
	 */
	public Transformador transformadorPara(final Transformacion transformacion) {
		final AtomoTransformador transformador = AtomoTransformador.create(transformacion);
		return transformador;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#sinDuplicadosPara(net.gaia.vortex.api.basic.Receptor)
	 */
	public Bifurcador sinDuplicadosPara(final Receptor receptor) {
		final Bifurcador filtro = filtroSinDuplicados();
		filtro.getConectorPorTrue().conectarCon(receptor);
		return filtro;
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexCore#filtroSinDuplicados()
	 */
	public Bifurcador filtroSinDuplicados() {
		final MemoriaDeMensajes memoriaDelFiltro = MemoriaLimitadaDeMensajes.create(CANTIDAD_MENSAJES_RECORDADOS);
		final EsMensajeNuevo condicion = EsMensajeNuevo.create(memoriaDelFiltro);
		final Bifurcador filtroCreado = filtroDe(condicion);
		return filtroCreado;
	}

}
