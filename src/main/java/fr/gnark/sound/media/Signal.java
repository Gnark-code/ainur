package fr.gnark.sound.media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Signal {
    private static final double ONE_ON_PI = 1 / Math.PI;
    private static final double TWO_ON_PI = 2 * ONE_ON_PI;
    private static final double FOUR_ON_PI = 4 * ONE_ON_PI;

    private final double twoPiF;

    private final Signal.SIGNAL_TYPE signal_type;
    private final List<Harmonic> harmonics = new ArrayList<>();
    private Double fundamentalFrequency;
    private final List<Double> _buffer;

    public Signal(final Signal.SIGNAL_TYPE signal_type, final double fundamentalFrequency) {
        this.signal_type = signal_type;
        this.fundamentalFrequency = fundamentalFrequency;
        twoPiF = 2 * Math.PI * fundamentalFrequency;
        _buffer = new ArrayList<>((int) (AudioFormatOutput.SAMPLE_RATE / this.fundamentalFrequency));
    }

    public Signal computeBuffer() {
        int maxSize = (int) (AudioFormatOutput.SAMPLE_RATE * 2 / this.fundamentalFrequency);
        for (int i = 0; i < maxSize; i++) {
            _buffer.add(innerComputeFormula(i / AudioFormatOutput.SAMPLE_RATE));
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

    List<Double> getBuffer() {
        return _buffer;
    }

    public double computeFormula(final double time) {
        double result = 0;
        if (_buffer.isEmpty()) {
            result = innerComputeFormula(time);
        } else {
            //get from buffer
            if (time >= 0) {
                double timeInCurrentPeriod = time % (1 / fundamentalFrequency);
                final int index = (int) (timeInCurrentPeriod * AudioFormatOutput.SAMPLE_RATE);
                if (index < _buffer.size()) {
                    result = _buffer.get(index);
                } else //rounding error
                    if (index == _buffer.size()) {
                        result = _buffer.get(index - 1);
                    } else {
                        throw new IllegalArgumentException("Gnark does not know how to code a buffer");

                    }
            } else {
                throw new IllegalArgumentException("This program is not meant to travel backward in time");
            }
        }
        return result;
    }


    double innerComputeFormula(final double time) {
        double result = 0;
        switch (this.signal_type) {
            case SINE:
                result = getSinWithHarmonics(time);
                break;
            case SQUARE:
                result = Math.signum(Math.sin(twoPiF * time));
                break;
            case SQUARE_WITH_SIN_HARMONICS:
                result = Math.signum(getSinWithHarmonics(time));
                break;
            case SQUARE_SYNTHESIS:
                result = FOUR_ON_PI * (Math.sin(twoPiF * time));
                for (final Harmonic harmonic : harmonics) {
                    int k = harmonic.getRank();
                    if (harmonic.isOdd()) {
                        result += (FOUR_ON_PI * (Math.sin(k * twoPiF * time) / k));
                    }
                }
                result = result / 2;
                break;

            case TRIANGLE:
                result = (TWO_ON_PI) * Math.asin(Math.sin(twoPiF * time));
                break;
            case SAWTOOTH:
                double tf = time * fundamentalFrequency;
                result = 2 * (tf - Math.floor(0.5 + tf));
                break;
            case SAWTOOTH_ALT:
                //not the same phase
                result = -(TWO_ON_PI) * Math.atan(1 / Math.tan(Math.PI * fundamentalFrequency * time));
                break;
            case SAWTOOTH_SYNTHESIS:
                result = 0;
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
                break;
        }
        return result;
    }

    private double getSinWithHarmonics(final double time) {
        double temp = twoPiF * time;
        double result = Math.sin(temp);
        for (final Harmonic harmonic : harmonics) {
            result += harmonic.getAmplitude() * Math.sin(temp * harmonic.getRank());
        }
        return result;
    }

    @Override
    public String toString() {
        return "{ signal_type=" + signal_type + ",  number of harmonics=" + harmonics.size() + '}';
    }

    public enum SIGNAL_TYPE {
        SINE,
        SQUARE,
        SQUARE_SYNTHESIS,
        SQUARE_WITH_SIN_HARMONICS,
        TRIANGLE,
        SAWTOOTH,
        SAWTOOTH_SYNTHESIS,
        SAWTOOTH_ALT;
    }
}
