package fr.gnark.sound.applications;

import fr.gnark.sound.domain.anticorruption.ScaleToEvents;
import fr.gnark.sound.domain.media.Events;
import fr.gnark.sound.domain.media.WavEncoder;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Mode;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.music.Scale;
import fr.gnark.sound.domain.physics.waveforms.SawtoothWave;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ScaleAGogo {
    private static final int TICKS_BY_WHOLE_NOTE = 128;
    private static final ScaleToEvents previewScaleToEvents = new ScaleToEvents(TICKS_BY_WHOLE_NOTE / 16, 2);
    private static final double PREVIEW_BPM = 120;


    public byte[] getPreviewData(final Mode mode) throws IOException {
        final List<Events> ListOfEvents = previewScaleToEvents.map(new Scale(mode.getSemitones(), Note.builder().octave(2).baseNote(BaseNote.C).build()));

        final double definitionInMs = 60000.0 / (PREVIEW_BPM * (TICKS_BY_WHOLE_NOTE / 4.0));
        WavEncoder wavEncoder = new WavEncoder("PREVIEW ENCODER", definitionInMs,
                new SawtoothWave()
        );

        for (final Events events : ListOfEvents) {
            wavEncoder.handleEvents(events);
        }
        return wavEncoder.toWavBuffer();

    }
}
