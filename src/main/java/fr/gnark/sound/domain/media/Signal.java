package fr.gnark.sound.domain.media;

import java.util.*;

public abstract class Signal {
    protected static final double ONE_ON_PI = 1 / Math.PI;
    protected static final double TWO_ON_PI = 2 * ONE_ON_PI;
    protected static final double FOUR_ON_PI = 4 * ONE_ON_PI;
    protected final List<Harmonic> harmonics = new ArrayList<>();
    private final Map<Double, List<Double>> _buffer;

    protected Signal() {
        _buffer = new HashMap<>();
    }

    Map<Double, List<Double>> getBuffer() {
        return _buffer;
    }

    public Signal initBufferIfNecessary(final double fundamentalFrequency) {
        List<Double> bufferedValues = _buffer.get(fundamentalFrequency);
        if (bufferedValues == null) {
            bufferedValues = new ArrayList<>((int) (AudioFormatOutput.SAMPLE_RATE / fundamentalFrequency));
            int maxSize = (int) (AudioFormatOutput.SAMPLE_RATE * 2 / fundamentalFrequency);
            for (int i = 0; i < maxSize; i++) {
                bufferedValues.add(innerComputeFormula(fundamentalFrequency, i / AudioFormatOutput.SAMPLE_RATE));
            }
            _buffer.put(fundamentalFrequency, bufferedValues);
        }
        return this;
    }

    public Signal addHarmonics(final Harmonic harmonic) {
        harmonics.add(harmonic);
        return this;
    }

    public Signal addHarmonics(final int nbHarmonic) {
        for (int i = 0; i < nbHarmonic; i++) {
            harmonics.add(Harmonic.builder().index(i).amplitude(1.0 / nbHarmonic).build());
        }
        return this;
    }

    public List<Harmonic> getHarmonics() {
        return Collections.unmodifiableList(harmonics);
    }


    public double computeFormula(final double fundamentalFrequency, final double time) {
        double result = 0;
        List<Double> bufferedValues = _buffer.get(fundamentalFrequency);
        if (bufferedValues == null) {
            result = innerComputeFormula(fundamentalFrequency, time);
        } else {

            //get from buffer
            if (time >= 0) {
                double timeInCurrentPeriod = time % (1 / fundamentalFrequency);
                final int index = (int) (timeInCurrentPeriod * AudioFormatOutput.SAMPLE_RATE);
                if (index < bufferedValues.size()) {
                    result = bufferedValues.get(index);
                } else //rounding error
                    if (index == bufferedValues.size()) {
                        result = bufferedValues.get(index - 1);
                    } else {
                        throw new IllegalArgumentException("Gnark does not know how to code a buffer");

                    }
            } else {
                throw new IllegalArgumentException("This program is not meant to travel backward in time");
            }
        }
        return result;
    }

    protected abstract double innerComputeFormula(final double fundamentalFrequency, final double time);
}
