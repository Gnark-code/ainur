package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Signal;

public class SawtoothWave extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        final double tf = time * fundamentalFrequency;
        return 2 * (tf - Math.floor(0.5 + tf));
    }
}
