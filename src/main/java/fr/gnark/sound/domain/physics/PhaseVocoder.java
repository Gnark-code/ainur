package fr.gnark.sound.domain.physics;

import lombok.Getter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Iterator;

import static fr.gnark.sound.domain.physics.MathConstants.TWO_PI;

public class PhaseVocoder {
    private final int windowSize;
    private final double[] w;
    private final int hopAnalysis;
    private final int hopSynthesis;
    private final double outputSmoothingFactor;
    private final FastFourierTransformer fastFourierTransformer;

    public PhaseVocoder(final int windowSize, final double ratio) {
        this.windowSize = windowSize;
        hopAnalysis = windowSize / 4;
        hopSynthesis = (int) (hopAnalysis * ratio);

        outputSmoothingFactor = Math.sqrt(this.windowSize / (2.0 * hopSynthesis));
        this.w = new double[windowSize];
        for (int n = 0; n < windowSize; n++) {
            this.w[n] = hannFunction(n);
        }
        this.fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    public double[] proceed(final double[] data) {
        Frames frames = new Frames(windowSize).addWithOverlap(data, hopAnalysis);
        Frames outputFrames = new Frames(windowSize);
        double[] previousFramePhase = new double[windowSize];
        double[] phaseOut = new double[windowSize];
        final Iterator<double[]> iterator = frames.iterator();
        while (iterator.hasNext()) {
            final double[] frame = iterator.next();
            final FftResult analysis = analysis(frame);
            final Complex[] newSignal = processing(analysis, previousFramePhase, phaseOut);
            double[] outputFrame = synthesis(newSignal);
            outputFrames.add(outputFrame);
        }
        return new OverlapAdder((int) hopSynthesis).proceed(outputFrames);
    }


    private double hannFunction(final int n) {
        final double windowRatio = (double) n / windowSize;
        return 0.5 * (1 - Math.cos(TWO_PI * windowRatio));
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


    private Complex[] processing(final FftResult analysis, final double[] previousFramePhase, final double[] phaseOut) {
        final double[] magnitudes = analysis.getMagnitudes();
        final double[] phases = analysis.getPhases();
        final double ratio = (double) hopSynthesis / hopAnalysis;


        int index = 0;
        Complex[] newSignal = new Complex[magnitudes.length];
        while (index < magnitudes.length) {
            final double previousPhaseOut = phaseOut[index];
            final double previousPhaseIn = previousFramePhase[index];
            final double windowRatio = (double) index / windowSize;
            final double omegaBin = TWO_PI * hopAnalysis * windowRatio;
            double deltaPhi = omegaBin + unwrapPhase(phases[index] - previousPhaseIn - omegaBin);

            phaseOut[index] = unwrapPhase(previousPhaseOut + deltaPhi * ratio);
            newSignal[index] = new Complex(magnitudes[index]).multiply(Complex.I.multiply(phaseOut[index]).exp());

            previousFramePhase[index] = phases[index];
            index++;
        }

        return newSignal;
    }


    private double[] synthesis(final Complex[] signal) {
        return getWindowedOutputFrame(fastFourierTransformer.transform(signal, TransformType.INVERSE));
    }

    private double[] getWindowedOutputFrame(final Complex[] newSignal) {
        double[] outputFrame = new double[newSignal.length];
        for (int n = 0; n < newSignal.length; n++) {
            outputFrame[n] = (newSignal[n].getReal() * w[n]) / outputSmoothingFactor;
        }
        return outputFrame;
    }

    private double unwrapPhase(final double phaseIn) {
        return (((phaseIn + Math.PI) % (-2 * Math.PI)) + Math.PI);
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
                phases[i] = value.getArgument();
                i++;
            }
        }
    }

    public double getAmplitude(final Complex value) {
        return Math.sqrt(value.getImaginary() * value.getImaginary() + value.getReal() * value.getReal());
    }
}
