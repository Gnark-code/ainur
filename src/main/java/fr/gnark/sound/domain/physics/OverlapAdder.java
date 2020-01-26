package fr.gnark.sound.domain.physics;

import java.util.Iterator;
/*
*
*
*
 */
public class OverlapAdder {
    private final int hopA;
    private final int overlap;

    public OverlapAdder(final int hopA, final int overlap) {
        this.overlap = overlap;
        this.hopA = hopA;
    }

    public double[] proceed(final Frames frames) {
        double[] result = new double[frames.size()*overlap-overlap+frames.getFrameSize()];
        int n = 0;
        final Iterator<double[]> iterator = frames.iterator();
        while (iterator.hasNext()) {
            final double[] frame = iterator.next();
            int j = 0;
            while (j < frame.length) {
                result[n+j] = result[n+j] + frame[j];
                j++;
            }
            n += overlap;
        }
        return result;
    }

}
