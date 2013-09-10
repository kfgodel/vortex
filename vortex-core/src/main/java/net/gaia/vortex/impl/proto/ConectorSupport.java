/**
 * Created on: Sep 1, 2013 5:36:54 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.proto;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.support.ComponenteSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase ofrece la base para crear conectores definiendo los metodos para conectar a un
 * receptor destino
 * 
 * @author dgarcia
 */
public abstract class ConectorSupport extends ComponenteSupport implements Conector {

	protected Receptor conectado;
	public static final String conectado_FIELD = "conectado";

	public Receptor getConectado() {
		return conectado;
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El receptor destino no puede ser null. Usar el " + ReceptorNulo.class);
		}
		conectado = destino;
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#desconectar()
	 */
	public void desconectar() {
		final ReceptorNulo receptorNulo = ReceptorNulo.getInstancia();
		conectarCon(receptorNulo);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(conectado_FIELD, conectado).toString();
	}

	/**
	 * @see net.gaia.vortex.api.proto.Conector#getDestino()
	 */
	public Receptor getDestino() {
		return conectado;
	}

}