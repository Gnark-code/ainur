package fr.gnark.sound.domain.media;

import lombok.Getter;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@ToString
/**
 * Simultaneous events
 */
public class Events {
    private final Collection<Event> _events;
    @Getter
    private final long clkStart;
    @Getter
    private long durationInClockTicks;
    @Getter
    private boolean isPause;

    private Events(final List<Event> events, final long clkStart, final long durationInClockTicks, final boolean isPause) {
        this._events = new ConcurrentLinkedDeque<>(events);
        this.clkStart = clkStart;
        this.durationInClockTicks = durationInClockTicks;
        this.isPause = isPause;
    }

    public Events(final List<Event> events, final long clkStart, final long durationInClockTicks) {
        this(events, clkStart, durationInClockTicks, false);
    }

    final Iterator<Event> getEvents() {
        return this._events.iterator();
    }

    public void remove(final double frequency) {
        _events.removeIf(event -> event.getFrequency() == frequency);
    }

    public final boolean isEmpty() {
        return this._events.isEmpty();
    }

    final int size() {
        return this._events.size();
    }

    public static Events pause(final long durationInClockTicks) {
        return new Events(Collections.emptyList(), 0L, durationInClockTicks, true);
    }

    public static Events empty(final long durationInClockTicks) {
        return new Events(new ArrayList<>(), 0L, durationInClockTicks, true);
    }

    public void add(final Event event) {
        this._events.add(event);
    }
}
