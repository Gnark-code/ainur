package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Dispatcher;
import fr.gnark.sound.domain.media.Event;
import fr.gnark.sound.domain.media.Instrument;
import fr.gnark.sound.domain.music.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;

@Component
@Slf4j
public class Synthetizer {
    private final Dispatcher dispatcher;
    private final Instrument instrument;

    public Synthetizer(final Instruments instruments) throws LineUnavailableException {
        instrument = instruments.getProxy();
        dispatcher = new Dispatcher(64, instrument);
    }

    public void playNote(final Note note, final float volume) {
        dispatcher.dispatch(Event.builder().frequency(note.convertToFrequency()).amplitude(volume).build());
    }

    public void stopNote(final Note note) {
        dispatcher.stop(note.convertToFrequency());
    }

    public void modifyAttack(final double volumeInPercent) {
        instrument.getEnvelope().modifyAttack(volumeInPercent);
    }

    public void modifyRelease(final double volumeInPercent) {
        instrument.getEnvelope().modifyRelease(volumeInPercent);
    }

    public void modifyDecay(final double volumeInPercent) {
        instrument.getEnvelope().modifyDecay(volumeInPercent);
    }

    public void modifySustain(final double volumeInPercent) {
        instrument.getEnvelope().modifySustain(volumeInPercent);
    }
}
