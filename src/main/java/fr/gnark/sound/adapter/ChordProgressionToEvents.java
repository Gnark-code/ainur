package fr.gnark.sound.adapter;

import fr.gnark.sound.domain.media.Event;
import fr.gnark.sound.domain.media.Events;
import fr.gnark.sound.domain.music.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.gnark.sound.domain.music.PlayStyle.*;

/**
 * Created by Gnark on 13/07/2019.
 */
public class ChordProgressionToEvents {
    private static final int RAKE_INDEX = 2;
    private final long ticksByWholeNote;

    public ChordProgressionToEvents(final long ticksByWholeNote) {
        this.ticksByWholeNote = ticksByWholeNote;
    }


    public List<Events> map(final ChordProgression chordProgression) {
        final List<Events> result = new ArrayList<>();
        final List<PlayableChord> chords = chordProgression.generateChords();
        long startTime = 0;
        for (final PlayableChord chord : chords) {
            final long duration = getTicks(chord.getDuration());
            final PlayStyle playstyle = chord.getPlaystyle();
            if (!chord.getDuration().isPause()) {
                final List<Event> bassNotesEvents = chord.getBassNotes().stream()
                        .map(ChordProgressionToEvents::pitchToEvent)
                        .collect(Collectors.toList());


                if (UNISON.equals(playstyle)) {
                    startTime = mapUnison(result, startTime, chord, duration, bassNotesEvents);
                } else if (ONE_WAY_ARPEGGIO.equals(playstyle)) {
                    startTime = mapOneWayArpeggio(result, startTime, chord, duration, bassNotesEvents);
                } else if (ARPEGGIO.equals(playstyle)) {
                    startTime = mapArpeggio(result, startTime, chord, duration, bassNotesEvents);
                } else if (RAKE.equals(playstyle)) {
                    startTime = mapRake(result, startTime, chord, duration, bassNotesEvents);
                }
            } else {
                result.add(Events.pause(duration));
            }
        }
        return result;
    }

    /**
     * 2 -----------
     * 3 -----------
     * 3 -----------
     * 1 -----------
     * <- duration ->
     */
    private long mapUnison(final List<Events> result, long startTime, final PlayableChord chord, final long duration, final List<Event> events) {

        events.addAll(mapChordToEvents(chord));
        result.add(new Events(events, startTime, duration));

        startTime += duration;
        return startTime;
    }

    /**
     * 2
     * 3
     * 3
     * 1
     * <-duration->
     */
    private long mapOneWayArpeggio(final List<Events> result, long startTime, final PlayableChord chord, final long duration, final List<Event> events) {

        for (int i = 0; i < chord.getNotes().size(); i++) {
            final long noteDuration = (duration / chord.getNotes().size());
            final List<Event> eventsAdded = new ArrayList<>();
            eventsAdded.add(pitchToEvent(chord.getNotes().get(i)));
            eventsAdded.addAll(events);
            result.add(new Events(eventsAdded, startTime, noteDuration));
            startTime += noteDuration;
        }
        return startTime;
    }

    /**
     * 4
     * 3    3
     * 3        3
     * 1           1
     * <- duration ->
     */
    private long mapArpeggio(final List<Events> result, long startTime, final PlayableChord chord, final long duration, final List<Event> events) {

        final int nbNotes = (chord.getNotes().size() * 2) - 1;
        final long noteDuration = (duration / nbNotes);
        for (int i = 0; i < chord.getNotes().size(); i++) {
            final List<Event> eventsAdded = new ArrayList<>();
            eventsAdded.add(pitchToEvent(chord.getNotes().get(i)));
            eventsAdded.addAll(events);
            result.add(new Events(eventsAdded, startTime, noteDuration));

            startTime += noteDuration;
        }
        for (int j = chord.getNotes().size() - 2; j > 0; j--) {
            final List<Event> eventsAdded = new ArrayList<>();
            eventsAdded.add(pitchToEvent(chord.getNotes().get(j)));
            eventsAdded.addAll(events);
            result.add(new Events(eventsAdded, startTime, noteDuration));

            startTime += noteDuration;
        }
        return startTime;
    }

    /**
     * 4 ----
     * 3 -------
     * 3 ---let----
     * 1 ------ring--
     * <- duration ->
     */
    private long mapRake(final List<Events> result, long startTime, final PlayableChord chord, final long duration, final List<Event> events) {
        for (int i = 0; i < chord.getNotes().size(); i++) {
            events.add(pitchToEvent(chord.getNotes().get(i)));
            Events newEvents = new Events(new ArrayList<>(events), startTime, RAKE_INDEX);
            result.add(newEvents);
            startTime += RAKE_INDEX;
        }
        result.add(new Events(mapChordToEvents(chord), startTime, duration - chord.getNotes().size() * RAKE_INDEX));
        startTime += duration - chord.getNotes().size() * RAKE_INDEX;
        return startTime;
    }


    private List<Event> mapChordToEvents(final PlayableChord chord) {
        final List<Event> events = new ArrayList<>();
        for (int i = 0; i < chord.getNotes().size(); i++) {
            events.add(pitchToEvent(chord.getNotes().get(i)));
        }
        return events;
    }

    private long getTicks(final Subdivision duration) {
        //fastest way to divide by power of 2
        long ticks = ticksByWholeNote >> duration.getType().ordinal();
        if (duration.isTriplet()) {
            return (ticks * 2) / 3;
        } else if (duration.isDotted()) {
            return (long) (ticks * 1.5);
        }
        return ticks;
    }

    private static Event pitchToEvent(final Note pitch) {
        final double freq = pitch.convertToFrequency();
        return Event.builder()
                .frequency(freq)
                .amplitude(100f)
                .build();
    }

}
