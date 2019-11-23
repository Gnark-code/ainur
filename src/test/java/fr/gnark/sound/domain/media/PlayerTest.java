package fr.gnark.sound.domain.media;

import fr.gnark.sound.adapter.ChordProgressionToEvents;
import fr.gnark.sound.adapter.ScaleToEvents;
import fr.gnark.sound.domain.media.waveforms.SawtoothWaveWithSynthesis;
import fr.gnark.sound.domain.music.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import java.util.Arrays;
import java.util.List;

import static fr.gnark.sound.domain.music.Alterations.emptyAlterations;
import static java.util.Arrays.asList;

/**
 * Created by Gnark on 13/07/2019.
 * <p>
 * Class used to debug event processing
 */
@Disabled("Needs a computer with sound card to run")
public class PlayerTest {
    private static final int TICKS_BY_WHOLE_NOTE = 128;
    private static final ChordProgressionToEvents CHORD_PROGRESSION_TO_EVENTS = new ChordProgressionToEvents(TICKS_BY_WHOLE_NOTE);
    private static int BPM = 135;

    private final Player player;


    public PlayerTest() throws LineUnavailableException {
        final double definitionInMs = 60000.0 / (BPM * (TICKS_BY_WHOLE_NOTE / 4.0));
        Encoder encoder = new Encoder("SAWTOOTH", definitionInMs, new AudioFormatOutput(),
                new SawtoothWaveWithSynthesis().addHarmonics(5)
        );

        player = new Player(encoder);
    }

    @Test
    public void test() {
        final RythmicPatterns rythmicPatterns = new RythmicPatterns();
        Subdivision subdivision = Subdivision.builder().type(Subdivision.Type.QUARTER).build();
        rythmicPatterns.addRythmicPattern(Arrays.asList(subdivision, subdivision, subdivision, subdivision), Degree.I)
                .addRythmicPattern(subdivision, Degree.I)
                .addRythmicPattern(subdivision, Degree.VI)
                .addRythmicPattern(subdivision, Degree.V)
                .addRythmicPattern(subdivision, Degree.IV)
        ;

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.MAJOR)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.E_FLAT)
                        .build())
                .bassPattern(Chord.BassPattern.OCTAVE_BASS)
                .rythmicPatterns(rythmicPatterns)
                .build();

        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);

        player.play();
    }

    @Test
    public void testNoWomanNoCry() throws Exception {
        final RythmicPatterns rythmicPatterns = new RythmicPatterns()
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.I, Alterations.builder().addOctave(true).octaveModifier(1).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.V, Alterations.builder().addOctave(true).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.VI, Alterations.builder().addOctave(true).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.IV, Alterations.builder().addOctave(true).build(), null, null);

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.MAJOR)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.C).build())
                .rythmicPatterns(rythmicPatterns)
                .bassPattern(Chord.BassPattern.DOUBLE_OCTAVE_BASS)
                .build();


        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);
        player.repeat(2);
    }


    @Test
    public void testNoWomanNoCryWithInvertedChords() throws Exception {
        final RythmicPatterns rythmicPatterns = new RythmicPatterns()
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.I, Alterations.builder().octaveModifier(1).addOctave(true).inversion(1).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.V, Alterations.builder().inversion(1).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.VI, Alterations.builder().inversion(2).build(), null, null)
                .addRythmicPattern(RythmicTemplate.REGGAE_COT_COT, Degree.IV, Alterations.builder().inversion(3).build(), 4, null);

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.MAJOR)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.C).build())
                .rythmicPatterns(rythmicPatterns)
                .build();


        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);

        player.play();
    }

    @Test
    public void thePassenger() {
        final RythmicPatterns rythmicPatterns = new RythmicPatterns()
                .addRythmicPattern(RythmicTemplate.THE_PASSENGER, Degree.V, Alterations.builder().addOctave(true).build(), null, null)
                .addRythmicPattern(RythmicTemplate.THE_PASSENGER, Degree.VI, Alterations.builder().addOctave(true).build(), null, null)
                .addRythmicPattern(RythmicTemplate.THE_PASSENGER, Degree.I, Alterations.builder().octaveModifier(1).addOctave(true).build(), null, null)
                .addRythmicPattern(RythmicTemplate.THE_PASSENGER, Degree.IV, Alterations.builder().addOctave(true).build(), null, null);

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.MAJOR)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.C).build())
                .rythmicPatterns(rythmicPatterns)
                .build();


        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);
        player.repeat(2);
    }

    @Test
    public void testScale() {
        final Note note = Note.builder().octave(1).baseNote(BaseNote.B_FLAT).build();
        //index in the resolution
        ScaleToEvents scaleToEvents = new ScaleToEvents(TICKS_BY_WHOLE_NOTE / 16, 4);
        scaleToEvents.map(new Scale(Mode.UKRAINIAN_DORIAN, note))
                .forEach(player::postEvents);
        scaleToEvents.map(new Scale(Mode.PHRYGIAN_DOMINANT, note))
                .forEach(player::postEvents);
        scaleToEvents.map(new Scale(Mode.DOUBLE_HARMONIC_MINOR, note))
                .forEach(player::postEvents);
        player.play();
    }

    @Test
    public void testBass() {
        final List<Subdivision> wholeNote = Arrays.asList(Subdivision.builder().type(Subdivision.Type.HALF).build());
        final RythmicPatterns rythmicPatterns = new RythmicPatterns()
                .addRythmicPattern(wholeNote, Degree.I, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.VI, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.II, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.IV, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.III, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.VII, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.V, emptyAlterations(), 5, PlayStyle.ARPEGGIO)
                .addRythmicPattern(wholeNote, Degree.II, emptyAlterations(), 5, PlayStyle.ARPEGGIO);

        final ChordProgression chordProgression = ChordProgression.builder()
                .mode(Mode.DORIAN)
                .rootNote(Note.builder().octave(2)
                        .baseNote(BaseNote.D_FLAT)
                        .build())
                .bassPattern(Chord.BassPattern.POWER_CHORD)
                .rythmicPatterns(rythmicPatterns)
                .build();


        //index in the resolution
        CHORD_PROGRESSION_TO_EVENTS.map(chordProgression)
                .forEach(player::postEvents);
        player.play();
    }

    @Test
    public void prelude() {
        final Note C = Note.builder().octave(2).baseNote(BaseNote.C).build();
        final Note A = Note.builder().octave(1).baseNote(BaseNote.A).build();
        final Note B = Note.builder().octave(1).baseNote(BaseNote.B).build();
        //index in the resolution
        ScaleToEvents scaleToEvents = new ScaleToEvents(TICKS_BY_WHOLE_NOTE / 16, 4);
        scaleToEvents.map(new Scale(asList(2, 2, 3), C))
                .forEach(player::postEvents);

        scaleToEvents.map(new Scale(asList(2, 1, 2), A))
                .forEach(player::postEvents);

        scaleToEvents.map(new Scale(asList(2, 2, 3), C))
                .forEach(player::postEvents);

        scaleToEvents.map(new Scale(asList(2, 1, 2), A))

                .forEach(player::postEvents);

        scaleToEvents.map(new Scale(asList(3, 5, 2), A))
                .forEach(player::postEvents);

        scaleToEvents.map(new Scale(asList(3, 5, 2), B))
                .forEach(player::postEvents);

        player.play();
    }
}