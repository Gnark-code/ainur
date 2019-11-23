package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Signal;

public class TriangleWave extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        return (TWO_ON_PI) * Math.asin(Math.sin(2 * Math.PI * fundamentalFrequency * time));
    }
}
