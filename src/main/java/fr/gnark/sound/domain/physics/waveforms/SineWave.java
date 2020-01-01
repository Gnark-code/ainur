package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;

public class SineWave extends Signal {

    public static final SineWave sineWave() {
        return new SineWave();
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        double twoPiF = 2 * Math.PI * fundamentalFrequency * time;
        double result = 0;
        for (final Harmonic harmonic : harmonics) {
            result += harmonic.getAmplitude() * Math.sin(twoPiF * harmonic.getRank());
        }
        return result;
    }
}
