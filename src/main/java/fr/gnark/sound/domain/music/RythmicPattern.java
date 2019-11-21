package fr.gnark.sound.domain.music;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.gnark.sound.domain.music.Alterations.emptyAlterations;

@Builder
public class RythmicPattern {
    private final List<Subdivision> subdivisions;
    private final Degree degree;
    private final Integer numberOfNotes;
    private final Alterations alterations;
    private final PlayStyle playstyle;

    private RythmicPattern(final List<Subdivision> subdivisions, final Degree degree, final Integer numberOfNotes, final Alterations alterations, final PlayStyle playstyle) {
        this.subdivisions = subdivisions != null ? subdivisions : new ArrayList<>();
        this.degree = degree;
        this.numberOfNotes = numberOfNotes != null ? numberOfNotes : 3;
        this.alterations = alterations != null ? alterations : emptyAlterations();
        this.playstyle = playstyle != null ? playstyle : PlayStyle.UNISON;
    }

    public List<Subdivision> getSubdivisions() {
        return Collections.unmodifiableList(subdivisions);
    }

    public Degree getDegree() {
        return degree;
    }

    public Integer getNumberOfNotes() {
        return numberOfNotes;
    }

    public Alterations getAlterations() {
        return alterations;
    }
}
