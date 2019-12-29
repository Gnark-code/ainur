package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;

public interface Instrument {
    Signal getSignal();

    EnvelopeADSR getEnvelope();
}
