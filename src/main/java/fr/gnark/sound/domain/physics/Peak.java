package fr.gnark.sound.domain.physics;

import lombok.Getter;

@Getter
public class Peak {
    private double frequency;
    private double magnitude;

    public Peak(final double frequency, final double magnitude) {
        this.frequency = frequency;
        this.magnitude = magnitude;
    }
}
