package fr.gnark.sound.domain.media;

import fr.gnark.sound.domain.media.output.AudioFormatOutput;
import fr.gnark.sound.domain.media.waveforms.EnvelopeADSR;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@EqualsAndHashCode
@Getter
@Slf4j
public class Encoder {
    //duration of a clock tick in milliseconds
    private final double definitionInMs;
    private final String name;
    private final Output output;
    private final Signal signal;
    private EnvelopeADSR envelope;
    private final Collection<ReleasingEvents> releasingEvents;
    private double totalPlayTime;
    public List<Double> data = new ArrayList<>();

    public Encoder(final String name, final double definitionInMs, final Output output, final Signal signal) {
        this.output = output;
        this.definitionInMs = definitionInMs;
        this.name = name;
        this.signal = signal;
        this.envelope = EnvelopeADSR.builder()
                .attackInSeconds(0.01)
                .decayInSeconds(0.05)
                .sustainFactorinDbfs(-3.0)
                .releaseInSeconds(0.3)
                .build();
        this.releasingEvents = new ConcurrentLinkedDeque<>();
        totalPlayTime = 0.0;
    }

    public void handleEvents(final Events events) {
        final long throughput = output.getThroughputInBytes();
        final int frameSize = output.getFrameSize();
        final double delta = 1 / (output.getFrameRate());
        double seconds = (events.getDurationInClockTicks() * definitionInMs) / 1000;
        int nbBytes = (int) (seconds * throughput);
        // truncate to the nearest complete frame
        nbBytes = nbBytes - (nbBytes % frameSize);
        final Collection<ReleasingEvents> toRemove = new ArrayList<>();
        double time = 0;
        for (int sample = 0; sample < nbBytes; sample = sample + frameSize) {
            double computed = 0;
            double amplitudeR = 0;
            double amplitudeL = 0;
            time += delta;
            if (!events.isPause()) {
                final Iterator<Event> iterator = events.getEvents();
                while (iterator.hasNext()) {
                    final Event event = iterator.next();
                    final double amplitude = event.getAmplitude() * envelope.computeAmplitude(time);
                    amplitudeR += amplitude / 100;
                    amplitudeL += amplitude / 100;
                    //apply panning if necessary
                    if (event.getPanning().compareTo(0.0f) != 0) {
                        amplitudeR += amplitudeR * (event.getPanning() / 100);
                        amplitudeL -= amplitudeL * (event.getPanning() / 100);
                    }
                    computed += signal.computeFormula(event.getFrequency(), time);
                }
            }


            int nbReleasingEvents = 0;
            for (final ReleasingEvents relEvts : releasingEvents) {
                if (envelope.mustComputeRelease(time)) {
                    nbReleasingEvents += relEvts.getEvents().size();
                    final Iterator<Event> iterator2 = relEvts.getEvents().getEvents();
                    while (iterator2.hasNext()) {
                        final Event event = iterator2.next();
                        final double amplitude = event.getAmplitude() * (envelope.computeRelease(time) * 2);
                        amplitudeR += amplitude / 100;
                        amplitudeL += amplitude / 100;
                        //apply panning if necessary
                        if (event.getPanning().compareTo(0.0f) != 0) {
                            amplitudeR += amplitudeR * (event.getPanning() / 100);
                            amplitudeL -= amplitudeL * (event.getPanning() / 100);
                        }
                        computed += signal.computeFormula(event.getFrequency(), totalPlayTime);
                    }
                } else {
                    //remove events
                    toRemove.add(relEvts);
                }
            }
            final int nbOfEvents = events.size() + nbReleasingEvents;
            computed = computed / nbOfEvents;
            amplitudeR = amplitudeR / nbOfEvents;
            amplitudeL = amplitudeL / nbOfEvents;
            data.add(amplitudeL * computed);
            output.storeData(amplitudeL * computed, amplitudeR * computed);

            if (!toRemove.isEmpty()) {
                releasingEvents.removeAll(toRemove);
                toRemove.clear();
            }
            totalPlayTime += delta;
        }

        if (!events.isPause()) {
            this.releasingEvents.add(new ReleasingEvents(events, totalPlayTime));
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

    public void setEnvelope(final EnvelopeADSR envelope) {
        this.envelope = envelope;
    }

    @Getter
    protected class ReleasingEvents {
        private Events events;
        private double timeOfInsertion;

        public ReleasingEvents(final Events events, final double timeOfInsertion) {
            this.events = events;
            this.timeOfInsertion = timeOfInsertion;
        }
    }

}
