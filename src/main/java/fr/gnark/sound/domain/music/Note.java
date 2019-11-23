package fr.gnark.sound.domain.music;

import fr.gnark.sound.domain.DomainObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
    // from 0 to 8
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
        if (octave < 0 || octave > 8) {
            throw new IllegalArgumentException("octave should be set between 0 and 8");
        }
    }

}
