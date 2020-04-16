package fr.gnark.sound.domain.physics;

import fr.gnark.sound.domain.DomainException;
import fr.gnark.sound.domain.DomainObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Frames extends DomainObject {
    @Getter
    private final int frameSize;
    private final List<double[]> _frames;

    public Frames(final int frameSize) {
        this.frameSize = frameSize;
        this._frames = new ArrayList<>();
    }

    public Frames addWithOverlap(final double[] data, final int overlap) {
        for (int i = 0; i < data.length; i +=  overlap) {
            if (i + frameSize < data.length) {
                final double[] buffer = new double[frameSize];
                System.arraycopy(data, i, buffer, 0, frameSize);
                _frames.add(buffer);
            }
        }
        return this;
    }

    public Iterator<double[]> iterator() {
        return _frames.iterator();
    }

    public void add(final double[] frame) {
        if (frame.length != frameSize) {
            throw new DomainException("expected frame of length " + frameSize + " . given length of input:" + frame.length);
        }

        this._frames.add(frame);
    }

    public int size() {
        return _frames.size();
    }
}
