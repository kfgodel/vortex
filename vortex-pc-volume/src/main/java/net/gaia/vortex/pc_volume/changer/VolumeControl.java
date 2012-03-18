/**
 * 18/03/2012 00:03:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.pc_volume.changer;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un control de volumen encontrado
 * 
 * @author D. García
 */
public class VolumeControl {
	private static final Logger LOG = LoggerFactory.getLogger(VolumeControl.class);
	private Line line;

	public static VolumeControl createFor(final Line hostLine) {
		final VolumeControl name = new VolumeControl();
		name.line = hostLine;
		return name;
	}

	/**
	 * Cambia el volumen de este control al nivel indicado
	 * 
	 * @param volumenLevel
	 *            El nivel como un entero que indica porcentaje
	 */
	public void setLevel(final int volumenLevel) {
		boolean openedHere = false;
		if (!line.isOpen()) {
			try {
				line.open();
			} catch (final LineUnavailableException e) {
				LOG.error("No se pudo abrir la linea para cambiar el volumen", e);
				return;
			}
			openedHere = true;
		}
		try {
			final FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
			final float newVolume = adjustRangeOf(volumenLevel, volumeControl);
			volumeControl.setValue(newVolume);
			LOG.info("Volumen de Linea[{}] cambiado a: {}", line.getLineInfo(), newVolume);
		} finally {
			if (openedHere) {
				line.close();
			}
		}
	}

	/**
	 * Ajusta el rango del volumen expresado para satisfacer los valores del control
	 * 
	 * @param volumenLevel
	 *            El nivel deseado
	 * @param volumeControl
	 * @return El mismo nivel expresado en funcion del control
	 */
	private float adjustRangeOf(final int volumenLevel, final FloatControl volumeControl) {
		final float maximum = volumeControl.getMaximum();
		final float minimum = volumeControl.getMinimum();
		final float delta = maximum - minimum;
		final float newVolume = minimum + delta * (volumenLevel / 100f);
		return newVolume;
	}

}
