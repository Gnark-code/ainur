package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Math.sin;

/**
 * Bizarre flute created accidentaly
 */
public class Warp extends Signal {

    final Random secureRandom = new SecureRandom();

    private double airblowFactor = 0.003;
    private double vibratoVariationsInCents = 25.0;

    public Warp() {
        final double totalAmplitude = 1522.4 + 611.3 + 412.6 + 195.7 + 66.1;
        this.addHarmonics(
                Harmonic.builder()
                        .phase(-110.94)
                        .amplitude(1522.4 / totalAmplitude)
                        .variationFromFundamentalInCents(overtonesCents.get(0))
                        .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phase(122.091)
                                .amplitude(611.3 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(1))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phase(-142.0)
                                .amplitude(412.6 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(2))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phase(-160.32)
                                .amplitude(195.7 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(3))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phase(-17.5336)
                                .amplitude(66.1 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(4))
                                .build())
        ;
    }

    @Override
    protected double innerComputeFormula(final double fundamentalFrequency, final double time) {
        final double frequency = vibrato(fundamentalFrequency, time);
        double result = 0;
        for (final Harmonic harmonic : harmonics) {
            double twoPiF = 2 * Math.PI * harmonic.getFrequencyFromVariation(frequency);
            result += harmonic.getAmplitude() * sin(twoPiF * time + harmonic.getPhaseInRadians());
        }

        return (1 - airblowFactor) * result + airblow();
    }

    private double vibrato(final double fundamentalFrequency, final double time) {
        return fundamentalFrequency + sin(2 * Math.PI * 90.0 * time) * Math.pow(2, vibratoVariationsInCents / 1200);
    }

    private double airblow() {
        //white noise generation
        return airblowFactor * secureRandom.nextGaussian();
    }
}
