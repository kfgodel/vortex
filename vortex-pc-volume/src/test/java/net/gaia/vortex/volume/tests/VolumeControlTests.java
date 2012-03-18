package net.gaia.vortex.volume.tests;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

import org.junit.Ignore;
import org.junit.Test;

/**
 * 15/03/2012 00:36:06 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commonibution 3.0 Unported License</a>.
 */

/**
 * Esta clase prueba el control de volumen de la PC
 * 
 * @author D. García
 */
public class VolumeControlTests {

	@Ignore
	@Test
	public void deberiaIndicarCualEsElVolumenActual() throws LineUnavailableException {
		final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		System.out.println("Hay " + mixers.length + " mixers");
		for (int i = 0; i < mixers.length; i++) {
			final Mixer.Info mixerInfo = mixers[i];
			System.out.println("-Mixer[" + i + "]: " + mixerInfo.getName());
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			final Control[] mixerControls = mixer.getControls();
			System.out.println("\tTiene " + mixerControls.length + " controles:");
			for (int j = 0; j < mixerControls.length; j++) {
				final Control control = mixerControls[j];
				System.out.println("\t-Control[" + j + "]:" + control.getType());
				if (control.getType().equals(FloatControl.Type.VOLUME)) {
					final FloatControl volCtrl = (FloatControl) control;
					System.out.println("\t\tvolCtrl.getValue() = " + volCtrl.getValue());
				}
			}

			final Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source
			System.out.println("\tTiene " + lineInfos.length + " lineInfos");
			for (int j = 0; j < lineInfos.length; j++) {
				final Line.Info lineInfo = lineInfos[j];
				System.out.println("\t-Line.Info[" + j + "]: " + lineInfo);

				final Line line = mixer.getLine(lineInfo);
				boolean openedHere = false;
				if (!line.isOpen()) {
					line.open();
					openedHere = true;
				}
				try {
					final Control[] controls = line.getControls();
					System.out.println("\t\tHay " + controls.length + " controles disponibles");
					for (int k = 0; k < controls.length; k++) {
						final Control control = controls[k];
						System.out.println("\t\t-Control[" + k + "]:" + control.getType());
						if (control.getType().equals(FloatControl.Type.VOLUME)) {
							final FloatControl volCtrl = (FloatControl) control;
							System.out.println("\t\t\tvolCtrl.getValue() = " + volCtrl.getValue());
						}
					}
				} finally {
					if (openedHere) {
						line.close();
					}
				}
			}
		}

	}

	@Ignore
	@Test
	public void deberíaBajarElVolumenAlMinimo() throws LineUnavailableException {
		final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		final Mixer.Info mixerInfo = mixers[5];
		final Mixer mixer = AudioSystem.getMixer(mixerInfo);
		final Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source
		final Info lineInfo = lineInfos[0];
		final Line line = mixer.getLine(lineInfo);
		if (!line.isOpen()) {
			line.open();
		}
		final FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
		volCtrl.setValue(0);
		line.close();
	}

	@Ignore
	@Test
	public void deberiarSubirAlMaximo() throws LineUnavailableException {
		final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		final Mixer.Info mixerInfo = mixers[5];
		final Mixer mixer = AudioSystem.getMixer(mixerInfo);
		final Line.Info[] lineInfos = mixer.getTargetLineInfo(); // target, not source
		final Info lineInfo = lineInfos[0];
		final Line line = mixer.getLine(lineInfo);
		if (!line.isOpen()) {
			line.open();
		}
		final FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
		volCtrl.setValue(1);
		line.close();
	}

}
