package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;

/**
 * Mutable instrument used to change the state of the signal
 */
public class InstrumentProxy implements Instrument {
    private Signal signal;
    private EnvelopeADSR envelopeADSR;

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

    public void change(final Instrument instrument) {
        this.signal = instrument.getSignal();
        this.envelopeADSR = instrument.getEnvelope();
    }
}
