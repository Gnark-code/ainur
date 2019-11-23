package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Harmonic;
import fr.gnark.sound.domain.media.Signal;

public class SineWave extends Signal {

    public static final SineWave sineWave() {
        return new SineWave();
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        double temp = 2 * Math.PI * fundamentalFrequency * time;
        double result = Math.sin(temp);
        for (final Harmonic harmonic : harmonics) {
            result += harmonic.getAmplitude() * Math.sin(temp * harmonic.getRank());
        }
        return result;
    }
}
