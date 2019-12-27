package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;

@Slf4j
public class RealtimeEncoder {
    private final RealtimeAudioFormat output;
    private final Signal signal;
    private boolean stop;
    private final EnvelopeADSR envelope;
    private final long throughput;
    private final int frameSize;
    private final double delta;

    public RealtimeEncoder(final Signal signal, final EnvelopeADSR envelopeADSR) throws LineUnavailableException {
        this.output = new RealtimeAudioFormat();
        this.signal = signal;
        this.envelope = envelopeADSR;
        throughput = output.getThroughputInBytes();
        frameSize = output.getFrameSize();
        delta = 1 / (output.getFrameRate());
    }

    public void handleEvent(final Event event) {
        stop = false;
        playEventUntilStopped(event);
    }

    private void playEventUntilStopped(final Event event) {
        double time = 0;
        while (!stop) {
            double computed = signal.computeFormula(event.getFrequency(), time);
            double amplitudeR = 0;
            double amplitudeL = 0;


            final double amplitude = event.getAmplitude() * envelope.computeAmplitude(time);
            amplitudeR += amplitude / 100;
            amplitudeL += amplitude / 100;
            //apply panning if necessary
            if (event.getPanning().compareTo(0.0f) != 0) {
                amplitudeR += amplitudeR * (event.getPanning() / 100);
                amplitudeL -= amplitudeL * (event.getPanning() / 100);
            }
            output.storeData(amplitudeL * computed, amplitudeR * computed);
            time += delta;
        }
        releaseEvent(event, time);
        output.cleanup();
    }

    public void stop() {
        stop = true;
    }


    private void releaseEvent(final Event event, final double time) {
        double copy = time;
        // truncate to the nearest complete frame
        int nbBytes = (int) (envelope.getReleaseInSeconds() * throughput);
        // truncate to the nearest complete frame
        nbBytes = nbBytes - (nbBytes % frameSize);
        double localtime = 0;
        for (int sample = 0; sample < nbBytes; sample = sample + frameSize) {
            double computed = 0;
            double amplitudeR = 0;
            double amplitudeL = 0;
            final double amplitude = event.getAmplitude() * envelope.computeRelease(localtime);
            amplitudeR += amplitude / 100;
            amplitudeL += amplitude / 100;
            //apply panning if necessary
            if (event.getPanning().compareTo(0.0f) != 0) {
                amplitudeR += amplitudeR * (event.getPanning() / 100);
                amplitudeL -= amplitudeL * (event.getPanning() / 100);
            }
            computed += signal.computeFormula(event.getFrequency(), copy);
            output.storeData(amplitudeL * computed, amplitudeR * computed);
            copy += delta;
            localtime += delta;
        }
    }
}