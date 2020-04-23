package fr.gnark.sound.domain.physics;

import lombok.Getter;
import org.apache.commons.math3.complex.Complex;

@Getter
public class FftResult {
    private final Complex[] analysis;
    private final double[] magnitudes;
    private final double[] phases;
    private double max;

    public FftResult(final Complex[] analysis) {
        this.analysis = analysis;
        max = 0;
        magnitudes = new double[analysis.length];
        phases = new double[analysis.length];
        int i = 0;
        for (final Complex value : analysis) {
            magnitudes[i] = getMagnitude(value);
            phases[i] = value.getArgument();
            if (magnitudes[i] > max) {
                max = magnitudes[i];
            }
            i++;
        }
    }

    public double getMagnitude(final Complex value) {
        return Math.sqrt(value.getImaginary() * value.getImaginary() + value.getReal() * value.getReal());
    }
}
