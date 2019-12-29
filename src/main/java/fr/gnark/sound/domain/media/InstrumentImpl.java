package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;


public class InstrumentImpl implements Instrument {
    private final String identifier;
    private final Signal signal;
    private final EnvelopeADSR envelopeADSR;

    public InstrumentImpl(final String identifier, final Signal signal, final EnvelopeADSR envelopeADSR) {
        this.identifier = identifier;
        this.signal = signal;
        this.envelopeADSR = envelopeADSR;
    }

    @Override
    public Signal getSignal() {
        return signal;
    }

    @Override
    public EnvelopeADSR getEnvelope() {
        return envelopeADSR;
    }

    public String getIdentifier() {
        return identifier;
    }
}
