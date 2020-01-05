package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;

import java.util.function.DoubleConsumer;

public class InstrumentImpl implements Instrument {
    private final String identifier;
    private final Signal signal;
    private final EnvelopeADSR envelopeADSR;
    private DoubleConsumer param1;
    private DoubleConsumer param2;
    private DoubleConsumer param3;
    private DoubleConsumer param4;

    public InstrumentImpl(final String identifier, final Signal signal, final EnvelopeADSR envelopeADSR) {
        this.identifier = identifier;
        this.signal = signal;
        this.envelopeADSR = envelopeADSR;
    }

    public DoubleConsumer getParam1() {
        return param1;
    }

    public void setParam1(final DoubleConsumer param1) {
        this.param1 = param1;
    }

    public DoubleConsumer getParam2() {
        return param2;
    }

    public void setParam2(final DoubleConsumer param2) {
        this.param2 = param2;
    }

    public void setParam3(final DoubleConsumer param3) {
        this.param3 = param3;
    }

    public void setParam4(final DoubleConsumer param4) {
        this.param4 = param4;
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
