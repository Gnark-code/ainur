package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Signal;

public class TriangleWave extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        return (TWO_ON_PI) * Math.asin(Math.sin(2 * Math.PI * fundamentalFrequency * time));
    }
}
