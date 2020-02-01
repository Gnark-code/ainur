package fr.gnark.sound.domain.physics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

class FramesTest {

    @Test
    public void test(){
        Frames frames = new Frames(3);
        frames.addWithOverlap(new double[]{1,2,3,4,5,6,7,8,9},1);

        Iterator<double[]> iterator = frames.iterator();
        final double[] frame1 = iterator.next();
        final double[] frame2 =  iterator.next();
        Assertions.assertEquals(frame1[1], frame2[0]);


    }
}