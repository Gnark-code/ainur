package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.output.AudioFormatOutput;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Iterator;

@EqualsAndHashCode
@Getter
@Slf4j
public class Encoder {
    //duration of a clock tick in milliseconds
    private final double definitionInMs;
    private final String name;
    private final Output output;
    private final Signal signal;
    public Encoder(final String name, final double definitionInMs, final Output output, final Signal signal) {
        this.output = output;
        this.definitionInMs = definitionInMs;
        this.name = name;
        this.signal = signal;
    }

    public void handleEvents(final Events events) {
        final long throughput = output.getThroughputInBytes();
        final int frameSize = output.getFrameSize();
        double seconds = (events.getDurationInClockTicks() * definitionInMs) / 1000;
        seconds -= (256.0 / 44100.0);
        int nbBytes = (int) (seconds * throughput);
        // truncate to the nearest complete frame
        nbBytes = nbBytes - (nbBytes % frameSize);
        for (int sample = 0; sample < nbBytes; sample = sample + frameSize) {
            double computed = 0;
            double amplitudeR = 0;
            double amplitudeL = 0;
            double time = sample / (double) throughput;
            final Iterator<Event> iterator = events.getEvents();
            while (iterator.hasNext()) {
                final Event event = iterator.next();
                amplitudeR += ((event.getAmplitude()) / 100);
                amplitudeL += ((event.getAmplitude()) / 100);
                //apply panning if necessary
                if (event.getPanning().compareTo(0.0f) != 0) {
                    amplitudeR += amplitudeR * (event.getPanning() / 100);
                    amplitudeL -= amplitudeL * (event.getPanning() / 100);
                }
                computed += signal.computeFormula(event.getFrequency(), time);
            }
            computed = computed / events.size();
            amplitudeR = amplitudeR / events.size();
            amplitudeL = amplitudeL / events.size();
            if (!events.isPause()) {
                output.storeData(amplitudeL * computed, amplitudeR * computed);
            } else {
                output.storeData(0, 0);
            }
        }
        output.cleanup();
    }

    public void flush() {
        output.flush();
    }

    public byte[] getEncodedData() throws IOException {
        if (this.output instanceof AudioFormatOutput) {
            return ((AudioFormatOutput) output).toWavBuffer();
        }
        return output.getBuffer();
    }

}
