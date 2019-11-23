package fr.gnark.sound.applications;

import fr.gnark.sound.adapter.ChordProgressionToEvents;
import org.springframework.stereotype.Component;

@Component
public class Chordinator {
    private static final int TICKS_BY_WHOLE_NOTE = 128;
    private static final ChordProgressionToEvents CHORD_PROGRESSION_TO_EVENTS = new ChordProgressionToEvents(TICKS_BY_WHOLE_NOTE);
    private static int BPM = 150;

}
