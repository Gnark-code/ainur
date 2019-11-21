package fr.gnark.sound.media;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.Iterator;

@EqualsAndHashCode
@Getter
@Log
public class Encoder {
    //duration of a clock tick in milliseconds
    private final double definitionInMs;
    private final String name;
    private final Output output;

    public Encoder(final String name, final double definitionInMs, final Output output) {
        this.output = output;
        this.definitionInMs = definitionInMs;
        this.name = name;
    }

    public void handleEvents(final Events events) {
        final long throughput = output.getThroughputInBytes();
        final int frameSize = output.getFrameSize();
        double seconds = (events.getDurationInClockTicks() * definitionInMs) / 1000;
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
                amplitudeR += (event.getAmplitude() / 2) / 100;
                amplitudeL += (event.getAmplitude() / 2) / 100;
                //apply panning if necessary
                if (event.getPanning().compareTo(0.0f) != 0) {
                    amplitudeR += amplitudeR * (event.getPanning() / 100);
                    amplitudeL -= amplitudeL * (event.getPanning() / 100);
                }
                computed += event.getSignal().computeFormula(time);
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
    }

    public void flush() {
        output.flush();
    }
}
