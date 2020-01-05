package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.physics.Signal;
import fr.gnark.sound.domain.physics.waveforms.EnvelopeADSR;

public interface Instrument {
    Signal getSignal();

    EnvelopeADSR getEnvelope();

}
