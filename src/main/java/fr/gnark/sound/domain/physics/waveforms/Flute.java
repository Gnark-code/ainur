package fr.gnark.sound.domain.physics.waveforms;

import fr.gnark.sound.domain.physics.Harmonic;
import fr.gnark.sound.domain.physics.Signal;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Random;

import static java.lang.Math.sin;

@Slf4j
public class Flute extends Signal {

    private static final double MIN_VIBRATO_IN_CENTS = 0.0;
    private static final double MAX_VIBRATO_IN_CENTS = 100.0;
    private static final double MIN_AIRBLOW = 0.0;
    private static final double MAX_AIRBLOW = 0.1;
    final Random secureRandom = new SecureRandom();

    private double airblowFactor = 0.003;
    private double vibratoInCents = 10.0;

    public Flute() {
        final double totalAmplitude = 1522.4 + 611.3 + 412.6 + 195.7 + 66.1;
        this.addHarmonics(
                Harmonic.builder()
                        .phaseInDegrees(-110.94)
                        .amplitude(1522.4 / totalAmplitude)
                        .variationFromFundamentalInCents(overtonesCents.get(0))
                        .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phaseInDegrees(122.091)
                                .amplitude(611.3 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(1))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phaseInDegrees(-142.0)
                                .amplitude(412.6 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(2))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phaseInDegrees(-160.32)
                                .amplitude(195.7 / totalAmplitude)
                                .variationFromFundamentalInCents(overtonesCents.get(3))
                                .build())
                .addHarmonics(
                        Harmonic.builder()
                                .phaseInDegrees(-17.5336)
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
        final double deltaInHertz = fundamentalFrequency * Math.pow(2, vibratoInCents / 1200) - fundamentalFrequency;
        return fundamentalFrequency + deltaInHertz * Math.signum(Math.sin(2 * Math.PI * deltaInHertz * time));
    }

    private double airblow() {
        //white noise generation
        return airblowFactor * secureRandom.nextGaussian();
    }

    public void setVibratoInCents(final Double valueInPercent) {
        this.vibratoInCents = getPercentage(valueInPercent, MIN_VIBRATO_IN_CENTS, MAX_VIBRATO_IN_CENTS);
        log.info("setting vibrato in cents to :" + vibratoInCents);
    }

    public void setAirblow(final Double valueInPercent) {
        this.airblowFactor = getPercentage(valueInPercent, MIN_AIRBLOW, MAX_AIRBLOW);
        log.info("setting airblow to :" + airblowFactor);
    }
}
