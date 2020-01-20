package fr.gnark.sound.domain.physics;

import java.util.Iterator;
/*
*
*
*
 */
public class OverlapAdder {
    private final int overlap;

    public OverlapAdder(final int overlap) {
        this.overlap = overlap;
    }

    public double[] proceed(final Frames frames) {
        double[] result = new double[frames.getFrameSize() + ((frames.size() - 1) * (frames.getFrameSize() - overlap))];
        int n = 0;
        final Iterator<double[]> iterator = frames.iterator();
        while (iterator.hasNext()) {
            final double[] frame = iterator.next();
            for (int j = 0; j < frame.length; j++) {
                if (j < overlap && n > j) {
                    result[n] = result[n] + frame[j];
                } else {
                    result[n] = frame[j];
                }
                n++;
            }
            n = n - overlap;
        }
        return result;
    }

}
