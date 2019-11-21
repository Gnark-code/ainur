package fr.gnark.sound.domain.music;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Gnark on 01/11/2019.
 */
public class Chord {
    private final Mode mode;
    private final Note rootNote;
    @Getter
    private final List<Note> notes;
    @Getter
    private final List<Note> bassNotes;


    @Override
    public int hashCode() {
        return Objects.hash(notes, bassNotes);
    }

    private final BassPattern bassPattern;

    @Builder
    public Chord(final Mode mode, final Note rootNote, final int numberOfNotes, final Degree degree, final BassPattern bassPattern, final Alterations alterations) {
        this.mode = mode;
        this.rootNote = rootNote;
        this.notes = new ArrayList<>();
        this.bassNotes = new ArrayList<>();
        this.bassPattern = bassPattern != null ? bassPattern : BassPattern.NO_BASS;
        computeNotes(mode, rootNote, numberOfNotes, degree);
        computeAlterations(alterations);
    }

    private void computeNotes(final Mode mode, final Note rootNote, final int numberOfNotes, final Degree degree) {
        final List<Note> notesOfTheMode = new ArrayList<>();
        notesOfTheMode.add(rootNote);

        for (int i = 0; i < mode.getSemitones().size() - 1; i++) {
            final int step = mode.getSemitones().get(i);
            final Note previousNote = notesOfTheMode.get(i);
            notesOfTheMode.add(previousNote.transpose(step));
        }

        final Note fundamental = notesOfTheMode.get(degree.ordinal());
        int steps = 2;
        notes.add(fundamental);
        for (int j = numberOfNotes - 1; j > 0; j--) {
            final Note nextThird = lookup(notesOfTheMode, degree.ordinal() + steps);
            steps = steps + 2;
            notes.add(nextThird);
        }
        computeBassNotes(degree, notesOfTheMode);
    }

    private void computeBassNotes(final Degree degree, final List<Note> notesOfTheMode) {
        switch (this.bassPattern) {
            case NO_BASS:
                break;
            case OCTAVE_BASS:
                this.bassNotes.add(lookup(notesOfTheMode, degree.ordinal()).copyWithDecrementedOctave());
                break;
            case DOUBLE_OCTAVE_BASS:
                final Note bassNote = lookup(notesOfTheMode, degree.ordinal()).copyWithDecrementedOctave();
                this.bassNotes.add(bassNote);
                this.bassNotes.add(bassNote.copyWithDecrementedOctave());
                break;
            case POWER_CHORD:
                final Note otherBassNote = lookup(notesOfTheMode, degree.ordinal()).copyWithDecrementedOctave();
                final Note lowerBassNote = otherBassNote.copyWithDecrementedOctave();
                final Note fifth = lowerBassNote.transpose(5);
                this.bassNotes.add(otherBassNote);
                this.bassNotes.add(fifth);
                this.bassNotes.add(lowerBassNote);
                break;

            default: //Do nothing
        }
    }

    private static final Note lookup(final List<Note> notesOfTheMode, final int index) {
        if (index > (notesOfTheMode.size() - 1)) {
            return lookup(notesOfTheMode.stream().map(Note::copyWithIncrementedOctave)
                    .collect(Collectors.toList()), index - notesOfTheMode.size());
        }
        return notesOfTheMode.get(index);
    }


    private void computeAlterations(final Alterations alterations) {
        if (alterations != null) {
            if (alterations.getOctaveModifier() != null) {
                final List<Note> pitches = notes.stream()
                        .map(note -> note.copyWithOctaveModifier(alterations.getOctaveModifier())).collect(Collectors.toList());
                notes.clear();
                notes.addAll(pitches);
            }
            if (alterations.getInversion() != null && alterations.getInversion() < notes.size()) {
                final int inversion = alterations.getInversion();
                final List<Note> pitches = new ArrayList<>();
                for (int j = inversion; j < notes.size(); j++) {
                    pitches.add(notes.get(j));
                }
                for (int i = 0; i < alterations.getInversion(); i++) {
                    pitches.add(notes.get(i).copyWithIncrementedOctave());
                }

                notes.clear();
                notes.addAll(pitches);
            }

            if (alterations.isAddOctave()) {
                notes.add(notes.get(0).copyWithIncrementedOctave());
            }
        }
    }

    public String name() {
        return "";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Chord chord = (Chord) o;
        return Objects.equals(notes, chord.notes) &&
                Objects.equals(bassNotes, chord.bassNotes);
    }

    public enum BassPattern {
        NO_BASS,
        OCTAVE_BASS,
        DOUBLE_OCTAVE_BASS,
        POWER_CHORD
    }
}
