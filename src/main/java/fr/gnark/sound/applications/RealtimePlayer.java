package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Event;
import fr.gnark.sound.domain.media.RealtimeEncoder;
import fr.gnark.sound.domain.media.waveforms.SineWave;
import fr.gnark.sound.domain.music.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class RealtimePlayer {
    private final RealtimeEncoder encoder;
    private final ExecutorService service;

    public RealtimePlayer() throws LineUnavailableException {
        encoder = new RealtimeEncoder(new SineWave());
        service = Executors.newFixedThreadPool(4);
    }

    public void playNote(final Note note, final float volume) {
        service.submit(() -> {
                    try {
                        encoder.addEvent(Event.builder().frequency(note.convertToFrequency()).amplitude(volume).build());
                    } catch (final Exception e) {
                        log.error("error caught", e);
                    }
                }
        );
    }

    public void stopNote(final Note note) {
        encoder.removeEvent(note.convertToFrequency());
    }
}
