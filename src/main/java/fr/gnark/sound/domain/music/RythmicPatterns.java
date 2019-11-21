package fr.gnark.sound.domain.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.singletonList;

public class RythmicPatterns {
    private final List<RythmicPattern> patterns;

    public RythmicPatterns() {
        this.patterns = new ArrayList<>();
    }

    public RythmicPatterns addRythmicPattern(final Subdivision subdivision, final Degree degree) {
        return this.addRythmicPattern(singletonList(subdivision), degree);
    }

    public RythmicPatterns addRythmicPattern(final List<Subdivision> subdivisions, final Degree degree) {
        patterns.add(RythmicPattern.builder()
                .subdivisions(subdivisions).degree(degree).build());
        return this;
    }

    public RythmicPatterns addRythmicPattern(RythmicTemplate rythmicTemplate,
                                             final Degree degree,
                                             final Alterations alterations,
                                             final Integer numberOfNotes,
                                             final PlayStyle playStyle) {
        patterns.add(RythmicPattern.builder()
                .subdivisions(rythmicTemplate.getSubdivisions())
                .degree(degree)
                .alterations(alterations)
                .numberOfNotes(numberOfNotes)
                .playstyle(playStyle)
                .build());
        return this;
    }

    public Iterator<RythmicPattern> getPatternsBySubdivisions() {
        return patterns.iterator();
    }
}
