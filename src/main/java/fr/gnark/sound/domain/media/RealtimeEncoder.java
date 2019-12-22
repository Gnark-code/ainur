package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.output.RealtimeAudioFormat;

import javax.sound.sampled.LineUnavailableException;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RealtimeEncoder {

    private final RealtimeAudioFormat output;
    private final Signal signal;
    private final Events events;
    private final ExecutorService service;
    private boolean stop;

    public RealtimeEncoder(final Signal signal) throws LineUnavailableException {
        this.events = Events.empty(1L);
        this.output = new RealtimeAudioFormat();
        this.signal = signal;
        this.service = Executors.newFixedThreadPool(1);
    }

    public void start() {
        final long throughput = output.getThroughputInBytes();
        final int frameSize = output.getFrameSize();
        // truncate to the nearest complete frame
        int sample = 0;
        while (!stop && !events.isEmpty()) {
            sample = sample + frameSize;
            double computed = 0;
            double amplitudeR = 0;
            double amplitudeL = 0;
            double time = sample / (double) throughput;
            final Iterator<Event> iterator = events.getEvents();
            final int eventSize = events.size();
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
            computed = computed / eventSize;
            amplitudeR = amplitudeR / eventSize;
            amplitudeL = amplitudeL / eventSize;
            output.storeData(amplitudeL * computed, amplitudeR * computed);
        }
        output.clean();
    }

    public void removeEvent(final double freq) {
        this.events.remove(freq);
        if (this.events.isEmpty()) {
            stop = true;
        }
    }

    public void addEvent(final Event event) {
        if (events.isEmpty()) {
            this.events.add(event);
            stop = false;
            this.start();
        } else {
            this.events.add(event);
        }
    }


}
