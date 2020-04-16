package fr.gnark.sound.applications;

import fr.gnark.sound.domain.media.Instrument;
import fr.gnark.sound.domain.media.InstrumentImpl;
import fr.gnark.sound.domain.media.InstrumentProxy;
import fr.gnark.sound.domain.music.BaseNote;
import fr.gnark.sound.domain.music.Note;
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

    private SampleImporter sampleImporter;

    public Instruments(final SampleImporter sampleImporter) {
        this.sampleImporter = sampleImporter;
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
        _instruments.add(getFlute());
        _instruments.add(getHarpsichord());
        // TODO : uncomment when performances of samples loading are better
        //  _instruments.add(getGuitar());
        proxy = new InstrumentProxy(_instruments.get(0));
    }

    private InstrumentImpl getGuitar() {
        final Guitar guitar = new Guitar();
        final double[] baseSample = this.sampleImporter.getWavBuffer("classpath:samples/guitar_e_44100_mono.wav");
        final Note e = Note.builder().octave(2).baseNote(BaseNote.E).build();
        guitar.addSampleData(baseSample, e.convertToFrequency());

        for (int semiToneUp = 1; semiToneUp < 4 * 12; semiToneUp++) {
            final double ratio = Math.pow(2, (semiToneUp / 12.0));
            guitar.addSampleData(this.sampleImporter.pitchShift(baseSample, ratio), e.transpose(semiToneUp).convertToFrequency());
        }

        for (int semitoneDown = 1; semitoneDown < 6; semitoneDown++) {
            final double ratio = Math.pow(2, (-semitoneDown / 12.0));
            guitar.addSampleData(this.sampleImporter.pitchShift(baseSample, ratio), e.transpose(-semitoneDown).convertToFrequency());
        }
        return new InstrumentImpl("Guitar",
                guitar,
                EnvelopeADSR.builder()
                        .attackInSeconds(0.02)
                        .decayInSeconds(0.00001)
                        .sustainFactorinDbfs(-1.0)
                        .releaseInSeconds(0.00001)
                        .build());
    }

    private InstrumentImpl getHarpsichord() {
        final double attackInSeconds = 0.02;
        final Harpsichord signal = new Harpsichord(attackInSeconds);
        InstrumentImpl harpsichord = new InstrumentImpl("Harpsichord",
                signal,
                EnvelopeADSR.builder()
                        .attackInSeconds(attackInSeconds)
                        .decayInSeconds(1.5)
                        .sustainFactorinDbfs(-9.0)
                        .releaseInSeconds(0.3)
                        .build());
        harpsichord.setParam1(signal::setPluckingRatio);
        harpsichord.setParam2(signal::setOctaveRatio);
        return harpsichord;
    }

    private InstrumentImpl getFlute() {
        final Flute signal = new Flute();
        InstrumentImpl flute = new InstrumentImpl("Flute",
                signal,
                EnvelopeADSR.builder()
                        .attackInSeconds(0.05)
                        .decayInSeconds(0.5)
                        .sustainFactorinDbfs(-3.0)
                        .releaseInSeconds(0.3)
                        .build());

        flute.setParam1(signal::setVibratoInCents);
        flute.setParam2(signal::setAirblow);
        return flute;
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

    public void changeParam2(final double v) {
        proxy.changeParam2(v);
    }
}
