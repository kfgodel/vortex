/**
 * 13/10/2012 22:12:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.patas.filtros;

import java.util.Collection;
import java.util.Set;

import net.gaia.vortex.tests.router.Mensaje;

/**
 * Esta clase representa un filtro que solo deja pasar los mensajes con cadenas predefinidas
 * 
 * @author D. García
 */
public class FiltroPorStrings implements Filtro {

	public static final String META_MENSAJE = "META_MENSAJE";

	private Set<String> permitidos;

	public static FiltroPorStrings create(final Set<String> permitidos) {
		final FiltroPorStrings name = new FiltroPorStrings();
		name.permitidos = permitidos;
		return name;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.patas.filtros.Filtro#aceptaA(net.gaia.vortex.tests.router.Mensaje)
	 */
	@Override
	public boolean aceptaA(final Mensaje mensaje) {
		final String tagDelMensaje = mensaje.getTag();
		if (META_MENSAJE.equals(tagDelMensaje)) {
			return true;
		}
		final boolean esAceptado = permitidos.contains(tagDelMensaje);
		return esAceptado;
	}

	public Set<String> getPermitidos() {
		return permitidos;
	}

	public void setPermitidos(final Set<String> permitidos) {
		this.permitidos = permitidos;
	}

	/**
	 * @see net.gaia.vortex.tests.router.impl.patas.filtros.Filtro#usaA(java.util.Collection)
	 */
	@Override
	public boolean usaA(final Collection<String> filtros) {
		for (final String filtro : filtros) {
			if (!getPermitidos().contains(filtro)) {
				return false;
			}
		}
		return true;
	}

}
