package fr.gnark.sound.domain.physics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OverlapAdderTest {


    @Test
    public void testOverlap() {
        OverlapAdder overlapAdder = new OverlapAdder(1);
        final double[] frame1 = new double[]{1, 2, 3};
        final double[] frame2 = new double[]{4, 5, 6};
        final double[] frame3 = new double[]{7, 8, 9};
        Frames frames = new Frames(3);
        frames.add(frame1);
        frames.add(frame2);
        frames.add(frame3);

        final double[] result = overlapAdder.proceed(frames);
        Assertions.assertEquals(5, result.length);
        Assertions.assertEquals(1.0, result[0]);
        Assertions.assertEquals(6.0, result[1]);
        Assertions.assertEquals(15.0, result[2]);
        Assertions.assertEquals(14.0, result[3]);
        Assertions.assertEquals(9.0, result[4]);
    }
}