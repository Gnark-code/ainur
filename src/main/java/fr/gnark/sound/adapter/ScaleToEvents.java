package fr.gnark.sound.adapter;

import fr.gnark.sound.domain.media.Event;
import fr.gnark.sound.domain.media.Events;
import fr.gnark.sound.domain.music.Note;
import fr.gnark.sound.domain.music.Scale;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class ScaleToEvents {
    private final int ticksByEvent;
    private final int nbOctaves;

    public ScaleToEvents(final int ticksByEvent, final int nbOctaves) {
        this.ticksByEvent = ticksByEvent;
        this.nbOctaves = nbOctaves;
    }

    public List<Events> map(final Scale scale) {
        final List<Events> result = new ArrayList<>();
        long startTime = 0;
        for (int i = 0; i < nbOctaves; i++) {
            for (final Note note : scale.getNotes()) {
                result.add(new Events(singletonList(pitchToEvent(note.transpose(i * 12))), startTime, ticksByEvent));
                startTime += ticksByEvent;
            }
        }
        for (int j = nbOctaves; j >= 0; j--) {
            for (int k = scale.getNotes().size() - 1; k > 0; k--) {
                result.add(new Events(singletonList(pitchToEvent(scale.getNotes().get(k).transpose(j * 12))), startTime, ticksByEvent));
                startTime += ticksByEvent;
            }
        }
        return result;
    }

    private static Event pitchToEvent(final Note pitch) {
        final double freq = pitch.convertToFrequency();
        return Event.builder()
                .frequency(freq)
                .amplitude(100f)
                .build();
    }
}
