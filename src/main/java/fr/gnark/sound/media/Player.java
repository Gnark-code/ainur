package fr.gnark.sound.media;

import lombok.extern.java.Log;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Created by Gnark on 13/04/2019.
 */
@Log
public class Player {
    private final Encoder encoder;

    private final ArrayDeque<Events> backlog;
    private final double definitionInMs;

    public Player(final int bpm, final double ticksByWholeNote) throws LineUnavailableException {
        this.definitionInMs = 60000.0 / (bpm * (ticksByWholeNote / 4));
        this.encoder = new Encoder("SAWTOOTH", definitionInMs, new AudioFormatOutput());
        backlog = new ArrayDeque<>();
    }

    public void postEvents(final Events events) {
        backlog.add(events);
    }


    public final void play() {
        while (!backlog.isEmpty()) {
            Events events = backlog.removeFirst();
            encoder.handleEvents(events);
        }
        encoder.flush();
    }

    public final void repeat(final int times) {
        for (int i = 0; i < times; i++) {
            final Iterator<Events> eventsIterator = backlog.iterator();
            while (eventsIterator.hasNext()) {
                encoder.handleEvents(eventsIterator.next());
            }
            encoder.flush();
        }
        backlog.clear();
    }
}
