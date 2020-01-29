package fr.gnark.sound.domain.physics;

import lombok.Getter;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Iterator;

import static fr.gnark.sound.domain.physics.MathConstants.TWO_PI;

public class PhaseVocoder {
    private final double sampleFrequency;
    private final int windowSize;
    private final double[] w;
    private final int hopAnalysis;
    private final double hopSynthesis;
    private final double deltaTanalysis;
    private final double deltaTsynthesis;
    private final FastFourierTransformer fastFourierTransformer;

    public PhaseVocoder(final double sampleFrequency, final int windowSize, final double ratio) {
        this.sampleFrequency = sampleFrequency;
        this.windowSize = windowSize;
        hopAnalysis = windowSize / 4;
        hopSynthesis = hopAnalysis * ratio;

        deltaTanalysis = hopAnalysis / this.sampleFrequency;
        deltaTsynthesis = hopAnalysis / this.sampleFrequency;

        this.w = new double[windowSize];
        for (int n = 0; n < windowSize; n++) {
            this.w[n] = hannFunction(n);
        }
        this.fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    public double[] proceed(final double[] data) {
        Frames frames = new Frames(windowSize).addWithOverlap(data, hopAnalysis);
        Frames outputFrames = new Frames(windowSize);
        double[] previousDeltaPhis = new double[windowSize];
        double[] previousFramePhase = new double[windowSize];
        double[] phaseOut = new double[windowSize];
        final Iterator<double[]> iterator = frames.iterator();
        int indexFrame = 0;
        while (iterator.hasNext()) {
            final double[] frame = iterator.next();
            final FftResult analysis = analysis(frame);
    //  final Complex[] newSignal = processing(analysis,previousFramePhase,phaseOut);
            //  final Complex[]     newSignal = analysis.analysis;
            final Complex[] newSignal = processing2(analysis, previousFramePhase, previousDeltaPhis, phaseOut, indexFrame);
            double[] outputFrame = synthesis(newSignal);
            outputFrames.add(outputFrame);
            indexFrame++;
        }
        return new OverlapAdder(hopAnalysis, (int) hopSynthesis).proceed(outputFrames);
    }

    private double[] synthesis(final Complex[] signal) {
        return getWindowedOutputFrame(fastFourierTransformer.transform(signal, TransformType.INVERSE));
    }

    private double[] getWindowedOutputFrame(final Complex[] newSignal) {
        double[] outputFrame = new double[newSignal.length];
        for (int n = 0; n < newSignal.length; n++) {
            outputFrame[n] = newSignal[n].getReal() * w[n];
        }
        return outputFrame;
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

        int index = 0;
        Complex[] newSignal = new Complex[magnitudes.length];
        while (index < magnitudes.length) {
    //        final int windowRatio = index / windowSize;
            final double windowRatio =(double) index / windowSize;
            final double ratio = hopSynthesis / hopAnalysis;
            final double omegaBin = TWO_PI * sampleFrequency * windowRatio;
            final double deltaPhi = ((phases[index] - previousFramePhase[index]) / deltaTanalysis) - omegaBin;
            previousFramePhase[index] = phases[index];
            // Remove the expected phase difference
           final double deltaPhiPrime = deltaPhi - omegaBin;
            final double deltaPhiMod = ((deltaPhiPrime + Math.PI) % TWO_PI) - Math.PI;
            final double trueFrequency = omegaBin + deltaPhiMod;
            phaseOut[index] = phaseOut[index] + trueFrequency *deltaTsynthesis;
            previousFramePhase[index] = phases[index];


     /*           final double deltaPhi  = phases[index]  - previousFramePhase[index];
                previousFramePhase[index] = phases[index];
                final double deltaPhiPrime          = deltaPhi- hopA * 2*Math.PI*windowRatio;
                final double deltaPhiPrimeMod         = ((deltaPhiPrime + Math.PI) % TWO_PI) - Math.PI;
                final double trueFrequency          = 2*Math.PI*windowRatio+ deltaPhiPrimeMod/hopA;
                phaseOut[index] = phaseOut[index] + hopS * trueFrequency;
        */




          //  newSignal[index] = new Complex(magnitudes[index]).multiply(Complex.I.multiply(phaseOut[index] * windowRatio).exp());

          /*      final double deltaPhi  = phases[index]  - previousFramePhase[index];
                previousFramePhase[index] = phases[index];
                final double deltaPhiPrime          = deltaPhi- hopAnalysis * 2*Math.PI*windowRatio;
                final double deltaPhiPrimeMod         = ((deltaPhiPrime + Math.PI) % TWO_PI) - Math.PI;
                final double trueFrequency          = 2*Math.PI*windowRatio+ deltaPhiPrimeMod/hopAnalysis;
                phaseOut[index] = phaseOut[index] + hopSynthesis* trueFrequency; */
                 newSignal[index] =  new Complex(magnitudes[index] * Math.cos(phaseOut[index]), magnitudes[index] * Math.sin(phaseOut[index]));
            index++;
        }
        return newSignal;
    }


    private Complex[] processing2(final FftResult analysis, final double[] previousFramePhase, final double[] previousDeltaPhis, final double[] phaseOut, final int indexFrame) {
        final double[] magnitudes = analysis.getMagnitudes();
        final double[] phases = analysis.getPhases();
        final double ratio = hopSynthesis / hopAnalysis;


        int index = 0;
        Complex[] newSignal = new Complex[magnitudes.length];
        while (index < magnitudes.length) {
            final double previousPhaseOut = phaseOut[index];
            final double previousPhaseIn = previousFramePhase[index];
            final double previousDeltaPhi = previousDeltaPhis[index];

            final double windowRatio = (double) index / windowSize;
            final double omegaBin = TWO_PI * hopAnalysis * windowRatio;
            double deltaPhi = omegaBin + unwrapPhase2(phases[index] - previousPhaseIn - omegaBin);
            if (indexFrame == 0) {
                phaseOut[index] = phases[index];
            } else {
                phaseOut[index] = unwrapPhase2(previousPhaseOut + previousDeltaPhi * ratio);
            }
            //newSignal[index] = new Complex(magnitudes[index] * Math.cos(phaseOut[index]), magnitudes[index] * Math.sin(phaseOut[index]));
            //newSignal[index] = analysis.analysis[index];
            newSignal[index] = new Complex(magnitudes[index]).multiply(Complex.I.multiply((omegaBin * (index / sampleFrequency)) + phaseOut[index]).exp());
            previousDeltaPhis[index] = deltaPhi;
            previousFramePhase[index] = phases[index];
            index++;
        }
        return newSignal;
    }



    private Complex[] processing3(final FftResult analysis,final double[] phaseOut) {
        final double[] magnitudes = analysis.getMagnitudes();
        final double[] phases = analysis.getPhases();
        final double ratio = hopSynthesis / hopAnalysis;


        int index = 0;
        Complex[] newSignal = new Complex[magnitudes.length];
        while (index < magnitudes.length) {
            phaseOut[index] = phases[index] *ratio;
            newSignal[index] = new Complex(magnitudes[index] * Math.cos(phaseOut[index]), magnitudes[index] * Math.sin(phaseOut[index]));
            index++;
        }
        return newSignal;
    }

    private double unwrapPhase2(final double phaseIn) {
        return ((phaseIn + Math.PI) % TWO_PI) - Math.PI;
    }

    private double unwrapPhase(final double phaseIn) {
        double a = phaseIn / TWO_PI;
        long k = Math.round(a);
        return phaseIn - k * TWO_PI;
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
