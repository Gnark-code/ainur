package fr.gnark.sound.domain.music;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Gnark on 13/07/2019.
 */
public enum Mode {

    IONIAN(asList(2, 2, 1, 2, 2, 2, 1)),
    DORIAN(asList(2, 1, 2, 2, 2, 1, 2)),
    PHRYGIAN(asList(1, 2, 2, 2, 1, 2, 2)),
    LYDIAN(asList(2, 2, 2, 1, 2, 2, 1)),
    MYXOLYDIAN(asList(2, 2, 1, 2, 2, 1, 2)),
    AEOLIAN(asList(2, 1, 2, 2, 1, 2, 2)),
    MINOR_HARMONIC(asList(2, 1, 2, 2, 1, 3, 1)),
    LOCRIAN(asList(1, 2, 2, 1, 2, 2, 2)),
    DOUBLE_HARMONIC_MAJOR(asList(1, 3, 1, 2, 1, 3, 1)),
    DOUBLE_HARMONIC_MINOR(asList(2, 1, 3, 1, 1, 3, 1)),
    PHRYGIAN_DOMINANT(asList(1, 3, 1, 2, 1, 2, 2)),
    UKRAINIAN_DORIAN(asList(2, 1, 3, 1, 2, 1, 2)),
    MINOR(AEOLIAN),
    MAJOR(IONIAN);
    final List<Integer> semitones;

    Mode(final List<Integer> semitones) {
        this.semitones = semitones;
    }

    Mode(final Mode mode) {
        this.semitones = mode.getSemitones();
    }

    public List<Integer> getSemitones() {
        return semitones;
    }
}
