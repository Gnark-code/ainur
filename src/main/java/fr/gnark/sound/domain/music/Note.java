package fr.gnark.sound.domain.music;

import fr.gnark.sound.domain.DomainObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Iterator;
import java.util.Optional;

/**
 * Created by Gnark on 17/06/2019.
 */
@Builder
@EqualsAndHashCode
@Getter
@ToString
public class Note extends DomainObject {
    private static final short NB_STEPS = 12;
    private final BaseNote baseNote;
    // from 0 to 10
    private int octave;

    public Note(BaseNote baseNote, int octave) {
        this.baseNote = baseNote;
        setOctave(octave);
    }


    public Note transpose(final int step) {
        if (step != 0) {
            final int position = BaseNote.getIndexOfNote(baseNote);
            int index = position + step;
            int octaveValue = this.octave;
            if (index > NB_STEPS - 1) {
                final int octaveToAdd = index / NB_STEPS;
                octaveValue = this.octave + octaveToAdd;
                index = index - (octaveToAdd * NB_STEPS);
            } else if (index < 0) {
                final int octaveToRemove = 1 - index / NB_STEPS;
                octaveValue = this.octave - octaveToRemove;
                index = octaveToRemove * NB_STEPS + index;
            }
            return Note.builder()
                    .octave(octaveValue)
                    .baseNote(BaseNote.getByIndexInTheScale(index))
                    .build();
        }
        return this;
    }

    public Double convertToFrequency() {
        return this.baseNote.getLowestFrequency() * Math.pow(2, octave + 1.0);
    }

    public Note copyWithOctaveModifier(final Integer octaveModifier) {
        return Note.builder().baseNote(baseNote).octave(octave + octaveModifier).build();
    }

    public Note copyWithIncrementedOctave() {
        return Note.builder().baseNote(baseNote).octave(octave + 1).build();
    }

    public Note copyWithDecrementedOctave() {
        return Note.builder().baseNote(baseNote).octave(octave - 1).build();
    }

    private void setOctave(int octave) {
        assertOctave(octave);
        this.octave = octave;
    }

    private void assertOctave(int octave) {
        if (octave < 0 || octave > 10) {
            throw new IllegalArgumentException("octave should be set between 0 and 8. value read:" + octave);
        }
    }

    public static Optional<Note> getFromFrequency(final double frequency, final double toleranceInCents) {
        return getFromFrequency(frequency, toleranceInCents, 0.0);
    }

    public static Optional<Note> getFromFrequency(final double frequency, final double toleranceInCents, final double biasInCents) {
        final double minFrequency = frequency * Math.pow(2, -toleranceInCents / 1200.0);
        final double maxFrequency = frequency * Math.pow(2, toleranceInCents / 1200.0);
        for (int octaveIndex = 0; octaveIndex < 10; octaveIndex++) {
            final Iterator<BaseNote> baseNoteIterator = BaseNote.iterator();
            while (baseNoteIterator.hasNext()) {
                final Note note = Note.builder().baseNote(baseNoteIterator.next()).octave(octaveIndex).build();
                final double freq = note.convertToFrequency() * Math.pow(2, biasInCents / 1200.0);
                if (freq > minFrequency && freq < maxFrequency) {
                    return Optional.of(note);
                }
            }
        }
        return Optional.empty();
    }

}
