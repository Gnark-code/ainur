package fr.gnark.sound.domain.media;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by Gnark on 01/05/2019.
 */

@Getter
public class Harmonic {
    private int index;
    private Double amplitude;
    private Double phase;

    @Builder
    public Harmonic(final int index, final Double amplitude, final Double phase) {
        this.index = index;
        this.amplitude = amplitude != null ? amplitude : 1;
        this.phase = phase != null ? phase : 0;
    }

    public boolean isOdd() {
        return getRank() % 2 != 0;
    }

    public int getRank() {
        return index + 1;
    }
}
