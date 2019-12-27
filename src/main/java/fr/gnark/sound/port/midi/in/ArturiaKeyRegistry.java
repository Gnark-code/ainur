package fr.gnark.sound.port.midi.in;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ArturiaKeyRegistry {
    public static final MidiKeyboardKey ATTACK_1 = build("ATTACK_1", 73);
    public static final MidiKeyboardKey DECAY_1 = build("DECAY_1", 75);
    public static final MidiKeyboardKey SUSTAIN_1 = build("SUSTAIN_1", 79);
    public static final MidiKeyboardKey RELEASE_1 = build("RELEASE_1", 72);
    public static final MidiKeyboardKey MASTER_VOLUME = build("MASTER_VOLUME", 85);
    private final Map<Integer, MidiKeyboardKey> _map = new HashMap<>();


    @PostConstruct
    private void addItems() {
        this._map.put(ATTACK_1.getValue(), ATTACK_1);
        this._map.put(DECAY_1.getValue(), DECAY_1);
        this._map.put(SUSTAIN_1.getValue(), SUSTAIN_1);
        this._map.put(RELEASE_1.getValue(), RELEASE_1);
        this._map.put(MASTER_VOLUME.getValue(), MASTER_VOLUME);
    }


    private static MidiKeyboardKey build(final String name, final int value) {
        return new MidiKeyboardKey(name, value);
    }


    public MidiKeyboardKey get(final int value) {
        return _map.get(value);
    }
}
