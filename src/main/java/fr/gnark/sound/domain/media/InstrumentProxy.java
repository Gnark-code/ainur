package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;

import java.util.function.DoubleConsumer;

/**
 * Mutable instrument used to change the state of the signal
 */
public class InstrumentProxy implements Instrument {
    private Signal signal;
    private EnvelopeADSR envelopeADSR;
    private DoubleConsumer param1;
    private DoubleConsumer param2;
    private DoubleConsumer param3;
    private DoubleConsumer param4;


    public InstrumentProxy(final InstrumentImpl instrument) {
        change(instrument);
    }

    @Override
    public Signal getSignal() {
        return this.signal;
    }

    @Override
    public EnvelopeADSR getEnvelope() {
        return this.envelopeADSR;
    }

    public void change(final InstrumentImpl instrument) {
        this.signal = instrument.getSignal();
        this.envelopeADSR = instrument.getEnvelope().copy();
        this.param1 = instrument.getParam1();
        this.param2 = instrument.getParam2();
    }

    public void changeParam1(final double value) {
        if (param1 != null) {
            param1.accept(value);
        }
    }

    public void changeParam2(final double value) {
        if (param2 != null) {
            param2.accept(value);
        }
    }
}
