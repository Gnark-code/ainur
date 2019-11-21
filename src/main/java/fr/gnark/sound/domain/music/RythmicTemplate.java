package fr.gnark.sound.domain.music;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static fr.gnark.sound.domain.music.Subdivision.Type.*;
import static java.util.Arrays.asList;

public class RythmicTemplate {

    public static final RythmicTemplate REGGAE_COT_COT
            = new RythmicTemplate(asList(getPause(_8th), getSubdivision(_16Th), getSubdivision(_16Th),
            getPause(_8th), getSubdivision(_16Th), getSubdivision(_16Th)));

    private static Subdivision getSubdivision(final Subdivision.Type type) {
        return Subdivision.builder().type(type).build();

    }

    private static Subdivision getTripletPause(final Subdivision.Type type) {
        return Subdivision.builder().type(type).isPause(true).build();
    }

    private static Subdivision getTriplet(final Subdivision.Type type) {
        return Subdivision.builder().type(type).build();

    }

    private static Subdivision getPause(final Subdivision.Type type) {
        return Subdivision.builder().type(type).isPause(true).build();
    }

    public static final RythmicTemplate THE_PASSENGER
            = new RythmicTemplate(asList(getTripletPause(QUARTER),
            getTriplet(_8th),
            getTriplet(_8th),
            getTripletPause(_8th),
            getTriplet(_8th)
    ));
    @Getter
    private final List<Subdivision> subdivisions;


    private RythmicTemplate(final List<Subdivision> subdivisions) {
        this.subdivisions = subdivisions != null ? subdivisions : new ArrayList<>();
    }
}
