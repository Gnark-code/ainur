package fr.gnark.sound.domain.media.waveforms;

import fr.gnark.sound.domain.media.Harmonic;
import fr.gnark.sound.domain.media.Signal;
import lombok.extern.slf4j.Slf4j;

import static java.lang.Math.*;

/**
 * Equations courtesy of Vogel Scheer
 * https://www.vogel-scheer.de/images/pdf/Infos/Saiten-Scholl-StringingTheHarpsichord-en.pdf
 */
@Slf4j
public class Harpsichord extends Signal {
    private static final double MAX_PLUCKING_RATIO = 0.5;
    private static final double MIN_PLUCKING_RATIO = 0.01;
    private double pluckingRatio = 0.3;

    public Harpsichord() {
        this.addOvertones(31);
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
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


}
