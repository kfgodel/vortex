/**
 * 20/08/2013 00:55:17 Copyright (C) 2013 Darío L. García
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

import net.gaia.vortex.api.basic.emisores.MonoConectable;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.proto.ConectorBloqueante;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase sirve de base para los componentes que solo tienen un conector de salida
 * 
 * @author D. García
 */
public class MonoConectableSupport extends EmisorSupport implements MonoConectable {

	private Conector conectorUnico;
	public static final String conectorUnico_FIELD = "conectorUnico";

	/**
	 * @see net.gaia.vortex.api.basic.emisores.MonoEmisor#getConectorDeSalida()
	 */
	public Conector getConectorDeSalida() {
		return conectorUnico;
	}

	protected void inicializar() {
		this.conectorUnico = ConectorBloqueante.create();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(conectorUnico_FIELD, conectorUnico).toString();
	}

}
