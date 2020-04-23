package fr.gnark.sound.domain.music;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Gnark on 21/07/2019.
 */
public class NoteTest {


    @Test
    public void test() {
        Note note = Note.builder().baseNote(BaseNote.C).octave(0).build();
        assertEquals(BaseNote.C_SHARP, note.transpose(1).getBaseNote());
        assertEquals(BaseNote.D, note.transpose(2).getBaseNote());
        assertEquals(BaseNote.E_FLAT, note.transpose(3).getBaseNote());
        assertEquals(BaseNote.E, note.transpose(4).getBaseNote());
        assertEquals(BaseNote.F, note.transpose(5).getBaseNote());
        assertEquals(BaseNote.F_SHARP, note.transpose(6).getBaseNote());
        assertEquals(BaseNote.G, note.transpose(7).getBaseNote());
        assertEquals(BaseNote.G_SHARP, note.transpose(8).getBaseNote());
        assertEquals(BaseNote.A, note.transpose(9).getBaseNote());
        assertEquals(BaseNote.B_FLAT, note.transpose(10).getBaseNote());
        assertEquals(BaseNote.B, note.transpose(11).getBaseNote());

    }

    @Test
    public void testOverBound() {
        Note note = Note.builder().baseNote(BaseNote.C).octave(1).build();
        assertEquals(BaseNote.C, note.transpose(12).getBaseNote());
        assertEquals(2, note.transpose(12).getOctave());
        assertEquals(BaseNote.C_SHARP, note.transpose(13).getBaseNote());
        assertEquals(2, note.transpose(13).getOctave());
    }

    @Test
    public void testLowerOctave() {
        Note note = Note.builder().baseNote(BaseNote.C).octave(2).build();
        Note note2 = Note.builder().baseNote(BaseNote.D).octave(2).build();
        assertEquals(BaseNote.B, note.transpose(-1).getBaseNote());
        assertEquals(BaseNote.C, note2.transpose(-2).getBaseNote());

    }

    @Test
    public void
    testOverBoundMultipleOctaves() {
        Note note = Note.builder().baseNote(BaseNote.C).octave(1).build();
        assertEquals(BaseNote.C, note.transpose(24).getBaseNote());
        assertEquals(3, note.transpose(24).getOctave());
        assertEquals(BaseNote.C_SHARP, note.transpose(25).getBaseNote());
        assertEquals(3, note.transpose(25).getOctave());
    }

    @Test
    public void testConversion() {
        final Double firstFreq = Note.builder().baseNote(BaseNote.A
        ).octave(2)
                .build().convertToFrequency();
        final Double secondFrequency = Note.builder().baseNote(BaseNote.E
        ).octave(3)
                .build().convertToFrequency();
        assertTrue(firstFreq < secondFrequency);
    }

    @Test
    public void testGetFromFrequency() {
        final double aFlatFrequency = 207.01;
        Note note = Note.getFromFrequency(aFlatFrequency, 10.0).orElse(null);
        Assertions.assertEquals(Note.builder().baseNote(BaseNote.A_FLAT).octave(2).build(), note);
    }
}