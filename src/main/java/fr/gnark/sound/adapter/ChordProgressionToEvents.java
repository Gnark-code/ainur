package fr.gnark.sound.adapter;

import fr.gnark.sound.domain.music.ChordProgression;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.music.PlayableChord;
import fr.gnark.sound.domain.music.Subdivision;
import fr.gnark.sound.media.Event;
import fr.gnark.sound.media.Events;
import fr.gnark.sound.media.Signal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.gnark.sound.domain.music.PlayStyle.*;
import static java.util.Collections.singletonList;

/**
 * Created by Gnark on 13/07/2019.
 */
public class ChordProgressionToEvents {
    private static final int RAKE_INDEX = 1;
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
            if (!chord.getDuration().isPause()) {
                final List<Event> events = chord.getBassNotes().stream()
                        .map(ChordProgressionToEvents::pitchToEvent)
                        .collect(Collectors.toList());

                if (UNISON.equals(chord.getPlaystyle())) {
                /*    2 -----------
                      3 -----------
                      3 -----------
                      1 -----------
                      <- duration ->
                     */
                    events.addAll(mapChordToEvents(chord));
                    result.add(new Events(events, startTime, duration));

                    startTime += duration;
                } else if (ONE_WAY_ARPEGGIO.equals(chord.getPlaystyle())) {
                   /*           2
                             3
                          3
                       1
                      <- duration ->
                     */
                    for (int i = 0; i < chord.getNotes().size(); i++) {
                        final long noteDuration = (duration / chord.getNotes().size());

                        result.add(new Events(singletonList(pitchToEvent(chord.getNotes().get(i))), startTime, noteDuration));

                        startTime += noteDuration;
                    }
                } else if (ARPEGGIO.equals(chord.getPlaystyle())) {
                   /*       4
                          3   3
                        3       3
                      1           1
                      <- duration ->
                     */
                    final int nbNotes = (chord.getNotes().size() * 2) - 1;
                    final long noteDuration = (duration / nbNotes);
                    for (int i = 0; i < chord.getNotes().size(); i++) {
                        events.add(pitchToEvent(chord.getNotes().get(i)));
                        result.add(new Events(events, startTime, noteDuration));

                        startTime += noteDuration;
                    }
                    for (int j = chord.getNotes().size() - 2; j > 0; j--) {
                        events.add(pitchToEvent(chord.getNotes().get(j)));
                        result.add(new Events(events, startTime, noteDuration));

                        startTime += noteDuration;
                    }
                } else if (RAKE.equals(chord.getPlaystyle())) {
                    /*      4 ------
                          3 --------
                        3 ---let----
                      1 ------ring--
                      <- duration ->
                     */
                    for (int i = 0; i < chord.getNotes().size(); i++) {
                        events.add(pitchToEvent(chord.getNotes().get(i)));
                        Events newEvents = new Events(new ArrayList<>(events), startTime, RAKE_INDEX);
                        result.add(newEvents);
                        startTime += RAKE_INDEX;
                    }
                    result.add(new Events(mapChordToEvents(chord), startTime, duration - chord.getNotes().size() * RAKE_INDEX));
                    startTime += duration - chord.getNotes().size() * RAKE_INDEX;
                }
            } else {
                result.add(Events.pause(duration));
            }
        }
        return result;
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
                .signal(new Signal(Signal.SIGNAL_TYPE.SAWTOOTH_SYNTHESIS, freq)
                        .addHarmonics(5)
                        .computeBuffer()
                )
                .build();
    }

}
