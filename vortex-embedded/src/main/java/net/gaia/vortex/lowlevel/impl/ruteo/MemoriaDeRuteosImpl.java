/**
 * 21/01/2012 19:59:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo;

import java.util.Iterator;
import java.util.Set;

import net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import ar.com.fdvs.dgarcia.colecciones.sets.ConcurrentHashSet;

/**
 * Esta clase es la implementación de la memoria de ruteos activos
 * 
 * @author D. García
 */
public class MemoriaDeRuteosImpl implements MemoriaDeRuteos {

	private Set<IdentificadorDeEnvio> ruteosActivos;

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos#registrarRuteoActivo(net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio)
	 */
	@Override
	public void registrarRuteoActivo(final IdentificadorDeEnvio idEnvioRecibido) {
		ruteosActivos.add(idEnvioRecibido);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos#registrarRuteoTerminado(net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio)
	 */
	@Override
	public void registrarRuteoTerminado(final IdentificadorDeEnvio idEnvioRecibido) {
		ruteosActivos.remove(idEnvioRecibido);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos#tieneRuteoActivoPara(net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio)
	 */
	@Override
	public boolean tieneRuteoActivoPara(final IdentificadorDeEnvio idEnvioRealizado) {
		final boolean tieneRuteo = ruteosActivos.contains(idEnvioRealizado);
		return tieneRuteo;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos#eliminarRuteosActivosPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void eliminarRuteosActivosPara(final ReceptorVortex receptorCerrado) {
		final Iterator<IdentificadorDeEnvio> ruteosIterator = this.ruteosActivos.iterator();
		while (ruteosIterator.hasNext()) {
			final IdentificadorDeEnvio identificadorDeEnvio = ruteosIterator.next();
			if (identificadorDeEnvio.esPara(receptorCerrado)) {
				ruteosIterator.remove();
			}
		}
	}

	public static MemoriaDeRuteosImpl create() {
		final MemoriaDeRuteosImpl memeoria = new MemoriaDeRuteosImpl();
		memeoria.ruteosActivos = new ConcurrentHashSet<IdentificadorDeEnvio>();
		return memeoria;
	}
}
