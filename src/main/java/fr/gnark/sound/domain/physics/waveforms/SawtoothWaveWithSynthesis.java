package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;

public class SawtoothWaveWithSynthesis extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        double twoPiF = 2 * Math.PI * fundamentalFrequency;
        double result = 0;
        for (final Harmonic harmonic : harmonics) {
            final int k = harmonic.getRank();
            final int sign;
            if (harmonic.isOdd()) {
                sign = -1;
            } else {
                sign = 1;
            }
            result -= (TWO_ON_PI) * sign * (Math.sin(twoPiF * k * time) / k);
        }

        return result;
    }
}
