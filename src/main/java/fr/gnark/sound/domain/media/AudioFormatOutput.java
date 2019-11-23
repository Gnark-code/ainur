package fr.gnark.sound.domain.media;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioFormatOutput implements Output {
    protected static final AudioFormat.Encoding ENCODING = AudioFormat.Encoding.PCM_SIGNED;
    public static final double SAMPLE_RATE = 44100;
    protected static final int SAMPLE_SIZE_IN_BITS = 16;
    protected static final int CHANNELS = 2;
    protected static final double GAIN = (Math.pow(2, SAMPLE_SIZE_IN_BITS) / 2) - 1;
    protected static final int FRAME_SIZE = ((SAMPLE_SIZE_IN_BITS + 7) / 8) * CHANNELS;
    protected static final double FRAME_RATE = SAMPLE_RATE;
    protected static final boolean BIG_ENDIAN = false;
    protected static final boolean SIGNED = true;
    private final AudioFormat format;
    private final SourceDataLine line;

    private byte[] _buffer;
    private int bufferIndex = 0;

    public AudioFormatOutput() throws LineUnavailableException {
        this.format = new AudioFormat((float) SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED,
                BIG_ENDIAN);
        line = AudioSystem.getSourceDataLine(format);
        newBuffer();
    }


    public void flush() {
        processData(_buffer);
        newBuffer();
    }

    private void newBuffer() {
        //50  MB of buffer available
        _buffer = new byte[50000000];
        bufferIndex = 0;
    }

    private void processData(final byte[] bytes) {
        try {
            line.open(format, (int) SAMPLE_RATE);
            line.start();
            line.write(bytes, 0, bufferIndex);
            line.drain();
            line.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void storeData(final double levelLeft, final double levelRight) {
        int castLevelLeft = (int) (levelLeft * GAIN);
        int castLevelRight = (int) (levelRight * GAIN);

        _buffer[bufferIndex] = (byte) (castLevelLeft);
        _buffer[bufferIndex + 1] = (byte) (castLevelLeft >>> 8);
        _buffer[bufferIndex + 2] = (byte) (castLevelRight);
        _buffer[bufferIndex + 3] = (byte) (castLevelRight >>> 8);
        bufferIndex = bufferIndex + 4;
    }

    @Override
    public long getThroughputInBytes() {
        return (long) (FRAME_SIZE * FRAME_RATE);
    }

    @Override
    public int getFrameSize() {
        return FRAME_SIZE;
    }
}
