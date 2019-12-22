package fr.gnark.sound.domain.media.output;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SlidingWindowTest {
    private static final int NB_OF_FRAMES = 6;
    private static final int FRAME_SIZE = 2;
    private static final int WINDOW_SIZE = 2;

    @Test
    public void testWrongWindowSize() {
        assertThrows(IllegalArgumentException.class, () -> new SlidingWindow(10, 2, 3));
    }

    @Test
    public void testWIthOverlap() {

        SlidingWindow slidingWindow = new SlidingWindow(NB_OF_FRAMES, FRAME_SIZE, WINDOW_SIZE);
        for (int i = 0; i < NB_OF_FRAMES * FRAME_SIZE; i = i + 4) {
            byte[] toWrite = new byte[FRAME_SIZE * WINDOW_SIZE];
            toWrite[0] = (byte) (i);
            toWrite[1] = (byte) (i + 1);
            toWrite[2] = (byte) (i + 2);
            toWrite[3] = (byte) (i + 3);
            slidingWindow.write(toWrite[0], toWrite[1]);
            slidingWindow.write(toWrite[2], toWrite[3]);
            final byte[] flushed = slidingWindow.window;
            Assertions.assertEquals((byte) (i), flushed[0]);
            Assertions.assertEquals((byte) (i + 1), flushed[1]);
            Assertions.assertEquals((byte) (i + 2), flushed[2]);
            Assertions.assertEquals((byte) (i + 3), flushed[3]);
        }

        slidingWindow.write((byte) (128), (byte) (129));
        slidingWindow.write((byte) (130), (byte) (131));
        byte[] last = slidingWindow.window;
        last[0] = (byte) (128);
        last[1] = (byte) (129);
        last[2] = (byte) (130);
        last[3] = (byte) (131);
    }

}