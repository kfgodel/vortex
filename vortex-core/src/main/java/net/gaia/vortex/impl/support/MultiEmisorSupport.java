/**
 * 20/08/2013 01:26:54 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.vortex.api.basic.emisores.MultiEmisor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.proto.ComponenteConector;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase sirve de base para los emisores de multiples conectores
 * 
 * @author D. García
 */
public class MultiEmisorSupport extends EmisorSupport implements MultiEmisor {

	private List<Conector> conectores;
	public static final String conectores_FIELD = "conectores";

	protected void inicializar() {
		this.conectores = new CopyOnWriteArrayList<Conector>();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MultiEmisor#crearConector()
	 */
	public Conector crearConector() {
		final ComponenteConector conectorCreado = ComponenteConector.create();
		getConectores().add(conectorCreado);
		return conectorCreado;
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MultiEmisor#getConectores()
	 */
	public List<Conector> getConectores() {
		return conectores;
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MultiEmisor#eliminarConector(net.gaia.vortex.api.proto.Conector)
	 */
	public void eliminarConector(final Conector conector) {
		getConectores().remove(conector);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(conectores_FIELD, conectores).toString();
	}

}
