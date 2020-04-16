package fr.gnark.sound.domain.physics;

import fr.gnark.sound.domain.DomainObject;

import java.util.*;

import static fr.gnark.sound.domain.media.output.WavConstants.SAMPLE_RATE;

public abstract class Signal extends DomainObject {

    protected final List<Double> overtonesCents;
    protected final List<Harmonic> harmonics;
    protected final Map<Double, double[]> _buffer;

    protected Signal() {
        _buffer = new HashMap<>();
        overtonesCents = List.of(0.0, 1200.0, 1902.0, 2400.0, 2786.3, 3102.0, 3368.8, 3600.0, 3803.9, 3986.3, 4151.3, 4302.0, 4440.5, 4568.8, 4688.3, 4800.0, 4905.0, 5004.0, 5098.0, 5186.0, 5271.0, 5351.0, 5428.0, 5502.0, 5573.0, 5641.0, 5706.0, 5669.0, 5730.0, 5788.0, 5845.0);
        harmonics = new ArrayList<>();
    }

    Map<Double, double[]> getBuffer() {
        return _buffer;
    }


    public Signal addHarmonics(final Harmonic harmonic) {
        harmonics.add(harmonic);
        return this;
    }

    public Signal addHarmonics(final int nbHarmonic) {
        for (int i = 0; i < nbHarmonic; i++) {
            harmonics.add(Harmonic.builder().index(i)
                    .amplitude(1.0 / nbHarmonic).build());
        }
        return this;
    }

    public Signal addOvertones(final int nbHarmonic) {
        checkRange("number of harmonics", (double) nbHarmonic, 0.0, (double) overtonesCents.size());
        for (int i = 0; i < nbHarmonic; i++) {
            harmonics.add(Harmonic.builder().index(i)
                    .variationFromFundamentalInCents(overtonesCents.get(i))
                    .amplitude(1.0 / nbHarmonic).build());
        }
        return this;
    }


    public List<Harmonic> getHarmonics() {
        return Collections.unmodifiableList(harmonics);
    }


    public double computeFormula(final double fundamentalFrequency, final double time) {
        double result;
        double[] bufferedValues = _buffer.get(fundamentalFrequency);
        if (bufferedValues == null) {
            result = innerComputeFormula(fundamentalFrequency, time);
        } else {
            result = getResultFromBuffer(fundamentalFrequency, time, bufferedValues);
        }
        return result;
    }

    public Signal initBufferIfNecessary(final double fundamentalFrequency) {
        double[] bufferedValues = _buffer.get(fundamentalFrequency);
        if (bufferedValues == null) {
            bufferedValues = new double[(int) (SAMPLE_RATE / fundamentalFrequency)];
            int maxSize = (int) (SAMPLE_RATE * 2 / fundamentalFrequency);
            for (int i = 0; i < maxSize; i++) {
                bufferedValues[i] = innerComputeFormula(fundamentalFrequency, i / SAMPLE_RATE);
            }
            _buffer.put(fundamentalFrequency, bufferedValues);
        }
        return this;
    }

    protected double getResultFromBuffer(final double fundamentalFrequency, final double time, final double[] bufferedValues) {
        final double result;//get from buffer
        if (time >= 0) {
            double timeInCurrentPeriod = time % (1 / fundamentalFrequency);
            final int index = (int) (timeInCurrentPeriod * SAMPLE_RATE);
            if (index < bufferedValues.length) {
                result = bufferedValues[index];
            } else //rounding error
                if (index == bufferedValues.length) {
                    result = bufferedValues[index - 1];
                } else {
                    throw new IllegalArgumentException("Gnark does not know how to code a buffer");
                }
        } else {
            throw new IllegalArgumentException("This program is not meant to travel backward in time");
        }
        return result;
    }

    protected abstract double innerComputeFormula(final double fundamentalFrequency, final double time);
}
