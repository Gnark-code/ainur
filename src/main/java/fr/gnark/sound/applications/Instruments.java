package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Instrument;
import fr.gnark.sound.domain.media.InstrumentImpl;
import fr.gnark.sound.domain.media.InstrumentProxy;
import fr.gnark.sound.domain.physics.waveforms.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class Instruments {
    private final List<InstrumentImpl> _instruments = new ArrayList<>();
    private InstrumentProxy proxy;
    private int index = 0;

    public Instruments() {
        EnvelopeADSR envelopeADSR = EnvelopeADSR.builder()
                .attackInSeconds(0.02)
                .decayInSeconds(0.01)
                .sustainFactorinDbfs(-3.0)
                .releaseInSeconds(0.05)
                .build();
        _instruments.add(new InstrumentImpl("Triangle",
                new TriangleWave(),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("Sine",
                new SineWave().addHarmonics(1),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("Imperfect sawtooth",
                new SawtoothWaveWithSynthesis().addHarmonics(5),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("Square",
                new SquareWave(),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("Imperfect Square",
                new SquareWaveWithSynthesis().addOvertones(31),
                envelopeADSR.copy()));

        _instruments.add(new InstrumentImpl("sawtooth",
                new SawtoothWave(),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("alternative sawtooth",
                new SawtoothAltWave(),
                envelopeADSR.copy()));
        _instruments.add(new InstrumentImpl("Warp",
                new Warp(),
                EnvelopeADSR.builder()
                        .attackInSeconds(0.05)
                        .decayInSeconds(0.5)
                        .sustainFactorinDbfs(-3.0)
                        .releaseInSeconds(4.0)
                        .build()));
        _instruments.add(new InstrumentImpl("Flute",
                new Flute(),
                EnvelopeADSR.builder()
                        .attackInSeconds(0.05)
                        .decayInSeconds(0.5)
                        .sustainFactorinDbfs(-3.0)
                        .releaseInSeconds(0.3)
                        .build()));
        _instruments.add(getHarpsichord());
        proxy = new InstrumentProxy(_instruments.get(0));
    }

    private InstrumentImpl getHarpsichord() {
        final Harpsichord signal = new Harpsichord();
        InstrumentImpl harpsichord = new InstrumentImpl("Harpsichord",
                signal,
                EnvelopeADSR.builder()
                        .attackInSeconds(0.02)
                        .decayInSeconds(1.5)
                        .sustainFactorinDbfs(-9.0)
                        .releaseInSeconds(0.3)
                        .build());
        harpsichord.setParam1(signal::setPluckingRatio);
        return harpsichord;
    }

    public Instrument getProxy() {
        return proxy;
    }

    public void nextInstrument(final double midiValueRead) {
        if (midiValueRead > 0) {
            index++;
            if (index >= _instruments.size()) {
                index = 0;
            }
            changeInstrument();
        }
    }

    private void changeInstrument() {
        final InstrumentImpl instrument = _instruments.get(index);
        log.info("now selecting " + instrument.getIdentifier());
        proxy.change(_instruments.get(index));
    }

    public void previousInstrument(final double midiValueRead) {
        if (midiValueRead > 0) {
            index--;
            if (index < 0) {
                index = _instruments.size() - 1;
            }
            final InstrumentImpl instrument = _instruments.get(index);
            log.info("now selecting " + instrument.getIdentifier());
            proxy.change(instrument);
        }
    }

    public void changeParam1(final double v) {
        proxy.changeParam1(v);
    }
}
