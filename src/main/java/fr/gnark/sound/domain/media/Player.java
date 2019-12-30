package fr.gnark.sound.domain.media;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Created by Gnark on 13/04/2019.
 */
@Slf4j
public class Player {
    private final Dispatcher dispatcher;
    private final Deque<Events> backlog;
    private final List<Double> frequenciesPlaying;
    private final double definitionInMs;

    public Player(final Dispatcher dispatcher, final double definitionInMs) {
        this.dispatcher = dispatcher;
        backlog = new ArrayDeque<>();
        frequenciesPlaying = new ArrayList<>();
        this.definitionInMs = definitionInMs;
    }

    public void postEvents(final Events events) {
        backlog.add(events);
    }


    public final void play() throws InterruptedException {
        while (!backlog.isEmpty()) {
            Events events = backlog.removeFirst();
            playEvents(events);
        }
    }

    public final void repeat(final int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            final Iterator<Events> eventsIterator = backlog.iterator();
            while (eventsIterator.hasNext()) {
                Events events = eventsIterator.next();
                playEvents(events);
            }
        }
        backlog.clear();
    }

    private void playEvents(final Events events) throws InterruptedException {
        final Iterator<Event> toPlay = events.getEvents();
        while (toPlay.hasNext()) {
            final Event event = toPlay.next();
            frequenciesPlaying.add(event.getFrequency());
            dispatcher.dispatch(event);
        }
        Thread.sleep((long) (events.getDurationInClockTicks() * definitionInMs));
        frequenciesPlaying.forEach(dispatcher::stop);
        frequenciesPlaying.clear();
    }
}
