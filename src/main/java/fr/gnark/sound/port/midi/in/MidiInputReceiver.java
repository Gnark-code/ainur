package fr.gnark.sound.port.midi.in;

import fr.gnark.sound.applications.Synthetizer;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sound.midi.*;
import java.util.Optional;
import java.util.stream.Stream;

import static javax.sound.midi.ShortMessage.*;

@Component
@Slf4j
public class MidiInputReceiver implements Receiver {
    @Value("${midi.input.name:null}")
    private String inputName;
    private Synthetizer synthetizer;
    private ArturiaKeyRegistry keyRegistry;

    public MidiInputReceiver(final Synthetizer synthetizer, final ArturiaKeyRegistry keyRegistry) {
        this.synthetizer = synthetizer;
        this.keyRegistry = keyRegistry;
    }

    @PostConstruct
    public void test() {
        MidiDevice device;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        Optional<MidiDevice.Info> info = Stream.of(infos).filter(i -> i.getName().contains(inputName)
                //class is package protected => use name to distinguish IN/OUT
                && i.getClass().getCanonicalName().contains("MidiIn")
        )
                .findFirst();
        if (info.isPresent()) {
            try {
                device = MidiSystem.getMidiDevice(info.get());
                device.open();
                device.getTransmitter().setReceiver(this);

            } catch (MidiUnavailableException e) {
                log.error("error caught", e);
            }
        }
    }

    @Override
    public void send(final MidiMessage message, final long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) message;
            final int value = shortMessage.getData1();
            final int volume = shortMessage.getData2();
            final int command = shortMessage.getCommand();
            final int channel = shortMessage.getChannel();
            final double volumeInPercent = (volume * 100.0) / 127.0;


            switch (command) {
                case NOTE_ON:
                    final Note note = getNote(value);
                    this.synthetizer.playNote(note, (float) volumeInPercent);
                    break;
                case NOTE_OFF:
                    final Note noteToStop = getNote(value);
                    this.synthetizer.stopNote(noteToStop);
                    break;
                case CONTROL_CHANGE:
                    keyRegistry.triggerControlChange(value, volumeInPercent);
                    break;
                default:
                    log.trace("unmanaged midi event received" + shortMessage.getCommand());
            }
            if (log.isTraceEnabled()) {
                log.trace("status:" + shortMessage.getStatus());
                log.trace("command:" + shortMessage.getCommand());
                log.trace("data 1:" + value);
                log.trace("data 2:" + shortMessage.getData2());
            }
        }
    }

    private Note getNote(final int value) {
        final int octave = (value / 12) - 1;
        final int index = value - ((octave + 1) * 12);
        return Note.builder().octave(octave)
                .baseNote(BaseNote.getByIndexInTheScale(index)).build();
    }

    @Override
    public void close() {
        log.info("midi device closed");
    }
}
