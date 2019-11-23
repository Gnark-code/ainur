package fr.gnark.sound.domain.media;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Created by Gnark on 13/04/2019.
 */
@Slf4j
public class Player {
    private final Encoder encoder;
    private final Deque<Events> backlog;

    public Player(final Encoder encoder) {
        this.encoder = encoder;
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
