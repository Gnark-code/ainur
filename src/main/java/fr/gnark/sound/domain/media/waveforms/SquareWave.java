package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Signal;

public class SquareWave extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        return Math.signum(Math.sin(2 * Math.PI * fundamentalFrequency * time));
    }
}
