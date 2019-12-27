package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Dispatcher;
import fr.gnark.sound.domain.media.Event;
import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;
import fr.gnark.sound.domain.media.waveforms.SawtoothWaveWithSynthesis;
import fr.gnark.sound.domain.music.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sound.sampled.LineUnavailableException;

@Component
@Slf4j
public class Synthetizer {
    private final Dispatcher dispatcher;
    private final EnvelopeADSR envelopeADSR;

    public Synthetizer() throws LineUnavailableException {
        envelopeADSR = EnvelopeADSR.builder()
                .attackInSeconds(0.02)
                .decayInSeconds(0.01)
                .sustainFactorinDbfs(-3.0)
                .releaseInSeconds(0.05)
                .build();
        dispatcher = new Dispatcher(16, new SawtoothWaveWithSynthesis().addHarmonics(5), envelopeADSR);

    }

    public void playNote(final Note note, final float volume) {
        dispatcher.dispatch(Event.builder().frequency(note.convertToFrequency()).amplitude(volume).build());
    }

    public void stopNote(final Note note) {
        dispatcher.stop(note.convertToFrequency());
    }

    public void modifyAttack(final double volumeInPercent) {
        envelopeADSR.modifyAttack(volumeInPercent);
    }

    public void modifyRelease(final double volumeInPercent) {
        envelopeADSR.modifyRelease(volumeInPercent);
    }
}
