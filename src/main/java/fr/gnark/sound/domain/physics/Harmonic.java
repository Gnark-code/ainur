package fr.gnark.sound.domain.physics;

import fr.gnark.sound.domain.DomainObject;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by Gnark on 01/05/2019.
 */

@Getter
public class Harmonic extends DomainObject {
    private int index;
    private Double amplitude;
    private Double phase;
    private Double variationFromFundamentalInCents;

    @Builder
    public Harmonic(final int index, final Double amplitude, final Double phase,
                    final Double variationFromFundamentalInCents) {
        this.index = index;
        this.amplitude = amplitude != null ? amplitude : 1;
        this.phase = phase != null ? phase : 0;
        this.variationFromFundamentalInCents = variationFromFundamentalInCents != null ? variationFromFundamentalInCents : 0;
    }

    public boolean isOdd() {
        return getRank() % 2 != 0;
    }

    public int getRank() {
        return index + 1;
    }

    public double getFrequencyFromVariation(final double fundamental) {
        return fundamental * Math.pow(2, variationFromFundamentalInCents / 1200);
    }

    public double getPhaseInRadians() {
        return phase * (Math.PI / 180);
    }

    public static double convertToDegrees(final double phaseInRadians){
        return phaseInRadians*(180/Math.PI);
    }
}
