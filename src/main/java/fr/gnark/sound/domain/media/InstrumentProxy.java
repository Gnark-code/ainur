package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;

import java.util.function.Consumer;

/**
 * Mutable instrument used to change the state of the signal
 */
public class InstrumentProxy implements Instrument {
    private Signal signal;
    private EnvelopeADSR envelopeADSR;
    private Consumer<Double> param1;

    public InstrumentProxy(final Instrument instrument) {
        this.signal = instrument.getSignal();
        this.envelopeADSR = instrument.getEnvelope().copy();
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
    }

    public void changeParam1(final double value) {
        if (param1 != null) {
            param1.accept(value);
        }
    }
}
