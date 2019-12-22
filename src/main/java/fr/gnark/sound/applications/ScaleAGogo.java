package fr.gnark.sound.applications;

import fr.gnark.sound.adapter.ScaleToEvents;
import fr.gnark.sound.domain.media.Encoder;
import fr.gnark.sound.domain.media.Events;
import fr.gnark.sound.domain.media.output.AudioFormatOutput;
import fr.gnark.sound.domain.media.waveforms.SawtoothWave;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Mode;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.music.Scale;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;
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
        Encoder encoder = null;
        try {
            encoder = new Encoder("PREVIEW ENCODER", definitionInMs, new AudioFormatOutput(),
                    new SawtoothWave()
            );
        } catch (LineUnavailableException e) {
            throw new IllegalArgumentException("something bad happened");
        }
        for (final Events events : ListOfEvents) {
            encoder.handleEvents(events);
        }
        return encoder.getEncodedData();

    }
}
