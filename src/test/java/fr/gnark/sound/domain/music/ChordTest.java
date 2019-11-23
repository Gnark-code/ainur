package fr.gnark.sound.domain.music;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Gnark on 01/11/2019.
 */
class ChordTest {
    private Chord getChord(Mode mode, Note rootNote, int numberOfNotes, Degree degree) {
        return Chord.builder().mode(mode)
                .rootNote(rootNote).numberOfNotes(numberOfNotes).degree(degree).build();
    }


    private Chord getChord(Mode mode, Note rootNote, int numberOfNotes, Degree degree, final Alterations alterations) {
        return Chord.builder().mode(mode)
                .rootNote(rootNote)
                .alterations(alterations)
                .numberOfNotes(numberOfNotes).degree(degree).build();
    }

    @Test
    public void checkIonian() {
        final Mode mode = Mode.MAJOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F_SHARP, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F_SHARP, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C_SHARP, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C_SHARP, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F_SHARP, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C_SHARP, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }


    @Test
    public void checkDorian() {
        final Mode mode = Mode.DORIAN;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }

    @Test
    public void checkMyxolydian() {
        final Mode mode = Mode.MYXOLYDIAN;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F_SHARP, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F_SHARP, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F_SHARP, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }

    @Test
    public void checkAeolian() {
        final Mode mode = Mode.MINOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B_FLAT, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B_FLAT, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B_FLAT, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }


    @Test
    public void checkHarmonicMinor() {
        final Mode mode = Mode.MINOR_HARMONIC;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B_FLAT, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C_SHARP, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B_FLAT, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C_SHARP, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B_FLAT, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C_SHARP, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }

    @Test
    public void checklydian() {
        final Mode mode = Mode.LYDIAN;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F_SHARP, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E, 2);
        assertNote(II.getNotes().get(1), BaseNote.G_SHARP, 2);
        assertNote(II.getNotes().get(2), BaseNote.B, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F_SHARP, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C_SHARP, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G_SHARP, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C_SHARP, 3);
        assertNote(V.getNotes().get(2), BaseNote.E, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F_SHARP, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C_SHARP, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G_SHARP, 3);
    }

    @Test
    public void checkPhrygian() {
        final Mode mode = Mode.PHRYGIAN;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E_FLAT, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B_FLAT, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B_FLAT, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C, 3);
        assertNote(V.getNotes().get(2), BaseNote.E_FLAT, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B_FLAT, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E_FLAT, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }

    @Test
    public void checkLocrian() {
        final Mode mode = Mode.LOCRIAN;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F, 2);
        assertNote(I.getNotes().get(2), BaseNote.A_FLAT, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E_FLAT, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B_FLAT, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F, 2);
        assertNote(III.getNotes().get(1), BaseNote.A_FLAT, 2);
        assertNote(III.getNotes().get(2), BaseNote.C, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B_FLAT, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A_FLAT, 2);
        assertNote(V.getNotes().get(1), BaseNote.C, 3);
        assertNote(V.getNotes().get(2), BaseNote.E_FLAT, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B_FLAT, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E_FLAT, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }


    @Test
    public void checkDoubleHarmonicMajor() {
        final Mode mode = Mode.DOUBLE_HARMONIC_MAJOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord I = getChord(mode, rootNote, 3, Degree.I);
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F_SHARP, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);

        final Chord II = getChord(mode, rootNote, 3, Degree.II);
        assertNote(II.getNotes().get(0), BaseNote.E_FLAT, 2);
        assertNote(II.getNotes().get(1), BaseNote.G, 2);
        assertNote(II.getNotes().get(2), BaseNote.B_FLAT, 2);

        final Chord III = getChord(mode, rootNote, 3, Degree.III);
        assertNote(III.getNotes().get(0), BaseNote.F_SHARP, 2);
        assertNote(III.getNotes().get(1), BaseNote.A, 2);
        assertNote(III.getNotes().get(2), BaseNote.C_SHARP, 3);

        final Chord IV = getChord(mode, rootNote, 3, Degree.IV);
        assertNote(IV.getNotes().get(0), BaseNote.G, 2);
        assertNote(IV.getNotes().get(1), BaseNote.B_FLAT, 2);
        assertNote(IV.getNotes().get(2), BaseNote.D, 3);

        final Chord V = getChord(mode, rootNote, 3, Degree.V);
        assertNote(V.getNotes().get(0), BaseNote.A, 2);
        assertNote(V.getNotes().get(1), BaseNote.C_SHARP, 3);
        assertNote(V.getNotes().get(2), BaseNote.E_FLAT, 3);

        final Chord VI = getChord(mode, rootNote, 3, Degree.VI);
        assertNote(VI.getNotes().get(0), BaseNote.B_FLAT, 2);
        assertNote(VI.getNotes().get(1), BaseNote.D, 3);
        assertNote(VI.getNotes().get(2), BaseNote.F_SHARP, 3);

        final Chord VII = getChord(mode, rootNote, 3, Degree.VII);
        assertNote(VII.getNotes().get(0), BaseNote.C_SHARP, 3);
        assertNote(VII.getNotes().get(1), BaseNote.E_FLAT, 3);
        assertNote(VII.getNotes().get(2), BaseNote.G, 3);
    }

    @Test
    public void testInversions() {
        final Mode mode = Mode.MAJOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();
        final Chord firstInversion = getChord(mode, rootNote, 3, Degree.I, Alterations.builder().inversion(1).build());
        assertNote(firstInversion.getNotes().get(0), BaseNote.F_SHARP, 2);
        assertNote(firstInversion.getNotes().get(1), BaseNote.A, 2);
        assertNote(firstInversion.getNotes().get(2), BaseNote.D, 3);

        final Chord secondInversion = getChord(mode, rootNote, 3, Degree.I, Alterations.builder().inversion(2).build());
        assertNote(secondInversion.getNotes().get(0), BaseNote.A, 2);
        assertNote(secondInversion.getNotes().get(1), BaseNote.D, 3);
        assertNote(secondInversion.getNotes().get(2), BaseNote.F_SHARP, 3);

        final Chord noInversion = getChord(mode, rootNote, 3, Degree.I, Alterations.builder().inversion(3).build());
        assertEquals(noInversion, getChord(mode, rootNote, 3, Degree.I));
    }


    @Test
    public void testAddedNotes() {
        final Mode mode = Mode.MAJOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();

        final Chord I = getChord(mode, rootNote, 3, Degree.I, Alterations.builder().addOctave(true).build());
        assertNote(I.getNotes().get(0), BaseNote.D, 2);
        assertNote(I.getNotes().get(1), BaseNote.F_SHARP, 2);
        assertNote(I.getNotes().get(2), BaseNote.A, 2);
        assertNote(I.getNotes().get(3), BaseNote.D, 3);
    }

    @Test
    public void testBassNotes() {
        final Mode mode = Mode.MAJOR;
        final Note rootNote = Note.builder()
                .octave(2)
                .baseNote(BaseNote.D).build();

        Chord I = Chord.builder().mode(mode)
                .rootNote(rootNote).degree(Degree.I)
                .bassPattern(Chord.BassPattern.OCTAVE_BASS)
                .build();
        assertNote(I.getBassNotes().get(0), BaseNote.D, 1);
        I = Chord.builder().mode(mode)
                .rootNote(rootNote).degree(Degree.I)
                .bassPattern(Chord.BassPattern.DOUBLE_OCTAVE_BASS)
                .build();
        assertNote(I.getBassNotes().get(0), BaseNote.D, 1);
        assertNote(I.getBassNotes().get(1), BaseNote.D, 0);

        I = Chord.builder().mode(mode)
                .rootNote(rootNote).degree(Degree.I)
                .bassPattern(Chord.BassPattern.POWER_CHORD)
                .build();
        assertNote(I.getBassNotes().get(0), BaseNote.D, 1);
        assertNote(I.getBassNotes().get(1), BaseNote.A, 0);
        assertNote(I.getBassNotes().get(2), BaseNote.D, 0);
    }


    private void assertNote(Note pitch,
                            BaseNote baseNote, int octave) {
        assertEquals(baseNote, pitch.getBaseNote());
        assertEquals(octave, pitch.getOctave());

    }
}