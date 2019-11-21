package fr.gnark.sound.domain.music;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayableChord {
    private final Chord chord;
    private final PlayStyle playstyle;
    private final Subdivision duration;

    @Builder
    public PlayableChord(final Chord chord,
                         final PlayStyle playstyle, final Subdivision duration) {
        this.chord = chord;
        this.playstyle = playstyle;
        this.duration = duration;
    }

    public List<Note> getNotes() {
        return chord.getNotes();
    }

    public List<Note> getBassNotes() {
        return chord.getBassNotes();
    }

}
