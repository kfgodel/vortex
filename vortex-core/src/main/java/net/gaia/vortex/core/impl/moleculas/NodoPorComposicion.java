/**
 * 07/07/2012 00:48:02 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteSupport;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorVariable;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un nodo que define su comportamiento por composición de átomos
 * 
 * @author D. García
 */
@Molecula
public class NodoPorComposicion extends ComponenteSupport implements Nodo {

	private ReceptorVariable<Receptor> atomoInput;
	public static final String atomoInput_FIELD = "atomoInput";

	private Emisor atomoOutput;
	public static final String atomoOutput_FIELD = "atomoOutput";

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		atomoOutput.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		atomoOutput.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		Loggers.ATOMOS.trace("Recibido en nodo[{}] el mensaje[{}]", this.toShortString(), mensaje);
		final Receptor receptorActual = atomoInput.getReceptorActual();
		Loggers.ATOMOS.debug("Delegando a nodo input[{}] el mensaje[{}] desde[{}]",
				new Object[] { receptorActual.toShortString(), mensaje, this.toShortString() });
		receptorActual.recibir(mensaje);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeComponente_FIELD, getNumeroDeComponente())
				.con(atomoInput_FIELD, atomoInput).con(atomoOutput_FIELD, atomoOutput).toString();
	}

	public static NodoPorComposicion create(final Receptor atomoParaInput, final Emisor atomoParaOutput) {
		final NodoPorComposicion nodoPorComposicion = new NodoPorComposicion();
		nodoPorComposicion.atomoInput = ReceptorVariable.create(atomoParaInput);
		nodoPorComposicion.atomoOutput = atomoParaOutput;
		return nodoPorComposicion;
	}
}