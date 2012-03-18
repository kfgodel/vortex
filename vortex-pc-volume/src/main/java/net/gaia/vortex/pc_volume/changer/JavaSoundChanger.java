/**
 * 17/03/2012 23:53:32 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el cambiador de volumen utilizando la api de JavaSound
 * 
 * @author D. García
 */
public class JavaSoundChanger implements PcVolumeChanger {
	private static final Logger LOG = LoggerFactory.getLogger(JavaSoundChanger.class);

	public static JavaSoundChanger create() {
		final JavaSoundChanger changer = new JavaSoundChanger();
		changer.initialize();
		return changer;
	}

	private ArrayList<VolumeControl> volumeControls;

	/**
	 * Intenta encontrar los controles de volumen
	 * 
	 */
	private void initialize() {
		this.volumeControls = findVolumeControls();
		if (volumeControls.isEmpty()) {
			System.out.println("No fue posible determinar el control de volumen en esta máquina");
			System.exit(0);
		}
	}

	/**
	 * Intenta encontrar los controles de volumen de esta máquina
	 * 
	 * @return Los controles encontrados
	 */
	private ArrayList<VolumeControl> findVolumeControls() {
		final ArrayList<VolumeControl> volumeControls = new ArrayList<VolumeControl>();
		final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixers.length; i++) {
			final Mixer.Info mixerInfo = mixers[i];
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);

			final Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source
			for (int j = 0; j < lineInfos.length; j++) {
				final Line.Info lineInfo = lineInfos[j];

				Line line;
				try {
					line = mixer.getLine(lineInfo);
				} catch (final LineUnavailableException e) {
					LOG.error(
							"No es posible obtener la linea[" + lineInfo + "] del mixer[" + mixerInfo.getName() + "]",
							e);
					continue;
				}
				boolean openedHere = false;
				if (!line.isOpen()) {
					try {
						line.open();
					} catch (final LineUnavailableException e) {
						LOG.error("La linea[" + line.getLineInfo() + "] no está dispponible", e);
					}
					openedHere = true;
				}
				try {
					if (!line.isControlSupported(FloatControl.Type.VOLUME)) {
						// No es este el que buscamos
						continue;
					}
					final FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
					final VolumeControl volumeControl = VolumeControl.createFor(line);
					volumeControls.add(volumeControl);
				} finally {
					if (openedHere) {
						line.close();
					}
				}
			}
		}
		return volumeControls;
	}

	/**
	 * @see net.gaia.vortex.pc_volume.changer.PcVolumeChanger#changeTo(int)
	 */
	@Override
	public void changeTo(final int volumenLevel) {
		for (final VolumeControl volumeControl : volumeControls) {
			volumeControl.setLevel(volumenLevel);
		}
	}
}
