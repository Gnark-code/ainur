package fr.gnark.sound.domain.music;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@ToString
public class BaseNote {
    public static final BaseNote C = new BaseNote(16.35);
    public static final BaseNote C_SHARP = new BaseNote(17.32);
    public static final BaseNote D_FLAT = new BaseNote(C_SHARP.lowestFrequency);
    public static final BaseNote D = new BaseNote(18.35);
    public static final BaseNote D_SHARP = new BaseNote(19.45);
    public static final BaseNote E_FLAT = new BaseNote(D_SHARP.lowestFrequency);
    public static final BaseNote E = new BaseNote(20.60);
    public static final BaseNote F = new BaseNote(21.83);
    public static final BaseNote F_SHARP = new BaseNote(23.12);
    public static final BaseNote G_FLAT = new BaseNote(F_SHARP.lowestFrequency);
    public static final BaseNote G = new BaseNote(24.50);
    public static final BaseNote G_SHARP = new BaseNote(25.96);
    public static final BaseNote A_FLAT = new BaseNote(G_SHARP.lowestFrequency);
    public static final BaseNote A = new BaseNote(27.50);
    public static final BaseNote A_SHARP = new BaseNote(29.14);
    public static final BaseNote B_FLAT = new BaseNote(A_SHARP.lowestFrequency);
    public static final BaseNote B = new BaseNote(30.87);
    @Getter
    private final double lowestFrequency;

    private static final List<BaseNote> orderedList;

    static {
        orderedList = new LinkedList<>();
        orderedList.add(C);
        orderedList.add(C_SHARP);
        orderedList.add(D);
        orderedList.add(E_FLAT);
        orderedList.add(E);
        orderedList.add(F);
        orderedList.add(F_SHARP);
        orderedList.add(G);
        orderedList.add(G_SHARP);
        orderedList.add(A);
        orderedList.add(B_FLAT);
        orderedList.add(B);
    }

    //lowest frequency in equal temperament
    BaseNote(final double lowestFrequency) {
        this.lowestFrequency = lowestFrequency;
    }

    public static final BaseNote getByIndexInTheScale(final int index) {
        if (index < 0 || index > 11) {
            throw new IllegalArgumentException();
        }
        return orderedList.get(index);
    }

    public static final int getIndexOfNote(final BaseNote baseNote) {
        return orderedList.indexOf(baseNote);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseNote baseNote = (BaseNote) o;
        return Double.compare(baseNote.lowestFrequency, lowestFrequency) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowestFrequency);
    }
}
