package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Harmonic;
import fr.gnark.sound.domain.media.Signal;

public class SquareWaveWithSynthesis extends Signal {
    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        double twoPiF = 2 * Math.PI * fundamentalFrequency;
        double result = FOUR_ON_PI * (Math.sin(twoPiF * time));
        for (final Harmonic harmonic : harmonics) {
            int k = harmonic.getRank();
            if (harmonic.isOdd()) {
                result += (FOUR_ON_PI * (Math.sin(k * twoPiF * time) / k));
            }
        }
        result = result / 2;

        return result;

    }
}
