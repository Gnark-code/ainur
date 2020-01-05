package fr.gnark.sound.domain.media.output;

import java.util.function.Consumer;

public class SlidingWindow {
    private final byte[] _buffer;
    final byte[] window;
    private final int frameSize;
    private final int windowsize;
    private int windowIndex;
    private int writeIndex;
    private Consumer<byte[]> flushOutput;

    public SlidingWindow(final int nbOfFrames, final int frameSize, final int windowSizeInFrames) {
        if (nbOfFrames % windowSizeInFrames != 0) {
            throw new IllegalArgumentException("window size must be a multiple of the number of frames");
        }
        this.frameSize = frameSize;
        this.windowsize = windowSizeInFrames;
        this._buffer = new byte[nbOfFrames * frameSize];
        this.window = new byte[windowSizeInFrames * frameSize];
    }

    public SlidingWindow write(final byte... data) {
        if (data.length != frameSize) {
            throw new IllegalArgumentException("data must be set and be a byte array of length " + frameSize + " bytes");
        }

        System.arraycopy(data, 0, _buffer, writeIndex, frameSize);
        writeIndex = writeIndex + frameSize;
        if (writeIndex == _buffer.length) {
            writeIndex = 0;
        }
        if (writeIndex % (windowsize * frameSize) == 0) {
            flush();
        }
        return this;
    }

    public void setFlushOutput(final Consumer<byte[]> flushOutput) {
        this.flushOutput = flushOutput;
    }

    private byte[] flush() {
        System.arraycopy(_buffer, windowIndex, window, 0, windowsize * frameSize);
        windowIndex = windowIndex + windowsize * frameSize;
        if (windowIndex == _buffer.length) {
            windowIndex = 0;
        }

        if (flushOutput != null) {
            flushOutput.accept(window);
        }
        return window;
    }
}
