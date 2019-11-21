package fr.gnark.sound.domain.music;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Alterations {
    private Integer octaveModifier;
    private Integer inversion;
    private boolean addOctave;

    public Alterations(final Integer octaveModifier, final Integer inversion, final boolean addOctave) {
        this.octaveModifier = octaveModifier;
        this.inversion = inversion;
        this.addOctave = addOctave;
    }

    public static Alterations emptyAlterations() {
        return Alterations.builder().build();
    }
}
