package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;

import java.util.function.Consumer;


public class InstrumentImpl implements Instrument {
    private final String identifier;
    private final Signal signal;
    private final EnvelopeADSR envelopeADSR;
    private Consumer<Double> param1;

    public InstrumentImpl(final String identifier, final Signal signal, final EnvelopeADSR envelopeADSR) {
        this.identifier = identifier;
        this.signal = signal;
        this.envelopeADSR = envelopeADSR;
    }

    public Consumer<Double> getParam1() {
        return param1;
    }

    public void setParam1(final Consumer<Double> param1) {
        this.param1 = param1;
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
