package fr.gnark.sound.domain.physics;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FftPoint {
    private int sampleIndex;
    private double xReal;
    private double xIm;
    private double frequency;
    private double amplitude;
    private double phaseInRadians;
}
