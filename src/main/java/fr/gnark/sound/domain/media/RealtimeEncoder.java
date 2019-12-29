package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;

@Slf4j
public class RealtimeEncoder {
    private final RealtimeAudioFormat output;
    private boolean stop;
    private final double delta;
    private Instrument instrument;

    public RealtimeEncoder(final Instrument instrument) throws LineUnavailableException {
        this.output = new RealtimeAudioFormat();
        this.instrument = instrument;
        delta = 1 / (output.getFrameRate());
    }

    public void handleEvent(final Event event) {
        stop = false;
        playEventUntilStopped(event);
    }

    private void playEventUntilStopped(final Event event) {
        double time = 0;
        while (!stop) {
            double computed = instrument.getSignal().computeFormula(event.getFrequency(), time);
            double amplitudeR = 0;
            double amplitudeL = 0;

            final double amplitude = event.getAmplitude() * instrument.getEnvelope().computeAmplitude(time);
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
        double localtime = 0;
        while (localtime < instrument.getEnvelope().getReleaseInSeconds()) {
            double computed = 0;
            double amplitudeR = 0;
            double amplitudeL = 0;
            final double amplitude = event.getAmplitude() * instrument.getEnvelope().computeRelease(localtime);
            amplitudeR += amplitude / 100;
            amplitudeL += amplitude / 100;
            //apply panning if necessary
            if (event.getPanning().compareTo(0.0f) != 0) {
                amplitudeR += amplitudeR * (event.getPanning() / 100);
                amplitudeL -= amplitudeL * (event.getPanning() / 100);
            }
            computed += instrument.getSignal().computeFormula(event.getFrequency(), copy);
            output.storeData(amplitudeL * computed, amplitudeR * computed);
            copy += delta;
            localtime += delta;
        }
    }
}