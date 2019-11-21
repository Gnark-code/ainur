package fr.gnark.sound.media;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@ToString
public class Events {
    private final List<Event> _events;
    @Getter
    private final long clkStart;
    @Getter
    private long durationInClockTicks;
    @Getter
    private boolean isPause;

    private Events(final List<Event> events, final long clkStart, final long durationInClockTicks, final boolean isPause) {
        this._events = events;
        this.clkStart = clkStart;
        this.durationInClockTicks = durationInClockTicks;
        this.isPause = isPause;
    }

    public Events(final List<Event> events, final long clkStart, final long durationInClockTicks) {
        this._events = events;
        this.clkStart = clkStart;
        this.durationInClockTicks = durationInClockTicks;
        this.isPause = false;
    }

    final Iterator<Event> getEvents() {
        return this._events.iterator();
    }

    final int size() {
        return this._events.size();
    }

    public static Events pause(final long durationInClockTicks) {
        return new Events(Collections.emptyList(), 0L, durationInClockTicks, true);
    }
}
