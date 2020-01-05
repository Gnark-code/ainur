package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Math.*;

/**
 * Equations courtesy of Vogel Scheer
 * https://www.vogel-scheer.de/images/pdf/Infos/Saiten-Scholl-StringingTheHarpsichord-en.pdf
 */
@Slf4j
public class Harpsichord extends Signal {
    private final double attackInSeconds;
    private static final double MAX_PLUCKING_RATIO = 0.5;
    private static final double MIN_PLUCKING_RATIO = 0.01;
    private static final double MAX_OCTAVE_RATIO = 1.0;
    private static final double MIN_OCTAVE_RATIO = 0.0;
    private double pluckingRatio = 0.3;
    private double octaveRatio = 0.40;

    public Harpsichord(final double attackInSeconds) {
        this.attackInSeconds = attackInSeconds;
        this.addOvertones(31);
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        final double valueForFrequency = compute(fundamentalFrequency, time);
        final double valueForOctave = compute(fundamentalFrequency / 2, time);
        return (1.0 - octaveRatio) * valueForFrequency + octaveRatio * valueForOctave;
    }

    private double compute(final double fundamentalFrequency, final double time) {
        if (time < attackInSeconds) {
            //pure square wave
            return Math.signum(Math.sin(2 * Math.PI * fundamentalFrequency * time));
        }
        double result = 0;
        for (final Harmonic harmonic : harmonics) {
            double twoPiF = 2 * Math.PI * harmonic.getFrequencyFromVariation(fundamentalFrequency);
            final int k = harmonic.getRank();
            result += -TWO_ON_PI * (1.0 / k) * sin(k * pluckingRatio * PI) * cos(twoPiF * time);
        }
        return result;
    }

    public void setPluckingRatio(final double valueInPercent) {
        this.pluckingRatio = getPercentage(valueInPercent, MIN_PLUCKING_RATIO, MAX_PLUCKING_RATIO);
        log.info("setting plucking ratio to :" + pluckingRatio);
    }

    public void setOctaveRatio(final double valueInPercent) {
        this.octaveRatio = getPercentage(valueInPercent, MIN_OCTAVE_RATIO, MAX_OCTAVE_RATIO);
        log.info("setting octave ratio to :" + octaveRatio);
    }
}
