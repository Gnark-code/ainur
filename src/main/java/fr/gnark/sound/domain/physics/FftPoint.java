package fr.gnark.sound.domain.physics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FftPoint {
    private int sampleIndex;
    private double xReal;
    private double xIm;
    private double frequency;
    private double frequencyAmplitude;
    private double phaseInRadians;
}
