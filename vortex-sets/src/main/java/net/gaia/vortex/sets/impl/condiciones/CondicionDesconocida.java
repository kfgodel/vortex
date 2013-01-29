/**
 * 20/01/2013 19:40:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.condiciones;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.gaia.vortex.core.api.annotations.Paralelizable;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que es desconocida por esta aplicación y que puede formar
 * parte de otras condiciones sí conocidas.<br>
 * La condición desconocida modela partes de otras condiciones que tienen una semántica desconocida
 * por esta aplicación pero no por eso son intratables.<br>
 * Bajo una condición desconocida los routers serán más benevolentes dejando pasar mensajes que no
 * entienden
 * 
 * @author D. García
 */
@Paralelizable
public class CondicionDesconocida implements Condicion {

	private static final WeakSingleton<CondicionDesconocida> ultimaReferencia = new WeakSingleton<CondicionDesconocida>(
			DefaultInstantiator.create(CondicionDesconocida.class));

	public static CondicionDesconocida getInstancia() {
		return ultimaReferencia.get();
	}

	private Map<String, Object> formaOriginal;
	public static final String formaOriginal_FIELD = "formaOriginal";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		return ResultadoDeCondicion.INDECIDIBLE;
	}

	public static CondicionDesconocida create(final Map<String, Object> formaOriginal) {
		final CondicionDesconocida condicion = new CondicionDesconocida();
		condicion.formaOriginal = formaOriginal;
		return condicion;
	}

	public Map<String, Object> getFormaOriginal() {
		return formaOriginal;
	}

	public void setFormaOriginal(final Map<String, Object> formaOriginal) {
		this.formaOriginal = formaOriginal;
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	@Override
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(formaOriginal_FIELD, formaOriginal).toString();
	}

}
