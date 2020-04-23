package fr.gnark.sound.domain.physics;

import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

/**
 * analyze a signal to determine the peak of frequencies using FFT
 */
public class PeakFinder {
    private final FastFourierTransformer fastFourierTransformer;
    private final double sampleRate;
    private final float thresholdInPercent;

    public PeakFinder(final double sampleRate) {
        this(sampleRate, 0.1f);
    }

    public PeakFinder(final double sampleRate, final float thresholdInPercent) {
        this.sampleRate = sampleRate;
        this.thresholdInPercent = thresholdInPercent;
        fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    public List<Peak> proceed(final double[] data) {
        final List<Peak> peaks = new ArrayList<>();
        final FftResult fftResult = new FftResult(fastFourierTransformer.transform(data, TransformType.FORWARD));
        final double frequencyStep = sampleRate / fftResult.getMagnitudes().length;
        int i = 0;
        for (final double magnitude : fftResult.getMagnitudes()) {
            if (fftResult.getMax() * thresholdInPercent < magnitude) {
                peaks.add(new Peak(frequencyStep * i, magnitude));
            }
            i++;
        }
        return peaks;
    }


}
