package fr.gnark.sound.media;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class Events {
    private final List<Event> events;
    private final long clkStart;
    private long durationInClockTicks;
    private boolean isPause;

    private Events(final List<Event> events, final long clkStart, final long durationInClockTicks, final boolean isPause) {
        this.events = events;
        this.clkStart = clkStart;
        this.durationInClockTicks = durationInClockTicks;
        this.isPause = isPause;
    }

    public Events(final List<Event> events, final long clkStart, final long durationInClockTicks) {
        this.events = events;
        this.clkStart = clkStart;
        this.durationInClockTicks = durationInClockTicks;
        this.isPause = false;
    }

    public static Events pause(final long durationInClockTicks) {
        return new Events(Collections.emptyList(), 0L, durationInClockTicks, true);
    }
}
