package fr.gnark.sound.domain.physics;

import fr.gnark.sound.domain.media.output.WavConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

import static fr.gnark.sound.domain.media.output.WavConstants.GAIN;

public class SpectralAnalysis {
    private final List<FftPoint> _points = new ArrayList<>();


    public SpectralAnalysis performAnalysis(final double[] dataFromSample, final int numberOfSamples) {
        FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] transformed = fastFourierTransformer.transform(dataFromSample, TransformType.FORWARD);

        final double frequencyResolution = WavConstants.SAMPLE_RATE / numberOfSamples;

        int i = 0;
        for (final Complex complex : transformed) {
            final double frequency = i * frequencyResolution;
            final double frequencyAmplitude = Math.abs(Math.sqrt(complex.getReal() * complex.getReal() + complex.getImaginary() * complex.getImaginary()));
            double phaseInRadians = 0;
            if (complex.getImaginary() < (GAIN / 1000)) {
                phaseInRadians = Math.atan(complex.getImaginary() / complex.getReal());
            }
            _points.add(new FftPoint(i, complex.getReal(), complex.getImaginary(), frequency, frequencyAmplitude, phaseInRadians));

            i++;
        }
        return this;
    }


    public String extractEquation(final double fundamentalFrequency) {
        StringBuilder builder = new StringBuilder("final double n = fundamentalFrequency*time;\n");
        builder.append("double result=");
        for (final FftPoint fftPoint : _points) {
            if (fftPoint.getFrequencyAmplitude() > GAIN / 10 ) {
                final double factor = fftPoint.getFrequency() / fundamentalFrequency;
                final String realPart = fftPoint.getXReal() + "*Math.cos( ((2*Math.PI* " + fftPoint.getSampleIndex() + " * + " + factor + "*n)/" + _points.size() + " )+"+ fftPoint.getPhaseInRadians()+" ) ";
                final String imaginaryPart = fftPoint.getXIm() + "*Math.sin( ((2*Math.PI* " + fftPoint.getSampleIndex() + " * + " + factor + "* n)/" + _points.size() + " )+"+ fftPoint.getPhaseInRadians()+" ) ";
                builder.append(realPart).append(" - ").append(imaginaryPart).append("\n + ");
            }
        }

        String result = StringUtils.substringBeforeLast(builder.toString(), " + ");
        result += "; \n";
        result += " result= result/" + GAIN;
        return result;
    }

    public String extractEquation2(final double fundamentalFrequency) {
        StringBuilder builder = new StringBuilder("final double n = fundamentalFrequency*time;\n");
        builder.append("double result=");
        for (final FftPoint fftPoint : _points) {
            if (fftPoint.getFrequencyAmplitude() > GAIN / 10 ) {
                final double factor = fftPoint.getFrequency() / fundamentalFrequency;
                final String realPart = fftPoint.getXReal() + "*Math.cos( (2*Math.PI* " + fftPoint.getSampleIndex() + " * + " + fftPoint.getFrequency() + "*time)/" + _points.size() + " ) ";
                final String imaginaryPart = fftPoint.getXIm() + "*Math.sin( (2*Math.PI* " + fftPoint.getSampleIndex() + " * + " + fftPoint.getFrequency() + "* time)/" + _points.size() + ")";
                builder.append(realPart).append(" - ").append(imaginaryPart).append("\n + ");
            }
        }

        String result = StringUtils.substringBeforeLast(builder.toString(), " + ");
        result += "; \n";
        result += " result= result/" + GAIN;
        return result;
    }

}
