package fr.gnark.sound.domain.music;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Gnark on 13/07/2019.
 */
@Builder
@Getter
public class ChordProgression {
    private final Mode mode;
    private final Note rootNote;
    private final RythmicPatterns rythmicPatterns;

    public ChordProgression(final Mode mode, final Note rootNote, final RythmicPatterns rythmicPatterns) {
        this.mode = mode;
        this.rootNote = rootNote;
        this.rythmicPatterns = rythmicPatterns;
    }

    public List<PlayableChord> generateChords() {
        final List<PlayableChord> result = new ArrayList<>();
        Iterator<RythmicPattern> iterator = rythmicPatterns.getPatternsBySubdivisions();
        while (iterator.hasNext()) {
            final RythmicPattern currentItem = iterator.next();
            final Chord chord = Chord.builder()
                    .mode(mode)
                    .rootNote(rootNote)
                    .numberOfNotes(currentItem.getNumberOfNotes())
                    .degree(currentItem.getDegree())
                    .bassPattern(Chord.BassPattern.NO_BASS)
                    .alterations(currentItem.getAlterations())
                    .build();
            for (Subdivision subdivision : currentItem.getSubdivisions()) {
                final PlayableChord playableChord = PlayableChord.builder().chord(chord)
                        .duration(subdivision)
                        .playstyle(PlayStyle.UNISON)
                        .build();
                result.add(playableChord);
            }
        }

        return result;
    }

}
