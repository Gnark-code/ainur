package fr.gnark.sound.port.midi.in;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MidiKeyboardKey {
    private Keys name;
    private int value;
}
