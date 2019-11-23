package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Signal;

/**
 * Sawtooth with another phase
 */
public class SawtoothAltWave extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        return -(TWO_ON_PI) * Math.atan(1 / Math.tan(Math.PI * fundamentalFrequency * time));
    }
}
