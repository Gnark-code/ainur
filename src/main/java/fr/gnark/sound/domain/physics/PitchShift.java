package fr.gnark.sound.domain.physics;

import lombok.Getter;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static fr.gnark.sound.domain.physics.MathConstants.TWO_PI;


public class PitchShift {
    private final double sampleFrequency;
    private final FastFourierTransformer fastFourierTransformer;
    private final LinearInterpolator linearInterpolator;
    private final int windowSize;
    private final double[] w;

    public PitchShift(final double sampleFrequency, final int windowSize) {
        this.sampleFrequency = sampleFrequency;
        this.fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        this.linearInterpolator = new LinearInterpolator();
        this.windowSize = windowSize;
        this.w = new double[windowSize];
        for (int n = 0; n < windowSize; n++) {
            this.w[n] = hannFunction(n);
        }
    }

    private double hannFunction(final int n) {
        final double windowRatio = (double) n / windowSize;
        return 0.5 * (1 - Math.cos(TWO_PI * windowRatio));
    }

    public double[] shift(final double[] data, final double ratio) {
        final int hopA = windowSize / 2;
        final double hopS = ratio * hopA;
        final double deltaTa = hopA / sampleFrequency;
        final double deltaTs = hopS / sampleFrequency;
        Frames frames = new Frames(windowSize).addWithOverlap(data,  hopA);
        Frames outputFrames = new Frames(windowSize);
        double[] previousFramePhase = new double[windowSize];
        double[] phaseOut = new double[windowSize];
        final Iterator<double[]> iterator = frames.iterator();
        while(iterator.hasNext()){
            final double[] frame = iterator.next();
            final FftResult analysis = analysis(frame);
            final double[] magnitudes = analysis.getMagnitudes();
            final double[] phases = analysis.getPhases();

            int index = 0;
            Complex[] newSignal = new Complex[magnitudes.length];
            while (index < magnitudes.length) {
                final int windowRatio =  index / windowSize;

                final double omegaBin = TWO_PI * sampleFrequency * windowRatio;
                final double deltaPhi = (phases[index] - previousFramePhase[index]) / deltaTa - omegaBin;
                // Remove the expected phase difference
              //  final double deltaPhiPrime = deltaPhi - (hopA * TWO_PI * windowRatio);
                final double deltaPhiMod = ((deltaPhi + Math.PI) % TWO_PI) - Math.PI;
                final double trueFrequency = omegaBin + deltaPhiMod;
                phaseOut[index] = phaseOut[index] + trueFrequency * deltaTs;

                newSignal[index] = new Complex(magnitudes[index]).multiply(Complex.I.multiply(phaseOut[index]*windowRatio).exp());
                index++;
            }

            //Synthesis
            newSignal = fastFourierTransformer.transform(newSignal, TransformType.INVERSE);
            double[] outputFrame = new double[newSignal.length];
            for (int n = 0; n < newSignal.length; n++) {
                outputFrame[n] = newSignal[n].getReal() * w[n];
            }
            outputFrames.add(outputFrame);
        }
        final double[] reconstructedSignal = new OverlapAdder((int) hopA).proceed(outputFrames);
        return resample(reconstructedSignal,ratio);
    }

    private double[] resample(final double[] reconstructedSignal, final double ratio) {
        double[] x = new double[reconstructedSignal.length];
        double[] result = new double[reconstructedSignal.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = i / sampleFrequency;
        }
        PolynomialSplineFunction function = linearInterpolator.interpolate(x, reconstructedSignal);
        for (int i = 0; i < reconstructedSignal.length; i++) {
            final double value = i * ratio / sampleFrequency;
            if (value < x[x.length - 1]) {
                result[i] = function.value(i * ratio / sampleFrequency);
            }
        }
        return result;
    }

    private double[] overlapAdd(final List<double[]> outputFrames, final int windowSize, final double hopS) {
        double[] result = new double[windowSize * outputFrames.size() ];
        int n = 0;
        double[] previousFrame = new double[windowSize];
        for (final double[] frame : outputFrames) {
            for (int j = 0; j < frame.length; j++) {
                if (j < hopS) {
                    result[n] = frame[j] + previousFrame[(int) (windowSize - hopS + j)];
                } else {
                    result[n] = frame[j];
                }
                n++;
            }
            previousFrame = frame;
        }
        return result;
    }


    private List<double[]> getFrames(final double[] data, final double hopA, final int windowSize) {
        List<double[]> frames = new ArrayList<>();
        for (int i = 0; i < data.length; i += (windowSize - hopA)) {
            if (i + windowSize < data.length) {
                final double[] buffer = new double[windowSize];
                System.arraycopy(data, i, buffer, 0, windowSize);
                frames.add(buffer);
            }
        }
        return frames;
    }

    private double stepFunction(final double value) {
        if (value < 0) {
            return 0.0;
        }
        return 1.0;
    }

    private FftResult analysis(final double[] frame) {
        int n = 0;
        double[] result = new double[frame.length];
        for (final double frameSample : frame) {
            result[n] = frameSample * w[n];
            n++;
        }
        return new FftResult(fastFourierTransformer.transform(result, TransformType.FORWARD));
    }


    @Getter
    private class FftResult {
        private final Complex[] analysis;
        private final double[] magnitudes;
        private final double[] phases;

        public FftResult(final Complex[] analysis) {
            this.analysis = analysis;
            magnitudes = new double[analysis.length];
            phases = new double[analysis.length];
            int i = 0;
            for (final Complex value : analysis) {
                magnitudes[i] = getAmplitude(value);
                if (value.getReal() != 0.0) {
                    phases[i] = Math.atan(value.getImaginary() / value.getReal());
                } else {
                    phases[i] = 0;
                }
                i++;
            }
        }
    }

    public double getAmplitude(final Complex value) {
        return Math.sqrt(value.getImaginary() * value.getImaginary() + value.getReal() * value.getReal());
    }

}
