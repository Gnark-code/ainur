package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;

public class SquareWaveWithSynthesis extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {

        double result = 0;
        for (final Harmonic harmonic : harmonics) {
            double twoPiF = 2 * Math.PI * harmonic.getFrequencyFromVariation(fundamentalFrequency);
            int k = harmonic.getRank();
            if (harmonic.isOdd()) {
                result += (FOUR_ON_PI * (Math.sin(twoPiF * time) / k));
            }
        }
        result = result / 2;

        return result;

    }
}
