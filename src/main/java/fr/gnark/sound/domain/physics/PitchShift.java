package fr.gnark.sound.domain.physics;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;


public class PitchShift {
    private final LinearInterpolator linearInterpolator;
    private final int windowSize;

    public PitchShift(final int windowSize) {
        this.linearInterpolator = new LinearInterpolator();
        this.windowSize = windowSize;
    }

    public double[] shift(final double[] data, final double ratio) {
        final double[] reconstructedSignal = new PhaseVocoder(windowSize, ratio).proceed(data);
        return resample(reconstructedSignal, ratio);
    }

    private double[] resample(final double[] reconstructedSignal, final double ratio) {
        double[] x = new double[reconstructedSignal.length];
        double[] result = new double[(int) (reconstructedSignal.length / ratio)];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        PolynomialSplineFunction function = linearInterpolator.interpolate(x, reconstructedSignal);
        for (int i = 0; i < result.length; i++) {
            double value = i * ratio;
            if (value < x.length) {
                result[i] = function.value(value);
            } else {
                result[i] = function.value(Math.round(value));
            }
        }

        return result;
    }
}
