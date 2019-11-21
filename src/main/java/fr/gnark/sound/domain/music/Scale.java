package fr.gnark.sound.domain.music;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Scale {
    private final Note rootNote;
    @Getter
    private final List<Note> notes;

    public Scale(@NonNull final List<Integer> semitones, @NonNull final Note rootNote) {
        this.rootNote = rootNote;
        this.notes = new ArrayList<>();
        this.notes.add(rootNote);
        for (int i = 0; i < semitones.size(); i++) {
            final int step = semitones.get(i);
            final Note previousNote = this.notes.get(i);
            this.notes.add(previousNote.transpose(step));
        }
    }

    public Scale(@NonNull final Mode mode, @NonNull final Note rootNote) {
        this(mode.getSemitones(), rootNote);
    }
}
