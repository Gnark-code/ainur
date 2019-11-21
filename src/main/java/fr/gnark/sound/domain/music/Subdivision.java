package fr.gnark.sound.domain.music;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Subdivision {
    public enum Type {
        WHOLE,
        HALF,
        QUARTER,
        _8th,
        _16Th,
        _32Th,
        _64Th;
    }

    private final Type type;
    private final boolean isPause;
    private boolean isTriplet;
    private boolean isDotted;

    @Builder
    public Subdivision(final Type type, final boolean isPause, final boolean isTriplet, final boolean isDotted) {
        this.type = type;
        this.isPause = isPause;
        this.isTriplet = isTriplet;
        this.isDotted = isDotted;
    }
}
